package com.example.pokedex.presentation.Pokedex;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
    private ObservableArrayList<Pokemon> sourceList;
    private ButtonFavListener favListener;
    private ButtonTeamListener teamListener;

    public PokedexRecyclerAdapter(Context c, ObservableArrayList<Pokemon> source, ButtonFavListener favListener, ButtonTeamListener teamListener){
        context = c;
        sourceList = source;
        this.favListener = favListener;
        this.teamListener = teamListener;
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
        holder.pokemon = pokemon;

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

        if(pokemon.isInTeam){
            holder.addToTeamButton.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_team_on, null));
        }else{
            holder.addToTeamButton.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_team_off, null));
        }

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nameTv, pokedexNumTv, type1Tv, type2Tv;
        public ImageView imageView;
        public ImageButton favButton;
        public ImageButton addToTeamButton;

        protected Pokemon pokemon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.pokeCard_name_tv);
            pokedexNumTv = itemView.findViewById(R.id.pokeCard_id_tv);
            imageView = itemView.findViewById(R.id.pokeCard_sprite);
            type1Tv = itemView.findViewById(R.id.pokeCard_type1);
            type2Tv = itemView.findViewById(R.id.pokeCard_type2);
            favButton = itemView.findViewById(R.id.pokeCard_fav_button);
            addToTeamButton = itemView.findViewById(R.id.pokeCard_addToTeam_button);

            favButton.setOnClickListener(this);
            addToTeamButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "Pulsado button");
            if (v.equals(favButton)){
                //Si se pulsa el boton de añadir a favoritos
                Log.d(TAG, "FAV button");

                //Se cambia por la imagen correspondiente
                if(!pokemon.isFav){
                    favButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_fav_on, null));
                }else{
                    favButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_fav_off, null));
                }

                //Finalmente se trata el click
                favListener.onClick(pokemon, pokemon.isFav);

            }else if(v.equals(addToTeamButton)){
                //Si se pulsa el boton de añadir al equipo
                Log.d(TAG, "TEAM button");

                //Se cambia la imagen correspondiente
                if(pokemon.isInTeam){
                    addToTeamButton.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_team_on, null));
                }else{
                    addToTeamButton.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_team_off, null));
                }

                //Finalmente se trata el click
                teamListener.onClick(pokemon);
            }
        }
    }
}
