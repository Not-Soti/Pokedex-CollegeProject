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
    void insertPokemon(PokemonTeamEntity pokemon);

    @Delete
    void deletePokemon(PokemonTeamEntity pokemon);

    @Query("SELECT * FROM pokemon_table")
    LiveData<List<PokemonTeamEntity>> getFullTeam();

    @Query("SELECT * FROM pokemon_table WHERE id = :id")
    LiveData<PokemonTeamEntity> getPokemonById(int id);

    @Query("DELETE FROM pokemon_table")
    void deleteAll();

    @Query("DELETE FROM pokemon_table WHERE id = :id")
    void removePokemonById(int id);

    @Query("UPDATE pokemon_table SET " +
            "nombre = :name," +
            "mov1 = :mov1," +
            "mov2 = :mov2," +
            "mov3 = :mov3," +
            "mov4 = :mov4 " +
            "WHERE id = :id")
    void updatePokemon(int id, String name, String mov1, String mov2, String mov3, String mov4);
}
