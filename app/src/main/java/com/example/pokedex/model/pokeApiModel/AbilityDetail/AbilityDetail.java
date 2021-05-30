
package com.example.pokedex.model.pokeApiModel.AbilityDetail;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AbilityDetail {

    @SerializedName("effect_changes")
    @Expose
    private List<AbilityDetail_EffectChange> abilityDetailEffectChanges = null;
    @SerializedName("effect_entries")
    @Expose
    private List<AbilityDetail_EffectEntry__1> effectEntries = null;
    @SerializedName("flavor_text_entries")
    @Expose
    private List<AbilityDetail_FlavorTextEntry> flavorTextEntries = null;
    @SerializedName("generation")
    @Expose
    private AbilityDetail_Generation abilityDetailGeneration;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_main_series")
    @Expose
    private Boolean isMainSeries;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("names")
    @Expose
    private List<AbilityDetail_Name> abilityDetailNames = null;
    @SerializedName("pokemon")
    @Expose
    private List<AbilityDetail_Pokemon> pokemon = null;

    public List<AbilityDetail_EffectChange> getAbilityDetailEffectChanges() {
        return abilityDetailEffectChanges;
    }

    public void setAbilityDetailEffectChanges(List<AbilityDetail_EffectChange> abilityDetailEffectChanges) {
        this.abilityDetailEffectChanges = abilityDetailEffectChanges;
    }

    public List<AbilityDetail_EffectEntry__1> getEffectEntries() {
        return effectEntries;
    }

    public void setEffectEntries(List<AbilityDetail_EffectEntry__1> effectEntries) {
        this.effectEntries = effectEntries;
    }

    public List<AbilityDetail_FlavorTextEntry> getFlavorTextEntries() {
        return flavorTextEntries;
    }

    public void setFlavorTextEntries(List<AbilityDetail_FlavorTextEntry> flavorTextEntries) {
        this.flavorTextEntries = flavorTextEntries;
    }

    public AbilityDetail_Generation getAbilityDetailGeneration() {
        return abilityDetailGeneration;
    }

    public void setAbilityDetailGeneration(AbilityDetail_Generation abilityDetailGeneration) {
        this.abilityDetailGeneration = abilityDetailGeneration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsMainSeries() {
        return isMainSeries;
    }

    public void setIsMainSeries(Boolean isMainSeries) {
        this.isMainSeries = isMainSeries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AbilityDetail_Name> getAbilityDetailNames() {
        return abilityDetailNames;
    }

    public void setAbilityDetailNames(List<AbilityDetail_Name> abilityDetailNames) {
        this.abilityDetailNames = abilityDetailNames;
    }

    public List<AbilityDetail_Pokemon> getPokemon() {
        return pokemon;
    }

    public void setPokemon(List<AbilityDetail_Pokemon> pokemon) {
        this.pokemon = pokemon;
    }

}
