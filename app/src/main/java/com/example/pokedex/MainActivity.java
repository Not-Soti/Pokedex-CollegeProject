package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pokedex.presentation.PokedexViewModel;

public class MainActivity extends AppCompatActivity {

    private PokedexViewModel pokedexViewModel;
    private String TAG = "--MainActivity--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokedexViewModel = new ViewModelProvider(this).get(PokedexViewModel.class);

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