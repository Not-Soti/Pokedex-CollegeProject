package com.example.pokedex.Persistence;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.example.pokedex.netAccess.RestService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;

public class Repository {


    private String TAG = "--Repository--";
    private RestService restService;
    private Context context;
    //private ViewModel viewModel;
    //private Application application;

    private PokemonDAO pokemonDAO;
    private LiveData<List<PokemonTeamEntity>> pokemonTeam;

    public static final String favFileName = "favourites";

    public Repository(Application app){
        context = app.getApplicationContext();

        pokemonDAO = Database.getInstance(app).pokemonDAO();
        pokemonTeam = pokemonDAO.getFullTeam();
    }

    public void getPokemonListFromRest(int pokemonCount, List<Pokemon> pokemonList){
        WebService webService = new WebService(context);
        webService.getPokemonFromJSON(pokemonCount, pokemonList);
    }

    public void getPokemonFavsFromRest(HashSet<Integer> pokemonIDs, List<Pokemon> pokemonList){
        WebService webService = new WebService(context);
        webService.getFavsFromJSON(pokemonIDs, pokemonList);
    }

    public void downloadPokemonFromId(int id, List<Pokemon> pokemonList){
        WebService webService = new WebService(context);
        webService.getPokemonByID(id, pokemonList);
    }

    /**
     * Metodo utilizado para obtener los id de los
     * Pokemon que se han marcado como favoritos
     * @return HashSet conteniendo los id de los pokemon favoritos
     */
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

    public LiveData<List<PokemonTeamEntity>> getPokemonTeam(){
        return pokemonTeam;
    }

    public void insertPokemonIntoTeam(PokemonTeamEntity poke){
        new InsertInTeamAsyncTask(pokemonDAO).execute(poke);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = prefs.edit();
        e.putInt("cantidad_equipo", prefs.getInt("cantidad_equipo", 0)+1);
        e.apply();
    }

}
