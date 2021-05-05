package com.example.pokedex.presentation.Pokedex;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.AndroidViewModel;

import com.example.pokedex.Persistence.Repository;
import com.example.pokedex.model.pokemonModel.Pokedex;
import com.example.pokedex.model.pokemonModel.Pokemon;

import java.util.HashSet;

public class PokedexViewModel extends AndroidViewModel {

    private String TAG = "--PokedexViewModel--";

    private final ObservableArrayList<Pokemon> pokemonList;
    private final ObservableArrayList<Pokemon> favsList;

    private HashSet<Integer> favPokemonIDs;

    private final Repository repository;

    public PokedexViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);

        pokemonList = new ObservableArrayList<>();
        favsList = new ObservableArrayList<>();
        favPokemonIDs = repository.getFavouritePokemon();

    }


    private void updateFavSet(){
        favPokemonIDs = repository.getFavouritePokemon();
    }

    public void updatePokemonList(int count){
        repository.getPokemonListFromRest(count, pokemonList);
    }

    public void updateFavList(){
        repository.getPokemonFavsFromRest(favPokemonIDs, favsList);
    }

/*    public void addPokemonToAll(Pokemon pokemon){
        synchronized (pokemonList){
            pokemonList.add(pokemon);
        }
    }*/

    public void addPokemonToFavs(Pokemon pokemon){
        pokemon.isFav=true;

        synchronized (favsList){
            if(!favsList.contains(pokemon))
                favsList.add(pokemon);
        }

        //Tambien se marca que es favorito en la lista general
        synchronized (pokemonList){
            for (Pokemon poke : pokemonList){
                if (poke.getId().equals(pokemon.getId())){
                    poke.isFav = true;
                }
            }
        }

        repository.addFavPokemon(pokemon);
        updateFavSet();
    }

    public void removePokemonFromFavs(Pokemon pokemon){
        pokemon.isFav = false;

        synchronized (favsList){
            favsList.remove(pokemon);
            Log.d(TAG, "Eliminado de favs list");
        }

        //Tambien se marca que no es favorito en la lista general
        synchronized (pokemonList){
            for (Pokemon poke : pokemonList){
                //Log.d(TAG, "poke.id: "+poke.getId());
                if (poke.getId().equals(pokemon.getId())){
                    poke.isFav = false;
                }
            }
        }
        repository.removeFavPokemon(pokemon);
        updateFavSet();
    }

    public ObservableArrayList<Pokemon> getPokemonList() {
        return pokemonList;
    }

    public ObservableArrayList<Pokemon> getFavsList() {
        return favsList;
    }

    public HashSet<Integer> getFavPokemonIDs() {
        return favPokemonIDs;
    }
}
