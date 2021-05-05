package com.example.pokedex.presentation.Pokedex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.google.android.material.tabs.TabLayout;

import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PokedexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokedexFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokedexRecyclerAdapter pokemonAdapter;
    private PokedexViewModel viewModel;
    private ProgressDialog progressDialog;
    private String TAG = "--PokedexFragment--";
    //private Repository repository;
    private TabLayout tabLayout;

    private int pokemonCount = 10;
    private int pokemonFavCount = 0;
    //private HashSet<Integer> favouritePokemonSet;

    public PokedexFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PokedexFragment.
     */
    public static PokedexFragment newInstance() {
        PokedexFragment fragment = new PokedexFragment();
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
        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_pokedex, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class);
        //viewModel.getPokemonList().clear();
        viewModel.getPokemonList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Pokemon>>() {
            @Override
            public void onChanged(ObservableList<Pokemon> sender) {
            }
            @Override
            public void onItemRangeChanged(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
            }
            @Override
            public void onItemRangeInserted(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
                //updateFavSet();
                if(viewModel.getFavPokemonIDs() != null){
                    if(viewModel.getFavPokemonIDs().contains(sender.get(positionStart).getId())){
                        sender.get(positionStart).isFav = true;
                        viewModel.addPokemonToFavs(sender.get(positionStart));
                    }else{
                        sender.get(positionStart).isFav = false;
                    }
                }
                if(progressDialog != null){
                    progressDialog.setProgress(sender.size());
                }
                if(sender.size() == pokemonCount){
                    Collections.sort(viewModel.getPokemonList(), (p1, p2) -> p1.getId() - p2.getId());
                    populatePokemonAdapter();
                }
            }
            @Override
            public void onItemRangeMoved(ObservableList<Pokemon> sender, int fromPosition, int toPosition, int itemCount) {
            }
            @Override
            public void onItemRangeRemoved(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
            }
        });

        viewModel.getFavsList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Pokemon>>() {
            @Override
            public void onChanged(ObservableList<Pokemon> sender) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
            }

            @Override
            public void onItemRangeInserted(ObservableList<Pokemon> sender, int positionStart, int itemCount) {

                if(progressDialog != null){
                    progressDialog.setProgress(sender.size());
                }

                if(viewModel.getFavsList().size() == viewModel.getFavPokemonIDs().size()) {
                    Collections.sort(viewModel.getFavsList(), (p1, p2) -> p1.getId() - p2.getId());
                    if (tabLayout.getSelectedTabPosition() == 1) {
                        populatePokemonAdapter();
                    }
                }

            }

            @Override
            public void onItemRangeMoved(ObservableList<Pokemon> sender, int fromPosition, int toPosition, int itemCount) {

            }
            @Override
            public void onItemRangeRemoved(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
                //updateFavSet(); //TODO LO MISMO
                if (tabLayout.getSelectedTabPosition() == 1) {
                    populatePokemonAdapter();
                }
            }
        });

        recyclerView = theView.findViewById(R.id.pokedexFrag_recyclerView);
        tabLayout = theView.findViewById(R.id.pokedexFrag_TabLayout);

        //repository = new Repository(getContext(), pokedexViewModel);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getPokemonAll();
                        break;
                    case 1:
                        getPokemonFavs();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String numberOfPokemonStr = sharedPreferences.getString("pokemon_count", "10");
        int number = 10;
        try{
            number = Integer.parseInt(numberOfPokemonStr);
        }catch (Exception e){ }
        pokemonCount=number;


        //updateFavSet(); se obtiene al creal el viewModel
        getPokemonAll();

        return theView;
    }


    private void getPokemonAll(){
        //Descargar los pokemon si no se han descargado ya antes
        if(viewModel.getPokemonList().isEmpty()) {
            downloadPokemonAll();
        }else{
            //Si estan descargados se muestran en pantalla
            populatePokemonAdapter();
        }
    }

    private void getPokemonFavs(){
        //updateFavSet(); //TODO A VER BRO
        if(viewModel.getFavsList().size() != viewModel.getFavPokemonIDs().size()){
            downloadPokemonFavs();
        }else {
            populatePokemonAdapter();
        }
    }

    private void downloadPokemonAll(){
        //Se crea un dialog indicando que se están descargando cosas, el cual se eliminara
        //cuando acabe la descarga.
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Descargando");
        progressDialog.setMessage("Se está descargando la informacion necesaria");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(pokemonCount);
        progressDialog.show();
        viewModel.updatePokemonList(pokemonCount);
    }

    private void downloadPokemonFavs(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Descargando");
        progressDialog.setMessage("Se está descargando la informacion necesaria");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(viewModel.getFavPokemonIDs().size());
        progressDialog.show();
        viewModel.updateFavList();
    }

    /**
     * Puebla el adaptador con los pokemon necesarios
     */
    public void populatePokemonAdapter(){
        Activity act = getActivity();
        if(isAdded() && (act != null)) {

            /*Dependiendo de la pestaña seleccionada, se previsualizan todos
              los pokemon o solo los favoritos */
            ObservableArrayList<Pokemon> pokemonList;
            switch (tabLayout.getSelectedTabPosition()){
                case 0:
                    pokemonList = viewModel.getPokemonList();
                    break;
                case 1:
                    pokemonList = viewModel.getFavsList();
                    break;
                default:
                    pokemonList = viewModel.getPokemonList();
                    break;
            }

            act.runOnUiThread(() -> {
                Log.d(TAG, "Tamaño set de favoritos: " + viewModel.getFavPokemonIDs().size());
                Log.d(TAG, "Tamaño de la lista de favoritos: " + viewModel.getFavsList().size());
                if (progressDialog != null) progressDialog.dismiss();
                pokemonAdapter = new PokedexRecyclerAdapter(getContext(), viewModel, pokemonList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(pokemonAdapter);
            });
        }
    }

}