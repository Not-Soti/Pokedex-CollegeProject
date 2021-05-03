package com.example.pokedex.Persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pokemon_table")
public class PokemonTeamEntity {

    @PrimaryKey
    @NonNull
    private int id;
    private String nombre;
    private String mov1, mov2, mov3, mov4;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMov1() {
        return mov1;
    }

    public void setMov1(String mov1) {
        this.mov1 = mov1;
    }

    public String getMov2() {
        return mov2;
    }

    public void setMov2(String mov2) {
        this.mov2 = mov2;
    }

    public String getMov3() {
        return mov3;
    }

    public void setMov3(String mov3) {
        this.mov3 = mov3;
    }

    public String getMov4() {
        return mov4;
    }

    public void setMov4(String mov4) {
        this.mov4 = mov4;
    }
}
