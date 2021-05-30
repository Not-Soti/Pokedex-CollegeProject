
package com.example.pokedex.model.pokeApiModel.AbilityDetail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AbilityDetail_EffectEntry {

    @SerializedName("effect")
    @Expose
    private String effect;
    @SerializedName("language")
    @Expose
    private AbilityDetail_Language abilityDetailLanguage;

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public AbilityDetail_Language getAbilityDetailLanguage() {
        return abilityDetailLanguage;
    }

    public void setAbilityDetailLanguage(AbilityDetail_Language abilityDetailLanguage) {
        this.abilityDetailLanguage = abilityDetailLanguage;
    }

}
