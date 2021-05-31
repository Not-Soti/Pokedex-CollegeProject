package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pokedex.presentation.Pokedex.PokedexViewModel;

public class MainActivity extends AppCompatActivity {

    private PokedexViewModel pokedexViewModel;
    private String TAG = "--MainActivity--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokedexViewModel = new ViewModelProvider(this).get(PokedexViewModel.class); //TODO ponerlo aqui para que no recarue cada vez que se tiene que entrar

        String online = isOnline() ? "SI" : "NO";
        //Toast.makeText(getApplicationContext(), "Online: ".concat(online), Toast.LENGTH_LONG).show();
        Log.d(TAG,"Online: ".concat(online) );

        //Obtener de las preferencis si el modo noche está activo para aplicarlo
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean nightMode = sharedPreferences.getBoolean("NightMode", false);

        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    private boolean isOnline(){
        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}