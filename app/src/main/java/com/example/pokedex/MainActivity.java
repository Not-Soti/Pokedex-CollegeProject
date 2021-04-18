package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pokedex.presentation.ViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.example.pokedex.pokemonModel.PokemonListInfo;
import com.example.pokedex.pokemonModel.Pokemon;
import com.example.pokedex.pokemonModel.PokemonListItem;
import com.example.pokedex.pokemonModel.Type;
import com.example.pokedex.netAccess.RestService;
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

        String online = isOnline() ? "SI" : "NO";
        Toast.makeText(getApplicationContext(), "Online: ".concat(online), Toast.LENGTH_LONG).show();
        Log.d("---","Online: ".concat(online) );



    }

    private boolean isOnline(){
        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


}