package com.example.pokedex.Persistence;

import android.os.AsyncTask;
import android.util.Log;

public class RemoveTeamAsyncTask extends AsyncTask<PokemonTeamEntity, Void, Void> {

    private PokemonDAO pokemonDAO;

    public RemoveTeamAsyncTask(PokemonDAO dao){
        this.pokemonDAO = dao;
    }

    @Override
    protected Void doInBackground(PokemonTeamEntity... pokemonTeamEntities) {
        pokemonDAO.removePokemonById(pokemonTeamEntities[0].getId());
        Log.d("RemoveAsyncTask", "Pokemon removed");
        return null;
    }
}
