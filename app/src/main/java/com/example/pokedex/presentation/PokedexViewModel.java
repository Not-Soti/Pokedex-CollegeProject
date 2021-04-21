package com.example.pokedex.presentation;

import android.util.Log;

import androidx.databinding.ObservableArrayList;

import com.example.pokedex.Persistence.Repository;
import com.example.pokedex.model.pokemonModel.Pokedex;
import com.example.pokedex.model.pokemonModel.Pokemon;

import java.util.HashSet;

public class PokedexViewModel extends androidx.lifecycle.ViewModel {

    private String TAG = "--PokedexViewModel--";

    private final ObservableArrayList<Pokemon> pokemonList = new ObservableArrayList<>();
    private final ObservableArrayList<Pokemon> favsList = new ObservableArrayList<>();

    public ObservableArrayList<Pokemon> getPokemonList(){
        return pokemonList;
    }
    public ObservableArrayList<Pokemon> getFavsList() {
        return favsList;
    }

    public void addPokemonToAll(Pokemon pokemon){
        synchronized (pokemonList){
            pokemonList.add(pokemon);
        }
    }

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
    }

}
