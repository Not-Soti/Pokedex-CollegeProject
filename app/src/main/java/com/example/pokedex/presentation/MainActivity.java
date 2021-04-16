package com.example.pokedex.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pokedex.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;

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

    LinkedList<Pokemon> pokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokemonList = new LinkedList<Pokemon>();

        String online = isOnline() ? "SI" : "NO";
        Toast.makeText(getApplicationContext(), "Online: ".concat(online), Toast.LENGTH_LONG).show();
        Log.d("---","Online: ".concat(online) );

        RestService restService;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        restService = retrofit.create(RestService.class);

        restService.getPokemonList(10).enqueue(new Callback<PokemonListInfo>() {
            @Override
            public void onResponse(Call<PokemonListInfo> call, Response<PokemonListInfo> response) {

                PokemonListInfo pokemonListInfo = response.body();
                LinkedList<PokemonListItem> lista = new LinkedList<>(pokemonListInfo.getPokemonListItems());
                for(PokemonListItem p : lista){
                    Log.d("-------", "Nombre: " + p.getName());
                }


            }

            @Override
            public void onFailure(Call<PokemonListInfo> call, Throwable t) {

            }
        });

    }

    boolean isOnline(){
        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}