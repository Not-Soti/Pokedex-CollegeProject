
package com.example.pokedex.model.pokeApiModel.MoveDetail;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MoveDetail_FlavorTextEntry {

    @SerializedName("flavor_text")
    @Expose
    private String flavorText;
    @SerializedName("language")
    @Expose
    private MoveDetail_Language__2 language;
    @SerializedName("version_group")
    @Expose
    private MoveDetail_VersionGroup__1 versionGroup;

    public String getFlavorText() {
        return flavorText;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
    }

    public MoveDetail_Language__2 getLanguage() {
        return language;
    }

    public void setLanguage(MoveDetail_Language__2 language) {
        this.language = language;
    }

    public MoveDetail_VersionGroup__1 getVersionGroup() {
        return versionGroup;
    }

    public void setVersionGroup(MoveDetail_VersionGroup__1 versionGroup) {
        this.versionGroup = versionGroup;
    }

}
