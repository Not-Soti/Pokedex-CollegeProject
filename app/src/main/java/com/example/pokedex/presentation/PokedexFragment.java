package com.example.pokedex.presentation;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.Observable;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokedex.MainActivity;
import com.example.pokedex.R;
import com.example.pokedex.Repository;
import com.example.pokedex.netAccess.RestService;
import com.example.pokedex.pokemonModel.Pokemon;
import com.example.pokedex.pokemonModel.PokemonListInfo;
import com.example.pokedex.pokemonModel.PokemonListItem;
import com.example.pokedex.pokemonModel.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PokedexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokedexFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecycleViewAdapter pokemonAdapter;
    private ViewModel viewModel;
    private int pokemonCount = 10;
    private ProgressDialog progressDialog;
    private String TAG = "--PokedexFragment--";

    public PokedexFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PokedexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PokedexFragment newInstance(String param1, String param2) {
        PokedexFragment fragment = new PokedexFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_pokedex, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        viewModel.getPokemonList().clear();
        viewModel.getPokemonList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Pokemon>>() {
            @Override
            public void onChanged(ObservableList<Pokemon> sender) {
            }
            @Override
            public void onItemRangeChanged(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
            }
            @Override
            public void onItemRangeInserted(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
                //Log.d(TAG, "Tam lista: "+sender.size());
                if(sender.size() == pokemonCount){
                    Collections.sort(viewModel.getPokemonList(), (p1, p2) -> p1.getId() - p2.getId());
                    populatePokemonAdapter();
                }
            }
            @Override
            public void onItemRangeMoved(ObservableList<Pokemon> sender, int fromPosition, int toPosition, int itemCount) {
            }
            @Override
            public void onItemRangeRemoved(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
            }
        });

        recyclerView = theView.findViewById(R.id.pokedexFrag_recyclerView);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String numberOfPokemonStr = sharedPreferences.getString("pokemon_count", "10");
        int number = 10;
        try{
            number = Integer.parseInt(numberOfPokemonStr);
        }catch (Exception e){
            Log.d("TAAAAAG", "Mal numero: "+number);
        }
        pokemonCount=number;

        //Se crea un dialog indicando que se están descargando cosas, el cual se eliminara
        //cuando acabe la descarga.
        progressDialog = ProgressDialog.show(
                getContext(),
                "Descargando",
                "Se está descargando la informacion necesaria",
                true,
                false);

        /*Repository repository = new Repository(PokedexFragment.this);
        repository.getPokemon(pokemonCount, viewModel.getPokemonList());*/

        getPokemon();

        return theView;
    }

    private void getPokemon(){
        RestService restService;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        restService = retrofit.create(RestService.class);

        restService.getPokemonList(pokemonCount).enqueue(new Callback<PokemonListInfo>() {
            @Override
            public void onResponse(Call<PokemonListInfo> call, Response<PokemonListInfo> response) {

                PokemonListInfo pokemonListInfo = response.body();
                Log.d(TAG, response.message());

                //Lista con nombre y URL de cada pokemon
                LinkedList<PokemonListItem> lista = new LinkedList<>(pokemonListInfo.getPokemonListItems());

                //Para cada uno se obtiene su ID de la url dada
                for(PokemonListItem pokeInfo : lista) {
                    String[] url_split = pokeInfo.getUrl().split("/"); //El ID es el ultimo elemento de la URL
                    int id = Integer.parseInt(url_split[url_split.length-1]);

                    //De esta peticion se obtiene el objeto Pokemon necesario con sus características
                    restService.getPokemonById(id).enqueue(new Callback<Pokemon>() {
                        @Override
                        public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                            Pokemon pokemon = response.body();
                            //viewModel.getPokemonList().add(pokemon);

                            //Obtener las imagenes de sus tipos
                            getTypeNames(pokemon);

                            //Obtener su imagen para la lista
                            getListSprite(pokemon);

                        }
                        @Override
                        public void onFailure(Call<Pokemon> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<PokemonListInfo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getListSprite(Pokemon pokemon){
        //Se obtiene su imagen de la URL apropiada en un hilo nuevo
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Pidiendo sprite del pokemon "+pokemon.getId());
                String spriteURL = pokemon.getSprites().getFrontDefault();
                Drawable sprite = null;
                try {
                    InputStream is = (InputStream) new URL(spriteURL).getContent();
                    //Imagen obtenida de internet
                    sprite = Drawable.createFromStream(is, "PokeApi");
                } catch (IOException e) {
                    e.printStackTrace();
                    //Si no se obtiene, se pone la guardada en la aplicacion
                    sprite = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.pokemock, null);
                }
                pokemon.listSprite = sprite;
                viewModel.addPokemon(pokemon);
                Log.d(TAG, "añadido sprite del pokemon "+pokemon.getId() + ", total añadidos: "+viewModel.getPokemonList().size());
            }
        });
        thread.start();
    }

    private void getTypeNames(Pokemon pokemon){
        //Todos los pokemon tienen 1 tipo, y algunos tienen 2
        //Obtener el nombre del primer tipo
        List<Type> types = pokemon.getTypes();
        Type type1 = types.get(0);
        String type1Name = type1.getType().getName();
        pokemon.type1Str = type1Name;

        //Obtener el segundo, si tiene
        if(types.size()==2){
            Type type2 = types.get(1);
            String type2Name = type2.getType().getName();
            pokemon.type2Str = type2Name;
            //Log.d(TAG,"tipo2 "+ type2Name);
        }else{
            pokemon.type2Str = null;
        }
    }

    public void populatePokemonAdapter(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "tam: " + viewModel.getPokemonList().size());
                progressDialog.dismiss();
                pokemonAdapter = new RecycleViewAdapter(getContext(), viewModel.getPokemonList());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(pokemonAdapter);
            }
        });
    }
}