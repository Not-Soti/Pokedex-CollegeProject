package com.example.pokedex.netAccess;

import com.example.pokedex.model.pokeApiModel.AbilityDetail.AbilityDetail;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail;
import com.example.pokedex.model.pokeApiModel.PokemonIndex;
import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.example.pokedex.model.pokeApiModel.PokemonSpeciesDetail;
import com.example.pokedex.model.pokeApiModel.TypeDetail.TypeDetail;

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
    Call<PokemonIndex> getPokemonList(@Query("limit") int count);

    /**
     * Metodo que obtiene el JSON con la descripcion textual de cada pokemon
     */
    @GET("pokemon-species/{specie}")
    Call<PokemonSpeciesDetail> getPokemonSpeciesDetail(@Path("specie") int specieNumber);

    /**
     * Metodo que obtiene el JSON con los detalles del tipo pokemon seleccionado
     * @param type: tipo pokemon del que buscar el detalle (Planta, Fuego, Agua...)
     * @return: Objeto con los detalles del Tipo pokemon seleccionado
     */
    @GET("type/{type_string}")
    Call<TypeDetail> getPokemonTypeDetail(@Path("type_string") String type);


    /**
     * Metodo que obtiene el JSON con los detalles del movimiento pokemon seleccionado
     * @param move_id: id del movimiento del que buscar el detalle
     * @return: Objeto con los detalles del movimineto pokemon seleccionado
     */
    @GET("move/{move_id}")
    Call<MoveDetail> getMoveDetailByIds(@Path("move_id") int move_id);

    /**
     * Metodo que obtiene el JSON con los detalles del movimiento pokemon seleccionado
     * @param ability_id: id de la habilidad de la que buscar el detalle
     * @return: Objeto con los detalles de la habilidad pokemon seleccionada
     */
    @GET("ability/{ability_id}")
    Call<AbilityDetail> getAbilityDetailById(@Path("ability_id") int ability_id);
}
