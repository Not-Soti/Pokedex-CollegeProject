package com.example.pokedex.presentation;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;

import java.util.List;

import com.example.pokedex.pokemonModel.Pokemon;

public class ViewModel extends androidx.lifecycle.ViewModel {

    private List<Pokemon> pokemonList2;
    private ObservableArrayList<Pokemon> pokemonList = new ObservableArrayList<>();

    public ObservableArrayList<Pokemon> getPokemonList(){
        return pokemonList;
    }
    public void setPokemonList(ObservableArrayList<Pokemon> pokeList){
        pokemonList=pokeList;
    }

}
