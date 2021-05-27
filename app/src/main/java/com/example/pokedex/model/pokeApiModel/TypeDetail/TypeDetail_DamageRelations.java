
package com.example.pokedex.model.pokeApiModel.TypeDetail;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class TypeDetail_DamageRelations {

    @SerializedName("double_damage_from")
    @Expose
    private List<TypeDetail_DoubleDamageFrom> typeDetailDoubleDamageFrom = null;
    @SerializedName("double_damage_to")
    @Expose
    private List<TypeDetail_DoubleDamageTo> typeDetailDoubleDamageTo = null;
    @SerializedName("half_damage_from")
    @Expose
    private List<TypeDetail_HalfDamageFrom> typeDetailHalfDamageFrom = null;
    @SerializedName("half_damage_to")
    @Expose
    private List<TypeDetail_HalfDamageTo> typeDetailHalfDamageTo = null;
    @SerializedName("no_damage_from")
    @Expose
    private List<TypeDetail_NoDamageFrom> typeDetailNoDamageFrom = null;
    @SerializedName("no_damage_to")
    @Expose
    private List<Object> noDamageTo = null;

    public List<TypeDetail_DoubleDamageFrom> getTypeDetailDoubleDamageFrom() {
        return typeDetailDoubleDamageFrom;
    }

    public void setTypeDetailDoubleDamageFrom(List<TypeDetail_DoubleDamageFrom> typeDetailDoubleDamageFrom) {
        this.typeDetailDoubleDamageFrom = typeDetailDoubleDamageFrom;
    }

    public List<TypeDetail_DoubleDamageTo> getTypeDetailDoubleDamageTo() {
        return typeDetailDoubleDamageTo;
    }

    public void setTypeDetailDoubleDamageTo(List<TypeDetail_DoubleDamageTo> typeDetailDoubleDamageTo) {
        this.typeDetailDoubleDamageTo = typeDetailDoubleDamageTo;
    }

    public List<TypeDetail_HalfDamageFrom> getTypeDetailHalfDamageFrom() {
        return typeDetailHalfDamageFrom;
    }

    public void setTypeDetailHalfDamageFrom(List<TypeDetail_HalfDamageFrom> typeDetailHalfDamageFrom) {
        this.typeDetailHalfDamageFrom = typeDetailHalfDamageFrom;
    }

    public List<TypeDetail_HalfDamageTo> getTypeDetailHalfDamageTo() {
        return typeDetailHalfDamageTo;
    }

    public void setTypeDetailHalfDamageTo(List<TypeDetail_HalfDamageTo> typeDetailHalfDamageTo) {
        this.typeDetailHalfDamageTo = typeDetailHalfDamageTo;
    }

    public List<TypeDetail_NoDamageFrom> getTypeDetailNoDamageFrom() {
        return typeDetailNoDamageFrom;
    }

    public void setTypeDetailNoDamageFrom(List<TypeDetail_NoDamageFrom> typeDetailNoDamageFrom) {
        this.typeDetailNoDamageFrom = typeDetailNoDamageFrom;
    }

    public List<Object> getNoDamageTo() {
        return noDamageTo;
    }

    public void setNoDamageTo(List<Object> noDamageTo) {
        this.noDamageTo = noDamageTo;
    }

}
