package com.example.pokedex.presentation.Team;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pokedex.Persistence.PokemonTeamEntity;
import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.example.pokedex.presentation.Pokedex.PokedexRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends Fragment{

    // TODO: Rename and change types of parameters
    private RecyclerView recyclerView;
    private TeamViewModel viewModel;
    private String TAG = "TeamFragment";

    private ProgressDialog progressDialog;

    private boolean isDownloading = false;

    //Listener utilizado para actualizar un pokemon en la BBDD cuando se elige otro movimiento
    private SpinnerSelectedListener spinnerSelectedListener = new SpinnerSelectedListener() {
        @Override
        public void onItemSelected(Pokemon pokemon, String especie,  String nombre, String[] moves) {
            Log.d(TAG, "ItemSelected del fragment");
            viewModel.updatePokemon(pokemon.getId(),especie, nombre, moves);
        }

        @Override
        public void onNothingSelected() {

        }
    };

    //Listener utilizado para eliminar un pokemon de la BBDD cuando se clica el botón adecuado
    private DeleteButtonListener removeButtonListener = new DeleteButtonListener() {
        @Override
        public void onClick(Pokemon pokemon) {
            viewModel.removeFromTeam(pokemon);
        }
    };

    public TeamFragment() {
        // Required empty public constructor
    }

    //Listener utilizado para cuando se cambia el nombre del pokemon
    //Se activa cuando en el teclado se presiona la tecla Enter
    private EditTextChangedListener nameChangedListener = new EditTextChangedListener() {
        @Override
        public void onEditorAction(Pokemon pokemon, String specie, String name, String[] moves) {
            Log.d(TAG, "onEditorAction desde el fragment");
            viewModel.updatePokemon(pokemon.getId(),specie, name, moves);
        }
    };

    /**
     * Factory method
     */
    public static TeamFragment newInstance() {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View theView = inflater.inflate(R.layout.fragment_team, container, false);

        recyclerView = theView.findViewById(R.id.frag_team_recyclerView);

        viewModel = new ViewModelProvider(this).get(TeamViewModel.class);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        int numberOfPokemon = prefs.getInt("cantidad_equipo", 0);

        initViewModelListeners(numberOfPokemon);

        return theView;
    }


    private void downloadPokemon(){
        Log.d(TAG, "Download pokemon, tamaño: "+viewModel.getPokemonTeam().getValue().size());
        //Se crea un dialog indicando que se están descargando cosas, el cual se eliminara
        //cuando acabe la descarga.
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Descargando");
        progressDialog.setMessage("Se está descargando la informacion necesaria");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(Objects.requireNonNull(viewModel.getPokemonTeam().getValue()).size());
        progressDialog.show();
        viewModel.downloadPokemon();
    }

    /**
     * Puebla el adaptador con los pokemon necesarios
     */
    public void populatePokemonAdapter(){
        Activity act = getActivity();
        if(isAdded() && (act != null)) {

            //Se ordena la lista de pokemon a mostrar por ID
            Collections.sort(viewModel.getPokemonList(), (p1, p2) -> p1.getId() - p2.getId());

            //Se recorren, para correlacionar los movimientos guardados en la base de datos con los que se van a mostrar
            for(Pokemon pokemon : viewModel.getPokemonList()){
                for(PokemonTeamEntity pokemonDB : viewModel.getPokemonTeam().getValue()){
                    //Se busca el pokemon en las 2 listas y se copian los datos de uno a otro
                    if (pokemon.getId() == pokemonDB.getId()){
                        pokemon.setSavedName(pokemonDB.getNombre());
                        pokemon.savedMoves = new ArrayList<>();
                        pokemon.savedMoves.add(pokemonDB.getMov1());
                        pokemon.savedMoves.add(pokemonDB.getMov2());
                        pokemon.savedMoves.add(pokemonDB.getMov3());
                        pokemon.savedMoves.add(pokemonDB.getMov4());
                    }
                }
            }

            act.runOnUiThread(() -> {
                if (progressDialog != null) progressDialog.dismiss();
                TeamRecyclerAdapter pokemonAdapter = new TeamRecyclerAdapter(getContext(), viewModel.getPokemonList(), spinnerSelectedListener, removeButtonListener, nameChangedListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(pokemonAdapter);
            });
        }
    }


    public void initViewModelListeners(int numberOfPokemon){
        viewModel.getPokemonList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Pokemon>>() {
            @Override
            public void onChanged(ObservableList<Pokemon> sender) { }

            @Override
            public void onItemRangeChanged(ObservableList<Pokemon> sender, int positionStart, int itemCount) { }

            @Override
            public void onItemRangeInserted(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
                if(progressDialog != null) {
                    progressDialog.setProgress(sender.size());
                }

                //Si se han descargado todos los pokemon, se muestran
                if(sender.size() == numberOfPokemon){
                    populatePokemonAdapter();
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList<Pokemon> sender, int fromPosition, int toPosition, int itemCount) {}

            @Override
            public void onItemRangeRemoved(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
                populatePokemonAdapter();
            }
        });


        viewModel.getPokemonTeam().observe(getViewLifecycleOwner(), pokemonTeamEntities -> {
            if((!isDownloading) && (viewModel.getPokemonTeam().getValue().size()==numberOfPokemon) && (numberOfPokemon!=0)) {
                isDownloading=true;
                downloadPokemon();
            }
        });
    }
}