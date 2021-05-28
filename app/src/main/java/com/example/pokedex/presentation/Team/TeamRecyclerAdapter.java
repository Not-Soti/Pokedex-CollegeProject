package com.example.pokedex.presentation.Team;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private Context context;
    private SpinnerSelectedListener spinnerSelectedListener;
    private DeleteButtonListener deleteButtonListener;

    public TeamRecyclerAdapter(Context c, ObservableArrayList<Pokemon> source, SpinnerSelectedListener spinnerListener, DeleteButtonListener deleteListener){
        context = c;
        layoutInflater = LayoutInflater.from(context);
        sourceList=source;
        spinnerSelectedListener = spinnerListener;
        deleteButtonListener = deleteListener;
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
        String pokemonName = pokemon.getSavedName();
        String capitalizedName = pokemonName.substring(0,1).toUpperCase() + pokemonName.substring(1); //Poner la primera letra en mayuscula
        holder.nameEditText.setText(capitalizedName);

        //Especie
        String pokemonSpecie = pokemon.getSpecies().getName();
        holder.specieTv.setText(pokemonSpecie);

        //Imagen
        holder.imageView.setBackground(pokemon.listSprite);

        //Movimientos
        ArrayList<String> moves = new ArrayList<>();
        for(Move move : pokemon.getMoves()){
            String moveName = move.getMove().getName();
            moves.add(moveName);
        }

        //Se crea un String[] con los movimientos para crear el SpinnerAdapter
        String[] movesArr = new String[moves.size()];
        for(int i=0; i<moves.size(); i++){
            movesArr[i] = moves.get(i); //Se copian los movimientos al array
        }

        Arrays.sort(movesArr); //Se ordena alfabeticamente
        ArrayAdapter<String> moveAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, movesArr);
        moveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /*
        Se toma cada movimiento que estaba guardado en la bbdd, y se busca en el array que tiene
        todos los movimientos disponibles, para obtener su posición y así seleccionarlo al
        cargar la lista. Dicha posicion se guarda en el array savedMovesPos.
         */
        int[] savedMovesPos = new int[4];
        for(int i=0; i<=3; i++){ //Para cada movimiento
            for(int j=0; j<movesArr.length; j++){ //Se recorre la lista con todos los movimientos
                if(movesArr[j].equals(pokemon.savedMoves.get(i))){ //si el movimiento coincide
                    savedMovesPos[i] = j; //se guarda la posicion que tiene en la lista completa
                }
            }
        }

        //Cada spinner se inserta con el adaptador y el movimiento que le corresponde
        for(int i = 0; i<holder.movSpinners.length; i++){
            holder.movSpinners[i].setAdapter(moveAdapter);
            holder.movSpinners[i].setSelection(savedMovesPos[i],false); //false porque si no se ejecuda onItemSelectedListener()
            holder.movSpinners[i].setOnItemSelectedListener(holder);
        }

        //Remove button listener
        holder.removeButton.setOnClickListener(holder);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener, View.OnClickListener{

        //public TextView nameTv;
        public EditText nameEditText;
        public TextView specieTv;
        public ImageView imageView;
        public Spinner mov1Spi, mov2Spi, mov3Spi, mov4Spi;
        public ImageButton removeButton;
        public Spinner[] movSpinners;
        public Pokemon pokemon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameEditText = itemView.findViewById(R.id.card_team_nameText);
            imageView = itemView.findViewById(R.id.card_team_sprite);
            mov1Spi = itemView.findViewById(R.id.card_team_mov1Spinner);
            mov2Spi = itemView.findViewById(R.id.card_team_mov2Spinner);
            mov3Spi = itemView.findViewById(R.id.card_team_mov3Spinner);
            mov4Spi = itemView.findViewById(R.id.card_team_mov4Spinner);
            removeButton = itemView.findViewById(R.id.card_team_removeFromTeam_button);
            specieTv = itemView.findViewById(R.id.card_team_speciesTv);

            movSpinners = new Spinner[]{mov1Spi, mov2Spi, mov3Spi, mov4Spi};

            nameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    Log.d("TAG", "GUARDAR POKE");
                    return false;
                }
            });

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "Item selected de dentrooo");
            String nombre = pokemon.getName();
            String especie = pokemon.getSpecies().getName();
            String mov1 = mov1Spi.getSelectedItem().toString();
            String mov2 = mov2Spi.getSelectedItem().toString();
            String mov3 = mov3Spi.getSelectedItem().toString();
            String mov4 = mov4Spi.getSelectedItem().toString();
            String[] moves = {mov1, mov2, mov3, mov4};

            spinnerSelectedListener.onItemSelected(pokemon, especie, nombre, moves);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            spinnerSelectedListener.onNothingSelected();
        }

        @Override
        public void onClick(View v) {
            //Si es el boton de eliminar el pokemon
            if (v.equals(removeButton)){
                deleteButtonListener.onClick(pokemon);
            }
        }
    }
}
