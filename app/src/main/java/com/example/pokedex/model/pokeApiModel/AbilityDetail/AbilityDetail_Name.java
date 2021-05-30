
package com.example.pokedex.model.pokeApiModel.AbilityDetail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AbilityDetail_Name {

    @SerializedName("language")
    @Expose
    private AbilityDetail_Language__3 language;
    @SerializedName("name")
    @Expose
    private String name;

    public AbilityDetail_Language__3 getLanguage() {
        return language;
    }

    public void setLanguage(AbilityDetail_Language__3 language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
