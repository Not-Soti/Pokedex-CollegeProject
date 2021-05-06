package com.example.pokedex.presentation.Pokedex;

import com.example.pokedex.model.pokeApiModel.Pokemon;

public interface ButtonFavListener {
    void onClick(Pokemon p, boolean isFav);
}
