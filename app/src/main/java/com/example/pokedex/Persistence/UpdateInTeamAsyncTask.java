package com.example.pokedex.Persistence;

import android.os.AsyncTask;
import android.util.Log;

public class UpdateInTeamAsyncTask extends AsyncTask<PokemonTeamEntity, Void, Void> {

    private PokemonDAO pokemonDAO;

    public UpdateInTeamAsyncTask(PokemonDAO dao){
        this.pokemonDAO = dao;
    }

    @Override
    protected Void doInBackground(PokemonTeamEntity... pokemonTeamEntities) {
        PokemonTeamEntity poke = pokemonTeamEntities[0];
        int id = poke.getId();
        String name = poke.getNombre();
        String specie = poke.getSpecie();
        String mov1 = poke.getMov1();
        String mov2 = poke.getMov2();
        String mov3 = poke.getMov3();
        String mov4 = poke.getMov4();
        pokemonDAO.updatePokemon(id, specie, name, mov1, mov2, mov3, mov4);
        Log.d("UpdateAsyncTask", "Pokemon updated");
        return null;
    }
}
