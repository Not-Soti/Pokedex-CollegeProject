package com.example.pokedex.presentation.Team;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.Persistence.PokemonTeamEntity;
import com.example.pokedex.R;

import java.util.List;

public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter.ViewHolder> {

    private String TAG = "-----ADAPTER-----";

    private List<PokemonTeamEntity> pokemonTeam;
    private final LayoutInflater layoutInflater;

    public TeamRecyclerAdapter(Context context, List<PokemonTeamEntity> pokeTeam){
        layoutInflater = LayoutInflater.from(context);
        pokemonTeam=pokeTeam;
    }


    @NonNull
    @Override
    public TeamRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View theView = layoutInflater.inflate(R.layout.card_pokemon_team, parent, false);
        Log.d(TAG,"Creando ViewHolder");
        return new TeamRecyclerAdapter.ViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamRecyclerAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        PokemonTeamEntity poke = pokemonTeam.get(position);

        Log.d("-------adapter", "Pokenombre: "+poke.getNombre());

        holder.nameTv.setText(poke.getNombre());

    }

    @Override
    public int getItemCount() {
        //Log.d(TAG, "ItemCount"+pokemonTeam.size());
        return pokemonTeam.size();
    }

    public void setPokemonTeam(List<PokemonTeamEntity> pokemonTeam){
        Log.d(TAG, "SetPokemonTeam: tamaño "+pokemonTeam.size());
        this.pokemonTeam = pokemonTeam;
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv;
        public ImageView imageView;
        public Spinner mov1Spi, mov2Spi, mov3Spi, mov4Spi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.card_team_nameTv);
            imageView = itemView.findViewById(R.id.card_team_sprite);
            mov1Spi = itemView.findViewById(R.id.card_team_mov1Spinner);
            mov2Spi = itemView.findViewById(R.id.card_team_mov2Spinner);
            mov3Spi = itemView.findViewById(R.id.card_team_mov3Spinner);
            mov4Spi = itemView.findViewById(R.id.card_team_mov4Spinner);
        }
    }
}
