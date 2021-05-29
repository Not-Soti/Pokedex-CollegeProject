
package com.example.pokedex.model.pokeApiModel.MoveDetail;

import java.util.List;
import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MoveDetail_EffectChange {

    @SerializedName("effect_entries")
    @Expose
    private List<MoveDetail_EffectEntry> effectEntries = null;
    @SerializedName("version_group")
    @Expose
    private MoveDetail_VersionGroup moveDetailVersionGroup;

    public List<MoveDetail_EffectEntry> getEffectEntries() {
        return effectEntries;
    }

    public void setEffectEntries(List<MoveDetail_EffectEntry> effectEntries) {
        this.effectEntries = effectEntries;
    }

    public MoveDetail_VersionGroup getMoveDetailVersionGroup() {
        return moveDetailVersionGroup;
    }

    public void setMoveDetailVersionGroup(MoveDetail_VersionGroup moveDetailVersionGroup) {
        this.moveDetailVersionGroup = moveDetailVersionGroup;
    }

}
