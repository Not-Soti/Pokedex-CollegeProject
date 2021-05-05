
package com.example.pokedex.model.pokeApiModel;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Genera {

    @SerializedName("genus")
    @Expose
    private String genus;
    @SerializedName("language")
    @Expose
    private Language__1 language;

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public Language__1 getLanguage() {
        return language;
    }

    public void setLanguage(Language__1 language) {
        this.language = language;
    }

}
