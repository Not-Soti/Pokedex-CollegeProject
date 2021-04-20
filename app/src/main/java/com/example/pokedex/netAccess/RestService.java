package com.example.pokedex.netAccess;

import com.example.pokedex.model.pokemonModel.PokemonListInfo;
import com.example.pokedex.model.pokemonModel.Pokemon;
import com.example.pokedex.model.pokemonModel.PokemonSpeciesDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestService {

    /**
     * Metodo que obtiene el JSON asociado
     * a un pokemon, y sus caracteristicas
     * @param pokedexNumber: ID del pokemon a buscar
     * @return Objeto de la clase Pokemon
     */
    @GET("pokemon/{id}")
    Call<Pokemon> getPokemonById(@Path("id") int pokedexNumber);

    /**
     * Metodo que obtiene una lista conteniendo los nombres
     * y URLs de la informacion de la cantidad indicada de Pokemon
     * @param count: Cantidad de pokemon de los que recibir informacion
     * @return Objeto PokemonListInfo del que obtener la URL de
     * la información de cada Pokemon
     */
    @GET("pokemon/")
    Call<PokemonListInfo> getPokemonList(@Query("limit") int count);

    /**
     * Metodo que obtiene el JSON con la descripcion textual de cada pokemon
     */
    @GET("pokemon-species/{id}")
    Call<PokemonSpeciesDetail> getPokemonSpeciesDetail(@Path("id") int pokedexNumber);
}
