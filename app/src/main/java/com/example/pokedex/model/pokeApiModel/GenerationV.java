
package com.example.pokedex.model.pokeApiModel;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class GenerationV {

    @SerializedName("black-white")
    @Expose
    private BlackWhite blackWhite;

    public BlackWhite getBlackWhite() {
        return blackWhite;
    }

    public void setBlackWhite(BlackWhite blackWhite) {
        this.blackWhite = blackWhite;
    }

}
