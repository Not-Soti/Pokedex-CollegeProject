
package com.example.pokedex.model.pokeApiModel.AbilityDetail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AbilityDetail_Pokemon {

    @SerializedName("is_hidden")
    @Expose
    private Boolean isHidden;
    @SerializedName("pokemon")
    @Expose
    private AbilityDetail_Pokemon__1 pokemon;
    @SerializedName("slot")
    @Expose
    private Integer slot;

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public AbilityDetail_Pokemon__1 getPokemon() {
        return pokemon;
    }

    public void setPokemon(AbilityDetail_Pokemon__1 pokemon) {
        this.pokemon = pokemon;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

}
