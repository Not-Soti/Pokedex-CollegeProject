package com.example.pokedex;


import android.content.Context;


import com.example.pokedex.netAccess.RestService;

public class Repository {

    private String TAG = "--Repository--";
    private RestService restService;
    private Context context;


    public Repository(Context c){
        context=c;
    }

}
