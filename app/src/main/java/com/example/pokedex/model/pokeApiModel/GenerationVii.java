
package com.example.pokedex.model.pokeApiModel;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class GenerationVii {

    @SerializedName("icons")
    @Expose
    private Icons icons;
    @SerializedName("ultra-sun-ultra-moon")
    @Expose
    private UltraSunUltraMoon ultraSunUltraMoon;

    public Icons getIcons() {
        return icons;
    }

    public void setIcons(Icons icons) {
        this.icons = icons;
    }

    public UltraSunUltraMoon getUltraSunUltraMoon() {
        return ultraSunUltraMoon;
    }

    public void setUltraSunUltraMoon(UltraSunUltraMoon ultraSunUltraMoon) {
        this.ultraSunUltraMoon = ultraSunUltraMoon;
    }

}
