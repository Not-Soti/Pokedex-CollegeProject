package com.example.pokedex.netAccess;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.ObservableArrayList;

import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.Language;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail_Language;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail_Language__3;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail_LearnedByPokemon;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail_Name;
import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.example.pokedex.model.pokeApiModel.PokemonIndex;
import com.example.pokedex.model.pokeApiModel.PokemonIndexItem;
import com.example.pokedex.model.pokeApiModel.Type;
import com.example.pokedex.model.pokeApiModel.TypeDetail.TypeDetail;
import com.example.pokedex.model.pokeApiModel.TypeDetail.TypeDetail_Pokemon;
import com.example.pokedex.model.pokeApiModel.TypeDetail.TypeDetail_Pokemon__1;
import com.example.pokedex.netAccess.RestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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

    //Campo que indica el lenguaje del sistema, usado para obtener
    //los elementos en el idioma correspondiente
    private String systemLanguaje;


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

    /**
     * Metodo que descarga los pokemon que coincidad con el tipo elegido, y su ID es menor que
     * el que se pasa como argumento
     * @param pokemonCount: Maximo ID a descargar
     * @param pokemonList: Lista a la que se añadiran los pokemon
     * @param type: Tipo del pokemon que buscar
     */
    public void downloadPokemonByType(int pokemonCount, List<Pokemon> pokemonList, String type) {

        //Se descarga la informacion del tipo que se pasa como parametro

        restService.getPokemonTypeDetail(type).enqueue(new Callback<TypeDetail>() {
            @Override
            public void onResponse(Call<TypeDetail> call, Response<TypeDetail> response) {
                //Se obtiene la informacion generica del tipo pokemon
                TypeDetail typeDetail = response.body();

                if(typeDetail!=null){//Se obtiene la lista de pokemon de dicho tipo
                    LinkedList<TypeDetail_Pokemon> lista = new LinkedList<>(typeDetail.getPokemon());

                    //Se recorren los pokemon buscando su ID, para luego descargarlos individualmente
                    for (TypeDetail_Pokemon poke : lista) {
                        TypeDetail_Pokemon__1 pokemonAux = poke.getPokemon(); //Transicion necesaria para obtener el ID

                        String[] url_split = pokemonAux.getUrl().split("/"); //El ID es el ultimo elemento de la URL
                        int id = Integer.parseInt(url_split[url_split.length - 1]);

                        if (id <= pokemonCount) {
                            getPokemonByID(id, pokemonList);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TypeDetail> call, Throwable t) {

            }
        });

    }

    public void downloadPokemonByMove(int pokemonCount, ObservableArrayList<Pokemon> pokemonList, int moveID) {
        restService.getMoveDetailByIds(moveID).enqueue(new Callback<MoveDetail>() {
            @Override
            public void onResponse(Call<MoveDetail> call, Response<MoveDetail> response) {
                MoveDetail moveDetail = response.body();

                if(moveDetail != null){
                    LinkedList<MoveDetail_LearnedByPokemon> lista = new LinkedList<>(moveDetail.getMoveDetailLearnedByPokemon());

                    //Se recorren los pokemon buscando su ID, para luego descargarlos individualmente
                    for (MoveDetail_LearnedByPokemon poke : lista) {
                        String pokemonURL = poke.getUrl(); //Transicion necesaria para obtener el ID

                        String[] url_split = pokemonURL.split("/"); //El ID es el ultimo elemento de la URL
                        int id = Integer.parseInt(url_split[url_split.length - 1]);

                        if (id <= pokemonCount) {
                            Log.d(TAG, "Id" + id);
                            getPokemonByID(id, pokemonList);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoveDetail> call, Throwable t) {

            }
        });
    }

    public void downloadMoves(List<MoveDetail> moves, int moveCount) {
        //Se descargan los movimientos 1 a 1 y se añaden al hashmap.
        for(int i=1; i<=moveCount; i++){
            restService.getMoveDetailByIds(i).enqueue(new Callback<MoveDetail>() {
                @Override
                public void onResponse(Call<MoveDetail> call, Response<MoveDetail> response) {
                    MoveDetail moveDetail = response.body();

                    if(moveDetail != null){
                        moves.add(moveDetail);
                    }
                }

                @Override
                public void onFailure(Call<MoveDetail> call, Throwable t) {

                }
            });
        }
    }
}
