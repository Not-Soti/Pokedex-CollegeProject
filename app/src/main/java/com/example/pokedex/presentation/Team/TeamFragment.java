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

import com.example.pokedex.Persistence.PokemonTeamEntity;
import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.example.pokedex.presentation.Pokedex.PokedexRecyclerAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private RecyclerView recyclerView;
    private TeamViewModel viewModel;
    private String TAG = "TeamFragment";

    private ProgressDialog progressDialog;

    private boolean isDownloading = false;

    public TeamFragment() {
        // Required empty public constructor
    }

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
        Log.d(TAG, "number of pokemon: "+numberOfPokemon);

        viewModel.getPokemonTeam().observe(getViewLifecycleOwner(), pokemonTeamEntities -> {
            if((!isDownloading) && (viewModel.getPokemonTeam().getValue().size()==numberOfPokemon) && (numberOfPokemon!=0)) {
                isDownloading=true;
                downloadPokemon();
            }
        });

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
                    Collections.sort(viewModel.getPokemonList(), (p1, p2) -> p1.getId() - p2.getId());
                    populatePokemonAdapter();
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList<Pokemon> sender, int fromPosition, int toPosition, int itemCount) {}

            @Override
            public void onItemRangeRemoved(ObservableList<Pokemon> sender, int positionStart, int itemCount) {}
        });

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

            act.runOnUiThread(() -> {
                if (progressDialog != null) progressDialog.dismiss();
                TeamRecyclerAdapter pokemonAdapter = new TeamRecyclerAdapter(getContext(), viewModel.getPokemonList());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(pokemonAdapter);
            });
        }
    }
}