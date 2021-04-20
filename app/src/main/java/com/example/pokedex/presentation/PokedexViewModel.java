package com.example.pokedex.presentation;

import androidx.databinding.ObservableArrayList;

import com.example.pokedex.model.pokemonModel.Pokemon;

public class PokedexViewModel extends androidx.lifecycle.ViewModel {

    private ObservableArrayList<Pokemon> pokemonList = new ObservableArrayList<>();

    public ObservableArrayList<Pokemon> getPokemonList(){
        return pokemonList;
    }
    public void setPokemonList(ObservableArrayList<Pokemon> pokeList){
        pokemonList=pokeList;
    }

    public void addPokemon(Pokemon pokemon){
        synchronized (pokemonList){
            pokemonList.add(pokemon);
        }
    }

}
