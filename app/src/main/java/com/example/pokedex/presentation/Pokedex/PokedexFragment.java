package com.example.pokedex.presentation.Pokedex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.Pokedex;
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

    private ObservableArrayList<Pokemon> listToUse; //Referencia de la lista a usar para filtrar, ordenar...

    private ImageButton sortButton;
    private boolean isSortDesc = true;

    private int pokemonCount = 10;

    private final int MAX_POKEMON_NUMBER = 1118; //Numero total de pokemon en la API

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
        sortButton = theView.findViewById(R.id.pokedexFrag_sortButton);

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se pone la imagen correspondiente
                if(!isSortDesc){
                    sortButton.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.ic_sort_maxmin, null));
                }else{
                    sortButton.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.ic_sort_minmax, null));
                }
                isSortDesc = !isSortDesc; //Se cambia el criterio de ordenacion

                populatePokemonAdapter(); //Se pintan los pokemon de la lista
            }
        });

        //Se obtiene la barra de busqueda y se añade el listener de eventos
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //Se crea una lista para filtrar
                ObservableArrayList<Pokemon> listToFilter = new ObservableArrayList();

                //Dependiendo de la pestaña en la que se esté, se parte de la lista normal o la de favoritos
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
                //populatePokemonAdapter(listToFilter); //Se pintan los pokemon de la lista
                listToUse = new ObservableArrayList<>();
                listToUse.addAll(listToFilter);
                populatePokemonAdapter();
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
                    //populatePokemonAdapter(viewModel.getPokemonList());
                    listToUse = new ObservableArrayList<>();
                    listToUse.addAll(viewModel.getPokemonList());
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
                        //populatePokemonAdapter(viewModel.getFavsList());
                        listToUse = new ObservableArrayList<>();
                        listToUse.addAll(viewModel.getFavsList());
                        populatePokemonAdapter();
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
                    //populatePokemonAdapter(viewModel.getFavsList());
                    listToUse = new ObservableArrayList<>();
                    listToUse.addAll(viewModel.getFavsList());
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
        String numberOfPokemonStr = sharedPreferences.getString("pokemon_count", "100");
        int number = 100;
        try{
            number = Integer.parseInt(numberOfPokemonStr);
        }catch (Exception e){ }
        pokemonCount=number;
        if(pokemonCount > MAX_POKEMON_NUMBER){
            pokemonCount = MAX_POKEMON_NUMBER; //Limitar el numero al maximo de pokemon que hay en la API
        }

        //getPokemonAll(); //Called in viewModel.getPokemonTeam().observe()

        return theView;
    }


    private void getPokemonAll(){
        //Descargar los pokemon si no se han descargado ya antes
        if(viewModel.getPokemonList().isEmpty()) {
            downloadPokemonAll();
        }else{
            //Si estan descargados se muestran en pantalla
            //populatePokemonAdapter(viewModel.getPokemonList());
            listToUse = new ObservableArrayList<>();
            listToUse.addAll(viewModel.getPokemonList());
            populatePokemonAdapter();
        }
    }

    private void getPokemonFavs(){
        if(viewModel.getFavsList().size() != viewModel.getFavPokemonIDs().size()){
            downloadPokemonFavs();
        }else {
            //populatePokemonAdapter(viewModel.getFavsList());
            listToUse = new ObservableArrayList<>();
            listToUse.addAll(viewModel.getFavsList());
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
            sortPokemonList();

            act.runOnUiThread(() -> {
                if (progressDialog != null) progressDialog.dismiss();
                pokemonAdapter = new PokedexRecyclerAdapter(getContext(), listToUse, favListener, teamListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(pokemonAdapter);
            });
        }
    }

    private void sortPokemonList(){
        if(isSortDesc){
            //Se ordena de mayor a menor
            Collections.sort(listToUse, (p1, p2) -> p1.getId() - p2.getId());
        }else{
            //Se ordena de menor a mayor
            Collections.sort(listToUse, (p1, p2) -> p2.getId() - p1.getId());
        }

    }

}