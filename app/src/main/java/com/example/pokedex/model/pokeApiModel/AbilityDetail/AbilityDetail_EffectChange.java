
package com.example.pokedex.model.pokeApiModel.AbilityDetail;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AbilityDetail_EffectChange {

    @SerializedName("effect_entries")
    @Expose
    private List<AbilityDetail_EffectEntry> effectEntries = null;
    @SerializedName("version_group")
    @Expose
    private VersionGroup versionGroup;

    public List<AbilityDetail_EffectEntry> getEffectEntries() {
        return effectEntries;
    }

    public void setEffectEntries(List<AbilityDetail_EffectEntry> effectEntries) {
        this.effectEntries = effectEntries;
    }

    public VersionGroup getVersionGroup() {
        return versionGroup;
    }

    public void setVersionGroup(VersionGroup versionGroup) {
        this.versionGroup = versionGroup;
    }

}
