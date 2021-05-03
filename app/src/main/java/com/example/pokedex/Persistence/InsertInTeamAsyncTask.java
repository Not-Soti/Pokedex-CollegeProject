package com.example.pokedex.Persistence;


import android.os.AsyncTask;
import android.util.Log;

public class InsertInTeamAsyncTask extends AsyncTask<PokemonTeamEntity, Void, Void> {

    private PokemonDAO pokemonDAO;

    public InsertInTeamAsyncTask(PokemonDAO dao){
        this.pokemonDAO = dao;
    }

    @Override
    protected Void doInBackground(PokemonTeamEntity... pokemonTeamEntities) {
        pokemonDAO.insertPokemon(pokemonTeamEntities[0]);
        Log.d("InsertAsycTask", "Pokemon inserted");
        return null;
    }
}
