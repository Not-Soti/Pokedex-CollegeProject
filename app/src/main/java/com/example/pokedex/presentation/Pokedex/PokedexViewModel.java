package com.example.pokedex.presentation.Pokedex;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pokedex.Persistence.PokemonTeamEntity;
import com.example.pokedex.Persistence.Repository;
import com.example.pokedex.model.pokeApiModel.AbilityDetail.AbilityDetail;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail;
import com.example.pokedex.model.pokeApiModel.Pokemon;

import java.util.HashSet;
import java.util.List;

public class PokedexViewModel extends AndroidViewModel {

    private String TAG = "--PokedexViewModel--";

    private final ObservableArrayList<Pokemon> pokemonList;
    private final ObservableArrayList<Pokemon> favsList;
    private ObservableArrayList<MoveDetail> moveList;
    private ObservableArrayList<AbilityDetail> abilityList;

    private LiveData<List<PokemonTeamEntity>> pokemonTeam;

    private HashSet<Integer> pokemonIDsInTeam;
    private HashSet<Integer> favPokemonIDs;

    private final Repository repository;

    public PokedexViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);

        pokemonList = new ObservableArrayList<>();
        favsList = new ObservableArrayList<>();
        favPokemonIDs = repository.getFavouritePokemon();
        pokemonTeam = repository.getPokemonTeam();

        moveList = new ObservableArrayList<>();
        abilityList = new ObservableArrayList<>();
    }

    /**
     * Metodo que devuelve los IDs de los pokemon que forman el equipo
     * @return Set con los IDs de los pokemon pertenecientes al equipo
     */
    public void updateIDsInTeam(){
        HashSet<Integer> idsInTeam = new HashSet<>();
        for(PokemonTeamEntity p : pokemonTeam.getValue()){
            idsInTeam.add(p.getId());
        }
        pokemonIDsInTeam = idsInTeam;
    }

    private void updateFavSet(){
        favPokemonIDs = repository.getFavouritePokemon();
    }

    public void updatePokemonList(int count){
        repository.downloadPokemonAll(count, pokemonList);
    }

    public void updatePokemonListByType(int count, String type){
        repository.downloadPokemonByType(count, pokemonList, type);
    }

    public void updatePokemonListByAbility(int count, int abilityID){
        Log.d(TAG, "Ability ID = "+abilityID);
        repository.downloadPokemonByAbility(count, pokemonList, abilityID);
    }

    public void updatePokemonListByMove(int count, int moveID){
        Log.d(TAG, "Move ID = "+moveID);
        repository.downloadPokemonByMove(count, pokemonList, moveID);
    }

    public void updateFavList(){
        repository.downloadPokemonFavourites(favPokemonIDs, favsList);
    }


    public void addPokemonToFavs(Pokemon pokemon){
        pokemon.isFav=true;

        synchronized (favsList){
            if(!favsList.contains(pokemon))
                favsList.add(pokemon);
        }

        //Tambien se marca que es favorito en la lista general
        synchronized (pokemonList){
            for (Pokemon poke : pokemonList){
                if (poke.getId().equals(pokemon.getId())){
                    poke.isFav = true;
                }
            }
        }

        repository.addFavPokemon(pokemon);
        updateFavSet();
    }

    public void removePokemonFromFavs(Pokemon pokemon){
        pokemon.isFav = false;

        synchronized (favsList){
            favsList.remove(pokemon);
            Log.d(TAG, "Eliminado de favs list");
        }

        //Tambien se marca que no es favorito en la lista general
        synchronized (pokemonList){
            for (Pokemon poke : pokemonList){
                //Log.d(TAG, "poke.id: "+poke.getId());
                if (poke.getId().equals(pokemon.getId())){
                    poke.isFav = false;
                }
            }
        }
        repository.removeFavPokemon(pokemon);
        updateFavSet();
    }

    public ObservableArrayList<Pokemon> getPokemonList() {
        return pokemonList;
    }
    public ObservableArrayList<Pokemon> getFavsList() {
        return favsList;
    }
    public HashSet<Integer> getFavPokemonIDs() {
        return favPokemonIDs;
    }
    public HashSet<Integer> getPokemonIDsInTeam() {
        return pokemonIDsInTeam;
    }
    public LiveData<List<PokemonTeamEntity>> getPokemonTeam() {
        return pokemonTeam;
    }


    public void addPokemonToTeam(Pokemon pokemon) {
        pokemon.isInTeam=true;

        PokemonTeamEntity pokemonDAO = new PokemonTeamEntity();
        pokemonDAO.setId(pokemon.getId());
        pokemonDAO.setSpecie(pokemon.getName()); //Aqui el nombre y la especie es el mismo
        pokemonDAO.setNombre(pokemon.getName()); //En el equipo se puede poner un nombre propio
        repository.insertPokemonIntoTeam(pokemonDAO);
    }

    public void removeFromTeam(Pokemon pokemon) {
        pokemon.isInTeam=false;
        PokemonTeamEntity pokemonDAO = new PokemonTeamEntity();
        pokemonDAO.setId(pokemon.getId());
        repository.removePokemonFromTeam(pokemonDAO);
    }

    public void clearPokemonList(){
        pokemonList.clear();
    }

    public void clearFavsList(){
        favsList.clear();
    }

    /**Metodo usado para descargar los movimientos
     *
     * @param moveCount id del movimiento con id mas alto
     */
    public void updateMoves(int moveCount) {
        repository.downloadMoves(moveList, moveCount);
    }

    public void updateAbilities(int abilityCount){
        repository.downloadAbilities(abilityList, abilityCount);
    }

    public ObservableArrayList<MoveDetail> getMoveList(){
        return moveList;
    }
    public ObservableArrayList<AbilityDetail> getAbilityList(){
        return abilityList;
    }
}
