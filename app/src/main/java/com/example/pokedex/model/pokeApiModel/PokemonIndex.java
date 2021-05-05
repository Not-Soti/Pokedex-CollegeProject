
package com.example.pokedex.model.pokeApiModel;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase que contiene el resultado de una consulta a la API sin parámetros.
 * Devuelve una lista de objetos que contienen:
 *      1) Nombre del pokemon
 *      2) URL de la que obtener información de dicho pokemon
 */
@Generated("jsonschema2pojo")
public class PokemonIndex {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("previous")
    @Expose
    private Object previous;
    @SerializedName("results")
    @Expose
    private List<PokemonIndexItem> pokemonIndexItems = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public List<PokemonIndexItem> getPokemonIndexItems() {
        return pokemonIndexItems;
    }

    public void setPokemonIndexItems(List<PokemonIndexItem> pokemonIndexItems) {
        this.pokemonIndexItems = pokemonIndexItems;
    }

}
