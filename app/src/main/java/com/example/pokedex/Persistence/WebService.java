package com.example.pokedex.Persistence;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.example.pokedex.model.pokeApiModel.PokemonIndex;
import com.example.pokedex.model.pokeApiModel.PokemonIndexItem;
import com.example.pokedex.model.pokeApiModel.Type;
import com.example.pokedex.netAccess.RestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
    //private ViewModel viewModel;

    private RestService restService;
    private Gson gson;
    private Retrofit retrofit;

    //private boolean treatingFavPokemon;

    public WebService(Context c){
        context = c;
        //viewModel = vm;
        //treatingFavPokemon = treatingFavs;

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        restService = retrofit.create(RestService.class);
    }

    public void getPokemonFromJSON(int pokemonCount, List<Pokemon> pokemonList){
        restService.getPokemonList(pokemonCount).enqueue(new Callback<PokemonIndex>() {
            @Override
            public void onResponse(Call<PokemonIndex> call, Response<PokemonIndex> response) {

                PokemonIndex pokemonIndex = response.body();


                //Lista con nombre y URL de cada pokemon
                LinkedList<PokemonIndexItem> lista = new LinkedList<>(pokemonIndex.getPokemonIndexItems());

                //Para cada uno se obtiene su ID de la url dada
                for(PokemonIndexItem pokeInfo : lista) {
                    String[] url_split = pokeInfo.getUrl().split("/"); //El ID es el ultimo elemento de la URL
                    int id = Integer.parseInt(url_split[url_split.length-1]);

                    getPokemonByID(id, pokemonList);
                }
            }
            @Override
            public void onFailure(Call<PokemonIndex> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getFavsFromJSON(HashSet<Integer> pokemonIDs, List<Pokemon> pokemonList){
        for(int id : pokemonIDs){
            Log.d(TAG, "getFavsFromJSON id "+ id);
            getPokemonByID(id, pokemonList);
        }
    }

    public void getPokemonByID(int id, List<Pokemon> pokemonList){

        //De esta peticion se obtiene el objeto Pokemon necesario con sus características
        restService.getPokemonById(id).enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                Pokemon pokemon = response.body();
                //Obtener las imagenes de sus tipos
                getTypeNames(pokemon);

                //Obtener su imagen para la lista
                getListSprite(pokemon, pokemonList);

            }
            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getListSprite(Pokemon pokemon, List<Pokemon> pokemonList){
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
/*                if(!treatingFavPokemon) {
                    //((PokedexViewModel) viewModel).addPokemonToAll(pokemon);

                }else{
                    //((PokedexViewModel) viewModel).addPokemonToFavs(pokemon);
                }*/
                addPokemonToList(pokemonList, pokemon);
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


    private synchronized void addPokemonToList(List<Pokemon> pokemonList, Pokemon pokemon){
        pokemonList.add(pokemon);
    }
}
