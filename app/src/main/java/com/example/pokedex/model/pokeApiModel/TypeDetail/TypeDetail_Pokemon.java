
package com.example.pokedex.model.pokeApiModel.TypeDetail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class TypeDetail_Pokemon {

    @SerializedName("pokemon")
    @Expose
    private TypeDetail_Pokemon__1 pokemon;
    @SerializedName("slot")
    @Expose
    private Integer slot;

    public TypeDetail_Pokemon__1 getPokemon() {
        return pokemon;
    }

    public void setPokemon(TypeDetail_Pokemon__1 pokemon) {
        this.pokemon = pokemon;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

}
