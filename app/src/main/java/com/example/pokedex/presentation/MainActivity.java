package com.example.pokedex.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pokedex.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.pokemonModel.PokemonListInfo;
import model.pokemonModel.Pokemon;
import model.pokemonModel.PokemonListItem;
import netAccess.RestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ViewModel viewModel;
    int cantidadPokemon = 10; //TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        LinkedList<Pokemon> aux = new LinkedList<>();
        viewModel.setPokemonList(aux);

        String online = isOnline() ? "SI" : "NO";
        Toast.makeText(getApplicationContext(), "Online: ".concat(online), Toast.LENGTH_LONG).show();
        Log.d("---","Online: ".concat(online) );

        getPokemon();

    }

    private boolean isOnline(){
        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void getPokemon(){

        //Se crea un dialog indicando que se están descargando cosas, el cual se eliminara
        //cuando acabe la descarga.
        ProgressDialog progressDialog = ProgressDialog.show(
                MainActivity.this,
                "Descargando",
                "Se está descargando la informacion necesaria",
                true,
                false);

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

        restService.getPokemonList(cantidadPokemon).enqueue(new Callback<PokemonListInfo>() {
            @Override
            public void onResponse(Call<PokemonListInfo> call, Response<PokemonListInfo> response) {

                PokemonListInfo pokemonListInfo = response.body();
                //Lista con nombre y URL de cada pokemon
                LinkedList<PokemonListItem> lista = new LinkedList<>(pokemonListInfo.getPokemonListItems());

                //Para cada uno se obtiene su ID de la url dada
                for(PokemonListItem poke : lista) {
                    String[] url_split = poke.getUrl().split("/"); //El ID es el ultimo elemento de la URL
                    int id = Integer.parseInt(url_split[url_split.length-1]);

                    //De esta peticion se obtiene el objeto Pokemon necesario con sus características
                    restService.getPokemonById(id).enqueue(new Callback<Pokemon>() {
                        @Override
                        public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                            Pokemon pokemon = response.body();
                            viewModel.getPokemonList().add(pokemon);

                            //Obtener su imagen para la lista
                            getListSprite(pokemon);


                            //Si se obtienen todos, se ordena la lista por ID pues la descarga es asíncrona
                            if(viewModel.getPokemonList().size() == cantidadPokemon){
                                Collections.sort(viewModel.getPokemonList(), (p1, p2) -> p1.getId() - p2.getId());
                                Log.d("TAG", "Lista ordenada");
                                progressDialog.dismiss();
                            }
                        }
                        @Override
                        public void onFailure(Call<Pokemon> call, Throwable t) {
                            //Si no se obtiene un pokemon, se reduce la cantidad de pokemon necesaria
                            cantidadPokemon--;
                        }
                    });
                }


            }
            @Override
            public void onFailure(Call<PokemonListInfo> call, Throwable t) {

            }
        });
    }

    private void getListSprite(Pokemon pokemon){
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
                    sprite = ResourcesCompat.getDrawable(getResources(), R.drawable.pokemock, null);
                }
                pokemon.listSprite = sprite;
            }
        });
        thread.start();
    }

}