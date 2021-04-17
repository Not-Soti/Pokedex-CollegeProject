package com.example.pokedex.presentation;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.pokedex.R;

public class PresentationUtils {

    /**
     * Metodo que da formato a una TextView de tipo
     * @param tv: TextView a la que dar formato
     * @param type: Tipo del que obtener el formato
     */
    public static void setTypeTextViewFormat(Context c, TextView tv, String type){
        switch (type){
            case "normal":
                tv.setText(c.getString(R.string.type_normal));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_normal));
                break;
            case "fire":
                tv.setText(c.getString(R.string.type_fire));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_fire));
                break;
            case "water":
                tv.setText(c.getString(R.string.type_water));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_water));
                break;
            case "grass":
                tv.setText(c.getString(R.string.type_grass));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_grass));
                break;
            case "electric":
                tv.setText(c.getString(R.string.type_electric));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_electric));
                break;
            case "ice":
                tv.setText(c.getString(R.string.type_ice));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_ice));
                break;
            case "fighting":
                tv.setText(c.getString(R.string.type_fighting));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_fighting));
                break;
            case "poison":
                tv.setText(c.getString(R.string.type_poison));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_poison));
                break;
            case "ground":
                tv.setText(c.getString(R.string.type_ground));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_ground));
                break;
            case "flying":
                tv.setText(c.getString(R.string.type_flying));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_flying));
                break;
            case "psychic":
                tv.setText(c.getString(R.string.type_psychic));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_psychic));
                break;
            case "bug":
                tv.setText(c.getString(R.string.type_bug));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_bug));
                break;
            case "rock":
                tv.setText(c.getString(R.string.type_rock));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_rock));
                break;
            case "ghost":
                tv.setText(c.getString(R.string.type_ghost));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_ghost));
                break;
            case "dark":
                tv.setText(c.getString(R.string.type_dark));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_dark));
                break;
            case "dragon":
                tv.setText(c.getString(R.string.type_dragon));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_dragon));
                break;
            case "steel":
                tv.setText(c.getString(R.string.type_steel));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_steel));
                break;
            case "fairy":
                tv.setText(c.getString(R.string.type_fairy));
                tv.setBackgroundColor(ContextCompat.getColor(c, R.color.type_fairy));
                break;
        }
    }
}
