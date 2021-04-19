package com.example.pokedex.presentation;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;

import java.util.ArrayList;
import java.util.List;

import com.example.pokedex.pokemonModel.Pokemon;

public class ViewModel extends androidx.lifecycle.ViewModel {

    private List<Pokemon> pokemonList2;
    private ArrayList<Pokemon> pokemonList = new ArrayList<>();

    public ArrayList<Pokemon> getPokemonList(){
        return pokemonList;
    }
    public void setPokemonList(ArrayList<Pokemon> pokeList){
        pokemonList=pokeList;
    }

}
