
package com.example.pokedex.model.pokeApiModel.MoveDetail;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MoveDetail_Machine {

    @SerializedName("machine")
    @Expose
    private MoveDetail_Machine__1 machine;
    @SerializedName("version_group")
    @Expose
    private MoveDetail_VersionGroup__2 versionGroup;

    public MoveDetail_Machine__1 getMachine() {
        return machine;
    }

    public void setMachine(MoveDetail_Machine__1 machine) {
        this.machine = machine;
    }

    public MoveDetail_VersionGroup__2 getVersionGroup() {
        return versionGroup;
    }

    public void setVersionGroup(MoveDetail_VersionGroup__2 versionGroup) {
        this.versionGroup = versionGroup;
    }

}
