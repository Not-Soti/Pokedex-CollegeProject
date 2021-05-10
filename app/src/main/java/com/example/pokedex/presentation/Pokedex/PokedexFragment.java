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
import android.widget.SearchView;

import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.google.android.material.tabs.TabLayout;

import java.util.Collections;
import java.util.Iterator;

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
    private TabLayout tabLayout;
    private SearchView searchView;

    private int pokemonCount = 10;

    private ButtonFavListener favListener = new ButtonFavListener() {
        @Override
        public void onClick(Pokemon pokemon, boolean isFav) {
            if(!pokemon.isFav) {
                viewModel.addPokemonToFavs(pokemon);
            }else {
                viewModel.removePokemonFromFavs(pokemon);
            }
        }
    };

    private ButtonTeamListener teamListener = new ButtonTeamListener() {
        @Override
        public void onClick(Pokemon pokemon) {
            if(!pokemon.isInTeam){
                viewModel.addPokemonToTeam(pokemon);
            }else{
                viewModel.removeFromTeam(pokemon);
            }
        }
    };

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

        searchView = theView.findViewById(R.id.pokedexFrag_searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //Dependiendo de en que pestaña esté, si filtrara sobre una u otra lista
                ObservableArrayList<Pokemon> listToFilter = new ObservableArrayList();
                if(tabLayout.getSelectedTabPosition() == 0){
                    listToFilter.addAll(viewModel.getPokemonList());
                }else if(tabLayout.getSelectedTabPosition() == 1){
                    listToFilter.addAll(viewModel.getFavsList());
                }

                //Filtrar si se escriben al menos 3 caracteres
                if(newText.length() >= 3) {
                    Iterator<Pokemon> it = listToFilter.iterator();
                    while (it.hasNext()) {
                        if (!it.next().getName().contains(newText)) {
                            it.remove();
                        }
                    }
                }
                populatePokemonAdapter(listToFilter);
                return false;
            }
        });

        //viewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class); TODO Owner activity para que sea el de la actividad
        viewModel = new ViewModelProvider(this).get(PokedexViewModel.class);

        viewModel.getPokemonTeam().observe(getViewLifecycleOwner(), pokemonTeamEntities -> {
            viewModel.updateIDsInTeam();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            int numberOfPokemonTeam = prefs.getInt("cantidad_equipo", 0);
            if(numberOfPokemonTeam == viewModel.getPokemonTeam().getValue().size()){
                getPokemonAll();
            }
        });

        viewModel.getPokemonList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Pokemon>>() {
            @Override
            public void onChanged(ObservableList<Pokemon> sender) {
                viewModel.updateIDsInTeam();
            }
            @Override
            public void onItemRangeChanged(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
            }
            @Override
            public void onItemRangeInserted(ObservableList<Pokemon> sender, int positionStart, int itemCount) {

                //Se toman los favoritos y se indica si el pokemon insertado lo es
                if(viewModel.getFavPokemonIDs() != null){
                    if(viewModel.getFavPokemonIDs().contains(sender.get(positionStart).getId())){
                        sender.get(positionStart).isFav = true;
                        viewModel.addPokemonToFavs(sender.get(positionStart));
                    }else{
                        sender.get(positionStart).isFav = false;
                    }
                }

                //Se toma el equipo y se indica si el pokemon insertado pertenece a el
                if(viewModel.getPokemonIDsInTeam() != null){
                    if(viewModel.getPokemonIDsInTeam().contains(sender.get(positionStart).getId())){
                        sender.get(positionStart).isInTeam = true;
                        viewModel.addPokemonToFavs(sender.get(positionStart));
                    }else{
                        sender.get(positionStart).isInTeam = false;
                    }
                }

                //Se actualiza el progreso del dialogo
                if(progressDialog != null){
                    progressDialog.setProgress(sender.size());
                }

                //Si se han descargado todos los pokemon, se popula el recyclerView
                if(sender.size() == pokemonCount){
                    //viewModel.setUsableList(viewModel.getPokemonList());
                    populatePokemonAdapter(viewModel.getPokemonList());
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
                viewModel.updateIDsInTeam();
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
                    if (tabLayout.getSelectedTabPosition() == 1) {
                        //viewModel.setUsableList(viewModel.getFavsList());
                        populatePokemonAdapter(viewModel.getFavsList());
                    }
                }

            }

            @Override
            public void onItemRangeMoved(ObservableList<Pokemon> sender, int fromPosition, int toPosition, int itemCount) {

            }
            @Override
            public void onItemRangeRemoved(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
                if (tabLayout.getSelectedTabPosition() == 1) {
                    //viewModel.setUsableList(viewModel.getFavsList());
                    populatePokemonAdapter(viewModel.getFavsList());
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

        //getPokemonAll(); //Called in viewModel.getPokemonTeam().observe()

        return theView;
    }


    private void getPokemonAll(){
        //Descargar los pokemon si no se han descargado ya antes
        if(viewModel.getPokemonList().isEmpty()) {
            downloadPokemonAll();
        }else{
            //Si estan descargados se muestran en pantalla
            populatePokemonAdapter(viewModel.getPokemonList());
        }
    }

    private void getPokemonFavs(){
        if(viewModel.getFavsList().size() != viewModel.getFavPokemonIDs().size()){
            downloadPokemonFavs();
        }else {
            populatePokemonAdapter(viewModel.getFavsList());
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
    public void populatePokemonAdapter(ObservableArrayList<Pokemon> pokemonList){
        Activity act = getActivity();
        if(isAdded() && (act != null)) {

            /*Dependiendo de la pestaña seleccionada, se previsualizan todos
              los pokemon o solo los favoritos */
/*            ObservableArrayList<Pokemon> pokemonList;
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
            }*/

            //ObservableArrayList<Pokemon> pokemonList = viewModel.getUsableList();

            //Se ordena la lista por IDs
            Collections.sort(pokemonList, (p1, p2) -> p1.getId() - p2.getId());

            act.runOnUiThread(() -> {
                if (progressDialog != null) progressDialog.dismiss();
                pokemonAdapter = new PokedexRecyclerAdapter(getContext(), pokemonList, favListener, teamListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(pokemonAdapter);
            });
        }
    }

}