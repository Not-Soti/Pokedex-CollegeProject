
package com.example.pokedex.model.pokeApiModel.MoveDetail;

import java.util.List;
import javax.annotation.Generated;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MoveDetail {

    @SerializedName("accuracy")
    @Expose
    private Object accuracy;
    @SerializedName("contest_combos")
    @Expose
    private Object contestCombos;
    @SerializedName("contest_effect")
    @Expose
    private MoveDetail_ContestEffect moveDetailContestEffect;
    @SerializedName("contest_type")
    @Expose
    private MoveDetail_ContestType moveDetailContestType;
    @SerializedName("damage_class")
    @Expose
    private MoveDetail_DamageClass moveDetailDamageClass;
    @SerializedName("effect_chance")
    @Expose
    private Object effectChance;
    @SerializedName("effect_changes")
    @Expose
    private List<MoveDetail_EffectChange> moveDetailEffectChanges = null;
    @SerializedName("effect_entries")
    @Expose
    private List<MoveDetail_EffectEntry__1> effectEntries = null;
    @SerializedName("flavor_text_entries")
    @Expose
    private List<MoveDetail_FlavorTextEntry> flavorTextEntries = null;
    @SerializedName("generation")
    @Expose
    private MoveDetail_Generation moveDetailGeneration;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("learned_by_pokemon")
    @Expose
    private List<MoveDetail_LearnedByPokemon> moveDetailLearnedByPokemon = null;
    @SerializedName("machines")
    @Expose
    private List<MoveDetail_Machine> moveDetailMachines = null;
    @SerializedName("meta")
    @Expose
    private MoveDetail_Meta moveDetailMeta;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("names")
    @Expose
    private List<MoveDetail_Name> moveDetailNames = null;
    @SerializedName("past_values")
    @Expose
    private List<Object> pastValues = null;
    @SerializedName("power")
    @Expose
    private Integer power;
    @SerializedName("pp")
    @Expose
    private Integer pp;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("stat_changes")
    @Expose
    private List<Object> statChanges = null;
    @SerializedName("super_contest_effect")
    @Expose
    private MoveDetail_SuperContestEffect moveDetailSuperContestEffect;
    @SerializedName("target")
    @Expose
    private MoveDetail_Target moveDetailTarget;
    @SerializedName("type")
    @Expose
    private MoveDetail_Type type;

    public Object getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Object accuracy) {
        this.accuracy = accuracy;
    }

    public Object getContestCombos() {
        return contestCombos;
    }

    public void setContestCombos(Object contestCombos) {
        this.contestCombos = contestCombos;
    }

    public MoveDetail_ContestEffect getMoveDetailContestEffect() {
        return moveDetailContestEffect;
    }

    public void setMoveDetailContestEffect(MoveDetail_ContestEffect moveDetailContestEffect) {
        this.moveDetailContestEffect = moveDetailContestEffect;
    }

    public MoveDetail_ContestType getMoveDetailContestType() {
        return moveDetailContestType;
    }

    public void setMoveDetailContestType(MoveDetail_ContestType moveDetailContestType) {
        this.moveDetailContestType = moveDetailContestType;
    }

    public MoveDetail_DamageClass getMoveDetailDamageClass() {
        return moveDetailDamageClass;
    }

    public void setMoveDetailDamageClass(MoveDetail_DamageClass moveDetailDamageClass) {
        this.moveDetailDamageClass = moveDetailDamageClass;
    }

    public Object getEffectChance() {
        return effectChance;
    }

    public void setEffectChance(Object effectChance) {
        this.effectChance = effectChance;
    }

    public List<MoveDetail_EffectChange> getMoveDetailEffectChanges() {
        return moveDetailEffectChanges;
    }

    public void setMoveDetailEffectChanges(List<MoveDetail_EffectChange> moveDetailEffectChanges) {
        this.moveDetailEffectChanges = moveDetailEffectChanges;
    }

    public List<MoveDetail_EffectEntry__1> getEffectEntries() {
        return effectEntries;
    }

    public void setEffectEntries(List<MoveDetail_EffectEntry__1> effectEntries) {
        this.effectEntries = effectEntries;
    }

    public List<MoveDetail_FlavorTextEntry> getFlavorTextEntries() {
        return flavorTextEntries;
    }

    public void setFlavorTextEntries(List<MoveDetail_FlavorTextEntry> flavorTextEntries) {
        this.flavorTextEntries = flavorTextEntries;
    }

    public MoveDetail_Generation getMoveDetailGeneration() {
        return moveDetailGeneration;
    }

    public void setMoveDetailGeneration(MoveDetail_Generation moveDetailGeneration) {
        this.moveDetailGeneration = moveDetailGeneration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MoveDetail_LearnedByPokemon> getMoveDetailLearnedByPokemon() {
        return moveDetailLearnedByPokemon;
    }

    public void setMoveDetailLearnedByPokemon(List<MoveDetail_LearnedByPokemon> moveDetailLearnedByPokemon) {
        this.moveDetailLearnedByPokemon = moveDetailLearnedByPokemon;
    }

    public List<MoveDetail_Machine> getMoveDetailMachines() {
        return moveDetailMachines;
    }

    public void setMoveDetailMachines(List<MoveDetail_Machine> moveDetailMachines) {
        this.moveDetailMachines = moveDetailMachines;
    }

    public MoveDetail_Meta getMoveDetailMeta() {
        return moveDetailMeta;
    }

    public void setMoveDetailMeta(MoveDetail_Meta moveDetailMeta) {
        this.moveDetailMeta = moveDetailMeta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MoveDetail_Name> getMoveDetailNames() {
        return moveDetailNames;
    }

    public void setMoveDetailNames(List<MoveDetail_Name> moveDetailNames) {
        this.moveDetailNames = moveDetailNames;
    }

    public List<Object> getPastValues() {
        return pastValues;
    }

    public void setPastValues(List<Object> pastValues) {
        this.pastValues = pastValues;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getPp() {
        return pp;
    }

    public void setPp(Integer pp) {
        this.pp = pp;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<Object> getStatChanges() {
        return statChanges;
    }

    public void setStatChanges(List<Object> statChanges) {
        this.statChanges = statChanges;
    }

    public MoveDetail_SuperContestEffect getMoveDetailSuperContestEffect() {
        return moveDetailSuperContestEffect;
    }

    public void setMoveDetailSuperContestEffect(MoveDetail_SuperContestEffect moveDetailSuperContestEffect) {
        this.moveDetailSuperContestEffect = moveDetailSuperContestEffect;
    }

    public MoveDetail_Target getMoveDetailTarget() {
        return moveDetailTarget;
    }

    public void setMoveDetailTarget(MoveDetail_Target moveDetailTarget) {
        this.moveDetailTarget = moveDetailTarget;
    }

    public MoveDetail_Type getType() {
        return type;
    }

    public void setType(MoveDetail_Type type) {
        this.type = type;
    }

}
