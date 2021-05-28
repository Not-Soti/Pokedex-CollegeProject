package com.example.pokedex.Persistence;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.example.pokedex.netAccess.RestService;
import com.example.pokedex.netAccess.WebService;

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

    /**
     * Metodo que descarga los n primeros pokemon, donde n es el parametro que se
     * pasa como argumento
     * @param pokemonCount: Cantidad de pokemon a descargar
     * @param pokemonList: Lista a la que se añadiran los pokemon
     */
    public void downloadPokemonAll(int pokemonCount, List<Pokemon> pokemonList){
        WebService webService = new WebService(context);
        webService.getPokemonFromJSON(pokemonCount, pokemonList);
    }

    /**
     * Metodo que descarga los pokemon favoritos
     * @param pokemonIDs: ID de los pokemon favoritos
     * @param pokemonList: Lista a la que se añadiran los pokemon
     */
    public void downloadPokemonFavourites(HashSet<Integer> pokemonIDs, List<Pokemon> pokemonList){
        WebService webService = new WebService(context);
        webService.getFavsFromJSON(pokemonIDs, pokemonList);
    }

    /**
     * Metodo que descarga los pokemon que coincidad con el tipo elegido, y su ID es menor que
     * el que se pasa como argumento
     * @param pokemonCount: Maximo ID a descargar
     * @param pokemonList: Lista a la que se añadiran los pokemon
     * @param type: Tipo del pokemon que buscar
     */
    public void downloadPokemonByType(int pokemonCount, List<Pokemon> pokemonList, String type){

        WebService webService = new WebService(context);
        webService.downloadPokemonByType(pokemonCount, pokemonList, type);
    }

    public void downloadPokemonById(int id, List<Pokemon> pokemonList){
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

    public void removePokemonFromTeam(PokemonTeamEntity poke){
        new RemoveTeamAsyncTask(pokemonDAO).execute(poke);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = prefs.edit();
        e.putInt("cantidad_equipo", prefs.getInt("cantidad_equipo", 0)-1);
        e.apply();
    }

    public void updatePokemonInTeam(PokemonTeamEntity poke){
        new UpdateInTeamAsyncTask(pokemonDAO).execute(poke);
    }

}
