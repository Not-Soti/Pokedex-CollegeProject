
package com.example.pokedex.model.pokeApiModel.TypeDetail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class TypeDetail_GameIndex {

    @SerializedName("game_index")
    @Expose
    private Integer gameIndex;
    @SerializedName("generation")
    @Expose
    private TypeDetail_Generation generation;

    public Integer getGameIndex() {
        return gameIndex;
    }

    public void setGameIndex(Integer gameIndex) {
        this.gameIndex = gameIndex;
    }

    public TypeDetail_Generation getGeneration() {
        return generation;
    }

    public void setGeneration(TypeDetail_Generation generation) {
        this.generation = generation;
    }

}
