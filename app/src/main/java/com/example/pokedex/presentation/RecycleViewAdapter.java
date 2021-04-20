package com.example.pokedex.presentation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;

import java.util.List;
import java.util.Locale;

import com.example.pokedex.model.pokemonModel.Pokemon;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    List<Pokemon> pokemonList;
    private Context context;
    private String TAG = "RecycleViewAdapter";
    private Fragment fragment;

    public  RecycleViewAdapter(Context c, Fragment f, List<Pokemon> pokeList){
        context = c;
        pokemonList = pokeList;
        fragment = f;
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

        //Tipos
        String type1 = pokemon.type1Str;
        PresentationUtils.setTypeTextViewFormat(context,holder.type1Tv, type1);
        //Si no tiene segundo tipo, se hace invisible
        String type2 = pokemon.type2Str;
        if(type2==null) holder.type2Tv.setVisibility(View.GONE);
        else {
            holder.type2Tv.setVisibility(View.VISIBLE);
            PresentationUtils.setTypeTextViewFormat(context, holder.type2Tv, type2);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PokemonDetailFragment.PARAM_POKEMON_ID, pokemon.getId());
                Navigation.findNavController(v).navigate(R.id.action_pokedexFragment_to_pokemonDetailFragment,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTv, pokedexNumTv, type1Tv, type2Tv;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.pokeCard_name_tv);
            pokedexNumTv = itemView.findViewById(R.id.pokeCard_id_tv);
            imageView = itemView.findViewById(R.id.pokeCard_sprite);
            type1Tv = itemView.findViewById(R.id.pokeCard_type1);
            type2Tv = itemView.findViewById(R.id.pokeCard_type2);
        }

    }
}
