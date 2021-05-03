package com.example.pokedex.presentation;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;

public class TeamRecyclerAdapter extends RecyclerView.Adapter<PokedexRecyclerAdapter.ViewHolder> {
    @NonNull
    @Override
    public PokedexRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PokedexRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv;
        public ImageView imageView;
        public Spinner mov1Spi, mov2Spi, mov3Spi, mov4Spi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.pokeCard_name_tv);
            imageView = itemView.findViewById(R.id.pokeCard_sprite);
            mov1Spi = itemView.findViewById(R.id.card_team_mov1Spinner);
            mov2Spi = itemView.findViewById(R.id.card_team_mov2Spinner);
            mov3Spi = itemView.findViewById(R.id.card_team_mov3Spinner);
            mov4Spi = itemView.findViewById(R.id.card_team_mov4Spinner);
        }
    }
}
