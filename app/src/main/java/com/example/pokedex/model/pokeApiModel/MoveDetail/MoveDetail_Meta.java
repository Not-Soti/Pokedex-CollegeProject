
package com.example.pokedex.model.pokeApiModel.MoveDetail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MoveDetail_Meta {

    @SerializedName("ailment")
    @Expose
    private MoveDetail_Ailment moveDetailAilment;
    @SerializedName("ailment_chance")
    @Expose
    private Integer ailmentChance;
    @SerializedName("category")
    @Expose
    private MoveDetail_Category category;
    @SerializedName("crit_rate")
    @Expose
    private Integer critRate;
    @SerializedName("drain")
    @Expose
    private Integer drain;
    @SerializedName("flinch_chance")
    @Expose
    private Integer flinchChance;
    @SerializedName("healing")
    @Expose
    private Integer healing;
    @SerializedName("max_hits")
    @Expose
    private Object maxHits;
    @SerializedName("max_turns")
    @Expose
    private Object maxTurns;
    @SerializedName("min_hits")
    @Expose
    private Object minHits;
    @SerializedName("min_turns")
    @Expose
    private Object minTurns;
    @SerializedName("stat_chance")
    @Expose
    private Integer statChance;

    public MoveDetail_Ailment getMoveDetailAilment() {
        return moveDetailAilment;
    }

    public void setMoveDetailAilment(MoveDetail_Ailment moveDetailAilment) {
        this.moveDetailAilment = moveDetailAilment;
    }

    public Integer getAilmentChance() {
        return ailmentChance;
    }

    public void setAilmentChance(Integer ailmentChance) {
        this.ailmentChance = ailmentChance;
    }

    public MoveDetail_Category getCategory() {
        return category;
    }

    public void setCategory(MoveDetail_Category category) {
        this.category = category;
    }

    public Integer getCritRate() {
        return critRate;
    }

    public void setCritRate(Integer critRate) {
        this.critRate = critRate;
    }

    public Integer getDrain() {
        return drain;
    }

    public void setDrain(Integer drain) {
        this.drain = drain;
    }

    public Integer getFlinchChance() {
        return flinchChance;
    }

    public void setFlinchChance(Integer flinchChance) {
        this.flinchChance = flinchChance;
    }

    public Integer getHealing() {
        return healing;
    }

    public void setHealing(Integer healing) {
        this.healing = healing;
    }

    public Object getMaxHits() {
        return maxHits;
    }

    public void setMaxHits(Object maxHits) {
        this.maxHits = maxHits;
    }

    public Object getMaxTurns() {
        return maxTurns;
    }

    public void setMaxTurns(Object maxTurns) {
        this.maxTurns = maxTurns;
    }

    public Object getMinHits() {
        return minHits;
    }

    public void setMinHits(Object minHits) {
        this.minHits = minHits;
    }

    public Object getMinTurns() {
        return minTurns;
    }

    public void setMinTurns(Object minTurns) {
        this.minTurns = minTurns;
    }

    public Integer getStatChance() {
        return statChance;
    }

    public void setStatChance(Integer statChance) {
        this.statChance = statChance;
    }

}
