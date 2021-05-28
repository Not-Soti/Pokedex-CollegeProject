package com.example.pokedex.presentation.Team;

import com.example.pokedex.model.pokeApiModel.Pokemon;

public interface SpinnerSelectedListener {
    void onItemSelected(Pokemon pokemon, String specie, String nombre, String[] moves);
    void onNothingSelected();
}
