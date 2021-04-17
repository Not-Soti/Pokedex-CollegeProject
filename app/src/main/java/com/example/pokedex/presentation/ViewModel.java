package com.example.pokedex.presentation;

import java.util.List;

import model.pokemonModel.Pokemon;

public class ViewModel extends androidx.lifecycle.ViewModel {

    private List<Pokemon> pokemonList;

    public List<Pokemon> getPokemonList(){
        return pokemonList;
    }
    public void setPokemonList(List<Pokemon> pokeList){
        pokemonList=pokeList;
    }

}
