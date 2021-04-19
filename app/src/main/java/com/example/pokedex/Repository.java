package com.example.pokedex;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import com.example.pokedex.netAccess.RestService;
import com.example.pokedex.pokemonModel.Pokemon;
import com.example.pokedex.pokemonModel.PokemonListInfo;
import com.example.pokedex.pokemonModel.PokemonListItem;
import com.example.pokedex.pokemonModel.Type;
import com.example.pokedex.presentation.PokedexFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private String TAG = "--Repository--";
    private RestService restService;
    private Fragment fragment;

    public Repository(Fragment f){
        fragment=f;
    }


    public void getPokemon(int pokemonCount, ArrayList<Pokemon> pokemonList){

        RestService restService;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        restService = retrofit.create(RestService.class);

        /*De esta peticion se obtiene una lista cuyos elementos tienen:
            1- Nombre del pokemon
            2- URL donde obtener la informacion del pokemon
         */


        restService.getPokemonList(pokemonCount).enqueue(new Callback<PokemonListInfo>() {
            @Override
            public void onResponse(Call<PokemonListInfo> call, Response<PokemonListInfo> response) {

                PokemonListInfo pokemonListInfo = response.body();
                Log.d(TAG, response.message());

                //Lista con nombre y URL de cada pokemon
                LinkedList<PokemonListItem> lista = new LinkedList<>(pokemonListInfo.getPokemonListItems());

                //Para cada uno se obtiene su ID de la url dada
                for(PokemonListItem poke : lista) {
                    String[] url_split = poke.getUrl().split("/"); //El ID es el ultimo elemento de la URL
                    int id = Integer.parseInt(url_split[url_split.length-1]);

                    Log.d(TAG,"Pidiendo pokemon "+id);

                    //De esta peticion se obtiene el objeto Pokemon necesario con sus características
                    restService.getPokemonById(id).enqueue(new Callback<Pokemon>() {
                        @Override
                        public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                            Pokemon pokemon = response.body();
                            //viewModel.getPokemonList().add(pokemon);

                            //Obtener las imagenes de sus tipos
                            getTypeNames(pokemon);

                            //Obtener su imagen para la lista
                            getListSprite(pokemon, pokemonList, pokemonCount);

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

    /**
     * Metodo que obtiene el sprite del pokemon dado
     * de la URL de referencia
     * @param pokemon: Pokemon del que obtener el sprite
     */
    private void getListSprite(Pokemon pokemon, ArrayList<Pokemon> pokemonList, int pokemonCount){
        //Se obtiene su imagen de la URL apropiada en un hilo nuevo
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String spriteURL = pokemon.getSprites().getFrontDefault();
                Drawable sprite = null;
                try {
                    InputStream is = (InputStream) new URL(spriteURL).getContent();
                    //Imagen obtenida de internet
                    sprite = Drawable.createFromStream(is, "PokeApi");
                } catch (IOException e) {
                    e.printStackTrace();
                    //Si no se obtiene, se pone la guardada en la aplicacion
                    sprite = ResourcesCompat.getDrawable(fragment.getContext().getResources(), R.drawable.pokemock, null);
                }
                pokemon.listSprite = sprite;
                pokemonList.add(pokemon);
                Log.d(TAG, "Añadido pokemon "+pokemonList.size());
                if(pokemonList.size() == pokemonCount){
                    Collections.sort(pokemonList, (p1, p2) -> p1.getId() - p2.getId());
                    Log.d("TAG", "Lista ordenada");
                    ((PokedexFragment)fragment).populatePokemonAdapter();
                }
            }
        });
        thread.start();
    }

    /**
     * Metodo que obtiene los tipos del pokemon dado
     * @param pokemon: Pokemon del que obtener los tipos
     */
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
