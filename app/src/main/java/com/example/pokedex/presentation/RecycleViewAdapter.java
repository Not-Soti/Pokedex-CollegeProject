package com.example.pokedex.presentation;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;

import java.util.List;
import java.util.Locale;

import model.pokemonModel.Pokemon;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    List<Pokemon> pokemonList;

    public  RecycleViewAdapter(List<Pokemon> pokeList){
        pokemonList = pokeList;
    }

    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View theView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_card, parent, false);
        return new RecycleViewAdapter.ViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);

        //Nombre
        String name = pokemon.getName();
        String capitalizedName = name.substring(0,1).toUpperCase() + name.substring(1); //Poner la primera letra en mayuscula
        holder.nameTv.setText(capitalizedName);

        //ID
        holder.pokedexNumTv.setText(String.format(Locale.getDefault(),"Nº %d", pokemon.getId()));

        //Imagen
        holder.imageView.setBackground(pokemon.listSprite);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTv, pokedexNumTv;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.pokeCard_name_tv);
            pokedexNumTv = itemView.findViewById(R.id.pokeCard_id_tv);
            imageView = itemView.findViewById(R.id.pokeCard_image);
            //imageView.setBackgroundResource(R.drawable.pokemock);
        }
    }
}
