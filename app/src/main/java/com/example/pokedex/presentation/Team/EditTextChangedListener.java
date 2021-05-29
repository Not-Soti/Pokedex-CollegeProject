package com.example.pokedex.presentation.Team;

import com.example.pokedex.model.pokeApiModel.Pokemon;

public interface EditTextChangedListener {
    void onEditorAction(Pokemon pokemon, String specie, String name, String[] moves);
}
