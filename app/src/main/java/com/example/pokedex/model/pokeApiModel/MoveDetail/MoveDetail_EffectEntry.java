
package com.example.pokedex.model.pokeApiModel.MoveDetail;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MoveDetail_EffectEntry {

    @SerializedName("effect")
    @Expose
    private String effect;
    @SerializedName("language")
    @Expose
    private MoveDetail_Language moveDetailLanguage;

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public MoveDetail_Language getMoveDetailLanguage() {
        return moveDetailLanguage;
    }

    public void setMoveDetailLanguage(MoveDetail_Language moveDetailLanguage) {
        this.moveDetailLanguage = moveDetailLanguage;
    }

}
