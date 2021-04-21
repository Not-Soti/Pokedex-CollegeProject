package com.example.pokedex.presentation;

import androidx.databinding.ObservableArrayList;

import com.example.pokedex.model.pokemonModel.Pokedex;
import com.example.pokedex.model.pokemonModel.Pokemon;

import java.util.HashSet;

public class PokedexViewModel extends androidx.lifecycle.ViewModel {

    private ObservableArrayList<Pokemon> pokemonList = new ObservableArrayList<>();
    private ObservableArrayList<Pokemon> favsList = new ObservableArrayList<>();

    public ObservableArrayList<Pokemon> getPokemonList(){
        return pokemonList;
    }
    public void setPokemonList(ObservableArrayList<Pokemon> pokeList){
        pokemonList=pokeList;
    }

    public ObservableArrayList<Pokemon> getFavsList() {
        return favsList;
    }
    public void setFavsList(ObservableArrayList<Pokemon> favsList) {
        this.favsList = favsList;
    }

    public void addPokemonToAll(Pokemon pokemon){
        synchronized (pokemonList){
            pokemonList.add(pokemon);
        }
    }

    public void addPokemonToFavs(Pokemon pokemon){
        synchronized (favsList){
            favsList.add(pokemon);
        }
    }

    public void removePokemonFromFavs(Pokemon pokemon){
        synchronized (favsList){
            favsList.remove(pokemon);
        }
    }

}
