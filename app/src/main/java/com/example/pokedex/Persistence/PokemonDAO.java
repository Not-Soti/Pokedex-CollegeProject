package com.example.pokedex.Persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pokedex.Persistence.PokemonTeamEntity;

import java.util.List;

@Dao
public interface PokemonDAO {

    @Insert
    public void insertPokemon(PokemonTeamEntity pokemon);

    @Delete
    public void deletePokemon(PokemonTeamEntity pokemon);

    @Query("SELECT * FROM pokemon_table")
    public LiveData<List<PokemonTeamEntity>> getFullTeam();

    @Query("SELECT * FROM pokemon_table WHERE id = :id")
    public LiveData<PokemonTeamEntity> getPokemonById(int id);

    @Query("DELETE FROM pokemon_table")
    public void deleteAll();

    @Query("DELETE FROM pokemon_table WHERE id = :id")
    public void removePokemonById(int id);
}
