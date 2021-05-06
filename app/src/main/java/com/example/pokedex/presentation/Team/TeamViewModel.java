package com.example.pokedex.presentation.Team;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.pokedex.Persistence.PokemonTeamEntity;
import com.example.pokedex.Persistence.Repository;
import com.example.pokedex.model.pokeApiModel.Pokemon;

import java.util.List;
import java.util.Objects;

public class TeamViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<PokemonTeamEntity>> pokemonTeam;
    private ObservableArrayList<Pokemon> pokemonList;

    public TeamViewModel(@NonNull Application application){
        super(application);

        repository = new Repository(application);
        pokemonTeam = repository.getPokemonTeam();
        pokemonList = new ObservableArrayList<>();
    }

    /**
     * Metodo utilizado para descarga la información de los pokemon favoritos
     */
    public void downloadPokemon(){
        for(PokemonTeamEntity poke : Objects.requireNonNull(pokemonTeam.getValue())) {
            repository.downloadPokemonFromId(poke.getId(), pokemonList);
        }
    }

    public LiveData<List<PokemonTeamEntity>> getPokemonTeam() {
        return pokemonTeam;
    }
    public ObservableArrayList<Pokemon> getPokemonList() {
        return pokemonList;
    }

    public void removeFromTeam(Pokemon pokemon) {
        PokemonTeamEntity pokemonDAO = new PokemonTeamEntity();
        pokemonDAO.setId(pokemon.getId());
        repository.removePokemonFromTeam(pokemonDAO);

        //Se busca el pokemon y se elimina de la lista
        for(int i=0; i<pokemonList.size(); i++){
            if(pokemonList.get(i).getId() == pokemon.getId()){
                pokemonList.remove(i);
            }
        }
    }

    public void updatePokemon(int id, String nombre, String[] moves) {
        PokemonTeamEntity pokemonDAO = new PokemonTeamEntity();
        pokemonDAO.setId(id);
        pokemonDAO.setNombre(nombre);
        pokemonDAO.setMov1(moves[0]);
        pokemonDAO.setMov2(moves[1]);
        pokemonDAO.setMov3(moves[2]);
        pokemonDAO.setMov4(moves[3]);
        
        repository.updatePokemonInTeam(pokemonDAO);
    }
}
