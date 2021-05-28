package com.example.pokedex.presentation.Pokedex;

import android.content.Context;

import com.example.pokedex.R;

import java.util.ArrayList;

/**
 * Clase hecha para TODO
 */
public class PokemonSearchHelper {

    //Lista de los tipos válidos
    private ArrayList<String> validTypes;
    private Context context;

    public PokemonSearchHelper(Context context){
        this.context = context;

        validTypes = new ArrayList<String>();
        validTypes.add(context.getString(R.string.type_steel));
        validTypes.add(context.getString(R.string.type_water));
        validTypes.add(context.getString(R.string.type_bug));
        validTypes.add(context.getString(R.string.type_dragon));
        validTypes.add(context.getString(R.string.type_electric));
        validTypes.add(context.getString(R.string.type_normal));
        validTypes.add(context.getString(R.string.type_fire));
        validTypes.add(context.getString(R.string.type_grass));
        validTypes.add(context.getString(R.string.type_ice));
        validTypes.add(context.getString(R.string.type_fighting));
        validTypes.add(context.getString(R.string.type_poison));
        validTypes.add(context.getString(R.string.type_ground));
        validTypes.add(context.getString(R.string.type_flying));
        validTypes.add(context.getString(R.string.type_psychic));
        validTypes.add(context.getString(R.string.type_rock));
        validTypes.add(context.getString(R.string.type_ghost));
        validTypes.add(context.getString(R.string.type_dark));
        validTypes.add(context.getString(R.string.type_fairy));
    }

    /**
     * Metodo que controla que el string que se pasa como parámetro
     * es un tipo pokemon válido
     * @param type: Tipo a validar
     */
    public boolean isValidType(String type){
        boolean isValid = false;

        for(String t : validTypes){
            if(t.toLowerCase().equals(type.toLowerCase())){
                isValid = true;
            }
        }

        return isValid;
    }

    public ArrayList<String> getValidTypes(){
        return validTypes;
    }

    public String getTranslatedType(String query) {
        if (query.equalsIgnoreCase(context.getString(R.string.type_steel))) {
            return "steel";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_water))) {
            return "water";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_bug))) {
            return "bug";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_dragon))) {
            return "dragon";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_electric))) {
            return "electric";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_normal))) {
            return "normal";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_fire))) {
            return "fire";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_grass))) {
            return "grass";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_fighting))) {
            return "fighting";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_ice))) {
            return "ice";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_poison))) {
            return "poison";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_ground))) {
            return "ground";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_flying))) {
            return "flying";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_psychic))) {
            return "psychic";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_rock))) {
            return "rock";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_ghost))) {
            return "ghost";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_dark))) {
            return "dark";
        }else if(query.equalsIgnoreCase(context.getString(R.string.type_fairy))) {
            return "fairy";
        }else{
            return "";
        }
    }
}
