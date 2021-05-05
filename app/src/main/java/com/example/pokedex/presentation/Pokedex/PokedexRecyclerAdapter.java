package com.example.pokedex.presentation.Pokedex;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.ObservableArrayList;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;

import java.util.Locale;

import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.example.pokedex.presentation.PokemonDetailFragment;
import com.example.pokedex.presentation.PresentationUtils;

public class PokedexRecyclerAdapter extends RecyclerView.Adapter<PokedexRecyclerAdapter.ViewHolder> {

    private Context context;
    private String TAG = "RecycleViewAdapter";
    private PokedexViewModel viewModel;
    private ObservableArrayList<Pokemon> sourceList;

    public PokedexRecyclerAdapter(Context c, PokedexViewModel vm, ObservableArrayList<Pokemon> source){
        context = c;
        viewModel = vm;
        sourceList = source;

/*        if(!isFavThing){
            //treating non-favourite pokemon
            sourceList = vm.getPokemonList();
        }else{
            //trating favourte pokemon
            sourceList = vm.getFavsList();
        }*/
    }

    @NonNull
    @Override
    public PokedexRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View theView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pokemon_list, parent, false);
        return new PokedexRecyclerAdapter.ViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull PokedexRecyclerAdapter.ViewHolder holder, int position) {
        Pokemon pokemon = sourceList.get(position);

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
        String type2 = pokemon.type2Str;
        PresentationUtils.setTypeTextViewFormat(context, holder.type2Tv, type2);


        if(pokemon.isFav){
            holder.favButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_fav_on, null));
        }else{
            holder.favButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_fav_off, null));
        }

        holder.favButton.setOnClickListener(v -> {
            if(!pokemon.isFav) {
                viewModel.addPokemonToFavs(pokemon);
                holder.favButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_fav_on, null));
            }
            else {
                viewModel.removePokemonFromFavs(pokemon);
                holder.favButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_fav_off, null));
            }
        });

        holder.addToTeamButton.setOnClickListener(v -> {
            viewModel.addPokemonToTeam(pokemon);
        });

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(PokemonDetailFragment.PARAM_POKEMON_ID, pokemon.getId());
            Navigation.findNavController(v).navigate(R.id.action_pokedexFragment_to_pokemonDetailFragment,bundle);
        });

    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTv, pokedexNumTv, type1Tv, type2Tv;
        public ImageView imageView;
        public ImageButton favButton;
        public Button addToTeamButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.pokeCard_name_tv);
            pokedexNumTv = itemView.findViewById(R.id.pokeCard_id_tv);
            imageView = itemView.findViewById(R.id.pokeCard_sprite);
            type1Tv = itemView.findViewById(R.id.pokeCard_type1);
            type2Tv = itemView.findViewById(R.id.pokeCard_type2);
            favButton = itemView.findViewById(R.id.pokeCard_fav_button);
            addToTeamButton = itemView.findViewById(R.id.pokeCard_addToTeam_button);
        }


    }
}
