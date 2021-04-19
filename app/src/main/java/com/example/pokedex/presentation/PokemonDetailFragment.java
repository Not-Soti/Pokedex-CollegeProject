package com.example.pokedex.presentation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokedex.R;
import com.example.pokedex.netAccess.RestService;
import com.example.pokedex.pokemonModel.Pokemon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PokemonDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokemonDetailFragment extends Fragment {

    public static final String PARAM_POKEMON_ID = "pokemon_id";
    private final String TAG = "PokemonDetailFragment";
    private int pokemonID;
    private ViewModel viewModel;

    private ImageView pokemonSprite;
    private TextView nameTv, idTv;

    public PokemonDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pokemonID PokemonID number to represent in the fragment
     * @return A new instance of fragment PokemonDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PokemonDetailFragment newInstance(int pokemonID) {
        PokemonDetailFragment fragment = new PokemonDetailFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_POKEMON_ID, pokemonID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pokemonID = getArguments().getInt(PARAM_POKEMON_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_pokemon_detail, container, false);

        pokemonSprite = theView.findViewById(R.id.poke_detail_sprite);
        nameTv = theView.findViewById(R.id.poke_detail_nameTv);
        idTv = theView.findViewById(R.id.poke_detail_idTv);

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        Log.d(TAG, "Pokemon: "+ pokemonID);

        getPokemonInfo();

        return theView;
    }

    private void getPokemonInfo(){
        RestService restService;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        restService = retrofit.create(RestService.class);

        restService.getPokemonById(pokemonID).enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                int responseCode = response.code();
                Log.d(TAG, "Response code: " + responseCode);
                Pokemon pokemon = response.body();

                idTv.setText(String.format(Locale.getDefault(),"Nº %d", pokemon.getId()));

                String name = pokemon.getName();
                String capitalizedName = name.substring(0,1).toUpperCase() + name.substring(1); //Poner la primera letra en mayuscula
                nameTv.setText(capitalizedName);

                getPokemonSprite(pokemon);

            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {

            }
        });
    }

    private void getPokemonSprite(Pokemon pokemon){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Pidiendo sprite del pokemon "+pokemon.getId());
                String spriteURL = pokemon.getSprites().getOther().getOfficialArtwork().getFrontDefault();
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

                Drawable finalSprite = sprite;
                getActivity().runOnUiThread(() -> pokemonSprite.setBackground(finalSprite));
            }
        });
        thread.start();
    }
}