
package com.example.pokedex.model.pokeApiModel.AbilityDetail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AbilityDetail_FlavorTextEntry {

    @SerializedName("flavor_text")
    @Expose
    private String flavorText;
    @SerializedName("language")
    @Expose
    private AbilityDetail_Language__2 language;
    @SerializedName("version_group")
    @Expose
    private VersionGroup__1 versionGroup;

    public String getFlavorText() {
        return flavorText;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
    }

    public AbilityDetail_Language__2 getLanguage() {
        return language;
    }

    public void setLanguage(AbilityDetail_Language__2 language) {
        this.language = language;
    }

    public VersionGroup__1 getVersionGroup() {
        return versionGroup;
    }

    public void setVersionGroup(VersionGroup__1 versionGroup) {
        this.versionGroup = versionGroup;
    }

}
