package com.example.pokedex.presentation.Team;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.pokedex.Persistence.PokemonTeamEntity;
import com.example.pokedex.Persistence.Repository;

import java.util.List;

public class TeamViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<PokemonTeamEntity>> pokemonTeam;

    public TeamViewModel(@NonNull Application application){
        super(application);

        repository = new Repository(application);
        pokemonTeam = repository.getPokemonTeam();
    }

    public LiveData<List<PokemonTeamEntity>> getPokemonTeam() {
        return pokemonTeam;
    }
}
