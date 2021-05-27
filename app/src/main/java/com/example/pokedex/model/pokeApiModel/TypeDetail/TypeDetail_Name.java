
package com.example.pokedex.model.pokeApiModel.TypeDetail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class TypeDetail_Name {

    @SerializedName("language")
    @Expose
    private TypeDetail_Language typeDetailLanguage;
    @SerializedName("name")
    @Expose
    private String name;

    public TypeDetail_Language getTypeDetailLanguage() {
        return typeDetailLanguage;
    }

    public void setTypeDetailLanguage(TypeDetail_Language typeDetailLanguage) {
        this.typeDetailLanguage = typeDetailLanguage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
