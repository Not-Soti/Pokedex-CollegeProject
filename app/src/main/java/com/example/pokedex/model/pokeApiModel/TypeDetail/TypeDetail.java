
package com.example.pokedex.model.pokeApiModel.TypeDetail;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Conjunto de clases usadas cuando se descargan pokemon en funcion de su tipo
 */

@Generated("jsonschema2pojo")
public class TypeDetail {

    @SerializedName("damage_relations")
    @Expose
    private TypeDetail_DamageRelations typeDetailDamageRelations;
    @SerializedName("game_indices")
    @Expose
    private List<TypeDetail_GameIndex> gameIndices = null;
    @SerializedName("generation")
    @Expose
    private TypeDetail_Generation__1 generation;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("move_damage_class")
    @Expose
    private TypeDetail_MoveDamageClass typeDetailMoveDamageClass;
    @SerializedName("moves")
    @Expose
    private List<TypeDetail_Move> typeDetailMoves = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("names")
    @Expose
    private List<TypeDetail_Name> typeDetailNames = null;
    @SerializedName("pokemon")
    @Expose
    private List<TypeDetail_Pokemon> pokemon = null;

    public TypeDetail_DamageRelations getTypeDetailDamageRelations() {
        return typeDetailDamageRelations;
    }

    public void setTypeDetailDamageRelations(TypeDetail_DamageRelations typeDetailDamageRelations) {
        this.typeDetailDamageRelations = typeDetailDamageRelations;
    }

    public List<TypeDetail_GameIndex> getGameIndices() {
        return gameIndices;
    }

    public void setGameIndices(List<TypeDetail_GameIndex> gameIndices) {
        this.gameIndices = gameIndices;
    }

    public TypeDetail_Generation__1 getGeneration() {
        return generation;
    }

    public void setGeneration(TypeDetail_Generation__1 generation) {
        this.generation = generation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TypeDetail_MoveDamageClass getTypeDetailMoveDamageClass() {
        return typeDetailMoveDamageClass;
    }

    public void setTypeDetailMoveDamageClass(TypeDetail_MoveDamageClass typeDetailMoveDamageClass) {
        this.typeDetailMoveDamageClass = typeDetailMoveDamageClass;
    }

    public List<TypeDetail_Move> getTypeDetailMoves() {
        return typeDetailMoves;
    }

    public void setTypeDetailMoves(List<TypeDetail_Move> typeDetailMoves) {
        this.typeDetailMoves = typeDetailMoves;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypeDetail_Name> getTypeDetailNames() {
        return typeDetailNames;
    }

    public void setTypeDetailNames(List<TypeDetail_Name> typeDetailNames) {
        this.typeDetailNames = typeDetailNames;
    }

    public List<TypeDetail_Pokemon> getPokemon() {
        return pokemon;
    }

    public void setPokemon(List<TypeDetail_Pokemon> pokemon) {
        this.pokemon = pokemon;
    }

}
