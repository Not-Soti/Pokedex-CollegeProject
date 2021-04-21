package com.example.pokedex.Persistence;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;


import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModel;

import com.example.pokedex.R;
import com.example.pokedex.model.pokemonModel.Pokemon;
import com.example.pokedex.model.pokemonModel.PokemonListInfo;
import com.example.pokedex.model.pokemonModel.PokemonListItem;
import com.example.pokedex.model.pokemonModel.Type;
import com.example.pokedex.netAccess.RestService;
import com.example.pokedex.presentation.PokedexFragment;
import com.example.pokedex.presentation.PokedexViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private String TAG = "--Repository--";
    private RestService restService;
    private Context context;
    private ViewModel viewModel;

    public static final String favFileName = "favourites";

    public Repository(Context c, ViewModel vm){
        context=c;
        viewModel = vm;
    }

    public void getPokemonListFromRest(int pokemonCount){
        WebService webService = new WebService(context, viewModel, false);
        webService.getPokemonFromJSON(pokemonCount);
    }

    public void getPokemonFavsFromRest(HashSet<Integer> pokemonIDs){
        WebService webService = new WebService(context, viewModel, true);
        webService.getFavsFromJSON(pokemonIDs);
    }

    public HashSet<Integer> getFavouritePokemon(){
        //Obtener los que ya estaban guardados
        FileInputStream fis;
        ObjectInputStream ois;
        FileOutputStream fos;
        ObjectOutputStream oos;
        HashSet<Integer> favs = new HashSet<>();
        File file = new File(context.getFilesDir(), favFileName);

        if(!file.exists()){
            try {
                Log.d(TAG, "Creado fichero");
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if(file.length() == 0){
                /*Si nunca se ha escrito en el fichero, hay que hacerlo porque
                  si no produce EOFException al leerlo
                */
                Log.d(TAG, "Creando fichero");
                fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(favs);
                oos.close();
                fos.close();
            }
            fis = context.openFileInput(file.getName());
            ois = new ObjectInputStream(fis);
            favs = (HashSet<Integer>) ois.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return favs;
    }

    public synchronized void addFavPokemon(Pokemon p){
        HashSet<Integer> favs = getFavouritePokemon();
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        favs.add(p.getId());

        File file = new File(context.getFilesDir(), favFileName);
        if(file.exists()) {
            try {
                fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(favs);
                oos.close();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Pokemon guardado. Hay "+favs.size());
    }

    public synchronized void removeFavPokemon(Pokemon p){
        HashSet<Integer> favs = getFavouritePokemon();
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        favs.remove(p.getId());

        File file = new File(context.getFilesDir(), favFileName);
        if(file.exists()) {
            try {
                fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(favs);
                oos.close();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Pokemon elimiado. Hay "+favs.size());
    }
}
