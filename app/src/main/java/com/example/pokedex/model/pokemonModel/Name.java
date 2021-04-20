
package com.example.pokedex.model.pokemonModel;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Name {

    @SerializedName("language")
    @Expose
    private Language__2 language;
    @SerializedName("name")
    @Expose
    private String name;

    public Language__2 getLanguage() {
        return language;
    }

    public void setLanguage(Language__2 language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
