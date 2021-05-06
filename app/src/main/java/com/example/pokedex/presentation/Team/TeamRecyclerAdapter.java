package com.example.pokedex.presentation.Team;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.Persistence.PokemonTeamEntity;
import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.Move;
import com.example.pokedex.model.pokeApiModel.Pokemon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter.ViewHolder> {

    private String TAG = "-----ADAPTER-----";

    private ObservableArrayList<Pokemon> sourceList;
    private final LayoutInflater layoutInflater;
    private TeamViewModel viewModel;
    private Context context;
    private SpinnerSelectedListener spinnerSelectedListener;

    public TeamRecyclerAdapter(Context c, ObservableArrayList<Pokemon> source, TeamViewModel vm, SpinnerSelectedListener spinnerListener){
        context = c;
        layoutInflater = LayoutInflater.from(context);
        sourceList=source;
        viewModel = vm;
        spinnerSelectedListener = spinnerListener;

    }


    @NonNull
    @Override
    public TeamRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View theView = layoutInflater.inflate(R.layout.card_pokemon_team, parent, false);
        //Log.d(TAG,"Creando ViewHolder");
        return new TeamRecyclerAdapter.ViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamRecyclerAdapter.ViewHolder holder, int position) {
        //Log.d(TAG, "onBindViewHolder");
        Pokemon pokemon = sourceList.get(position);
        holder.pokemon = pokemon;

        //Log.d("-------adapter", "Pokenombre: "+poke.getName());

        //Nombre
        holder.nameTv.setText(pokemon.getName());

        //Imagen
        holder.imageView.setBackground(pokemon.listSprite);

        //Movimientos
        ArrayList<String> moves = new ArrayList<>();
        for(Move move : pokemon.getMoves()){
            String name = move.getMove().getName();
            moves.add(name);
        }

        String[] movesArr = new String[moves.size()];
        for(int i=0; i<moves.size(); i++){
            movesArr[i] = moves.get(i);
        }
        Arrays.sort(movesArr);
        ArrayAdapter<String> moveAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, movesArr);
        moveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for(int i = 0; i<holder.movSpinners.length; i++){
            holder.movSpinners[i].setAdapter(moveAdapter);
            holder.movSpinners[i].setSelection(1,false);
            holder.movSpinners[i].setOnItemSelectedListener(holder);
        }

        //Remove button listener
        holder.removeButton.setOnClickListener(v -> {
            viewModel.removeFromTeam(pokemon);
        });
    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

/*    public void setPokemonTeam(List<PokemonTeamEntity> pokemonTeam){
        Log.d(TAG, "SetPokemonTeam: tamaño "+pokemonTeam.size());
        this.pokemonTeam = pokemonTeam;
        notifyDataSetChanged();
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener{

        public TextView nameTv;
        public ImageView imageView;
        public Spinner mov1Spi, mov2Spi, mov3Spi, mov4Spi;
        public Button removeButton;
        public Spinner[] movSpinners;
        public Pokemon pokemon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.card_team_nameTv);
            imageView = itemView.findViewById(R.id.card_team_sprite);
            mov1Spi = itemView.findViewById(R.id.card_team_mov1Spinner);
            mov2Spi = itemView.findViewById(R.id.card_team_mov2Spinner);
            mov3Spi = itemView.findViewById(R.id.card_team_mov3Spinner);
            mov4Spi = itemView.findViewById(R.id.card_team_mov4Spinner);
            removeButton = itemView.findViewById(R.id.card_team_removeFromTeam_button);
            movSpinners = new Spinner[]{mov1Spi, mov2Spi, mov3Spi, mov4Spi};

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "Item selected de dentrooo");
            String nombre = pokemon.getName();
            String mov1 = mov1Spi.getSelectedItem().toString();
            String mov2 = mov2Spi.getSelectedItem().toString();
            String mov3 = mov3Spi.getSelectedItem().toString();
            String mov4 = mov4Spi.getSelectedItem().toString();
            String[] moves = {mov1, mov2, mov3, mov4};

            spinnerSelectedListener.onItemSelected(pokemon, nombre, moves);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            spinnerSelectedListener.onNothingSelected();
        }
    }
}
