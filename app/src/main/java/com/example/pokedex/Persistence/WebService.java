package com.example.pokedex.Persistence;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModel;

import com.example.pokedex.R;
import com.example.pokedex.model.pokemonModel.Pokemon;
import com.example.pokedex.model.pokemonModel.PokemonListInfo;
import com.example.pokedex.model.pokemonModel.PokemonListItem;
import com.example.pokedex.model.pokemonModel.Type;
import com.example.pokedex.netAccess.RestService;
import com.example.pokedex.presentation.PokedexViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebService {

    private String TAG = "--WebService--";
    private Context context;
    private ViewModel viewModel;

    private RestService restService;
    private Gson gson;
    private Retrofit retrofit;

    private int type;

    public WebService(Context c, ViewModel vm, int t){
        context = c;
        viewModel = vm;
        type = t;

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        restService = retrofit.create(RestService.class);
    }

    public void getPokemonFromJSON(int pokemonCount){
        restService.getPokemonList(pokemonCount).enqueue(new Callback<PokemonListInfo>() {
            @Override
            public void onResponse(Call<PokemonListInfo> call, Response<PokemonListInfo> response) {

                PokemonListInfo pokemonListInfo = response.body();


                //Lista con nombre y URL de cada pokemon
                LinkedList<PokemonListItem> lista = new LinkedList<>(pokemonListInfo.getPokemonListItems());

                //Para cada uno se obtiene su ID de la url dada
                for(PokemonListItem pokeInfo : lista) {
                    String[] url_split = pokeInfo.getUrl().split("/"); //El ID es el ultimo elemento de la URL
                    int id = Integer.parseInt(url_split[url_split.length-1]);
                    getPokemonByID(id);
                }
            }
            @Override
            public void onFailure(Call<PokemonListInfo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getFavsFromJSON(HashSet<Integer> pokemonIDs){
        for(int id : pokemonIDs){
            getPokemonByID(id);
        }
    }
    private void getPokemonByID(int id){
        //De esta peticion se obtiene el objeto Pokemon necesario con sus características
        restService.getPokemonById(id).enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                Pokemon pokemon = response.body();
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

    private void getListSprite(Pokemon pokemon){
        //Se obtiene su imagen de la URL apropiada en un hilo nuevo
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Log.d(TAG, "Pidiendo sprite del pokemon "+pokemon.getId());
                String spriteURL = pokemon.getSprites().getFrontDefault();
                Drawable sprite = null;
                try {
                    InputStream is = (InputStream) new URL(spriteURL).getContent();
                    //Imagen obtenida de internet
                    sprite = Drawable.createFromStream(is, "PokeApi");
                } catch (IOException e) {
                    e.printStackTrace();
                    //Si no se obtiene, se pone la guardada en la aplicacion
                    sprite = ResourcesCompat.getDrawable(context.getResources(), R.drawable.pokemock, null);
                }
                pokemon.listSprite = sprite;
                if(type == 0) {
                    ((PokedexViewModel) viewModel).addPokemonToAll(pokemon);
                }else if(type == 1){
                    ((PokedexViewModel) viewModel).addPokemonToFavs(pokemon);
                }
                //Log.d(TAG, "añadido sprite del pokemon "+pokemon.getId() + ", total añadidos: "+viewModel.getPokemonList().size());
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
}
