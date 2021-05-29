
package com.example.pokedex.model.pokeApiModel.MoveDetail;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MoveDetail_EffectEntry__1 {

    @SerializedName("effect")
    @Expose
    private String effect;
    @SerializedName("language")
    @Expose
    private MoveDetail_Language__1 language;
    @SerializedName("short_effect")
    @Expose
    private String shortEffect;

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public MoveDetail_Language__1 getLanguage() {
        return language;
    }

    public void setLanguage(MoveDetail_Language__1 language) {
        this.language = language;
    }

    public String getShortEffect() {
        return shortEffect;
    }

    public void setShortEffect(String shortEffect) {
        this.shortEffect = shortEffect;
    }

}
