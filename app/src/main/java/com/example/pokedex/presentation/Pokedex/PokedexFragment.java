package com.example.pokedex.presentation.Pokedex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.AbilityDetail.AbilityDetail;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail;
import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Collections;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PokedexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokedexFragment extends Fragment {

    private static final int MAX_POKEMON_NUMBER = 1118; //Numero total de pokemon en la API
    private static final int MAX_MOVE_NUMBER = 621; //Numero total de movimientos en la API
    private static final int MAX_ABILITY_NUMBER = 232; //Numero total de habilidades en la API

    private RecyclerView recyclerView;
    private PokedexRecyclerAdapter pokemonAdapter;
    private PokedexViewModel viewModel;
    private ProgressDialog progressDialog;
    private String TAG = "--PokedexFragment--";
    private TabLayout tabLayout;
    private SearchView filterSearchView; //SearchView usada para filtrar por nombre / ID

    private ObservableArrayList<Pokemon> listToUse; //Referencia de la lista a usar para filtrar, ordenar...

    private ImageButton sortButton;
    private boolean isSortDesc = true;

    private FloatingActionButton searchButton;

    /*Flag que controla si se esta haciendo una busqueda personalizada o no.
      Cuando no se hace, se descargan los N primeros pokemon, por lo que no se muestran
      hasta que no se descarguen todos. Al hacer una búsqueda no es posible saber de antemano
      cuantos se van a descargar, por lo tanto se mostraran a medida que se descarguen.
     */
    private boolean isPerformingSearch = false;

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

        filterSearchView = theView.findViewById(R.id.pokedexFrag_filter_searchView);
        sortButton = theView.findViewById(R.id.pokedexFrag_sortButton);

        searchButton = theView.findViewById(R.id.pokedexFrag_searchFAB);

        sortButton.setOnClickListener(v -> {
            //Se pone la imagen correspondiente
            if(!isSortDesc){
                sortButton.setImageResource(R.drawable.ic_sort_minmax);
            }else{
                sortButton.setImageResource(R.drawable.ic_sort_maxmin);
            }
            isSortDesc = !isSortDesc; //Se cambia el criterio de ordenacion

            populatePokemonAdapter(); //Se pintan los pokemon de la lista
        });

        //Se obtiene la barra de busqueda y se añade el listener de eventos
        filterSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPokemonList(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterPokemonList(newText);
                return false;
            }
        });

        //Al darle al boton de buscar, se abre el dialogo con las opciones de busqueda
        searchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                searchFragment.show(fragmentManager, "search_dialog");
                
            }
        });

        viewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class); //TODO Owner activity para que sea el de la actividad
        //viewModel = new ViewModelProvider(this).get(PokedexViewModel.class);

        viewModel.getPokemonTeam().observe(getViewLifecycleOwner(), pokemonTeamEntities -> {
            viewModel.updateIDsInTeam();

            /*Se obtiene de la base de datos cuales son los pokemon favoritos.
              Cuando se decarguen se hayan obtenido todos los de la BBDD, se
              descargan los pokemon de internet.
             */
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            int numberOfPokemonTeam = prefs.getInt("cantidad_equipo", 0);
            if(numberOfPokemonTeam == viewModel.getPokemonTeam().getValue().size()) {
                if (viewModel.getAbilityList().isEmpty()) {
                    getAbilities(); //Se descargan las abilidades si no estan descargadas
                }else if(viewModel.getMoveList().isEmpty()){
                    getMoves(); //Se descargan los movimientos si no estan descargados
                }else{
                    getPokemonAll(); //Si no, descargar pokemon
                }
            }
        });

        initViewModelListeners();

        recyclerView = theView.findViewById(R.id.pokedexFrag_recyclerView);
        tabLayout = theView.findViewById(R.id.pokedexFrag_TabLayout);

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

        //getPokemonAll(); //Se llama en viewModel.getPokemonTeam().observe()

        return theView;
    }


    protected void getPokemonAll(){
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

    protected void getPokemonFavs(){
        if(viewModel.getFavsList().size() != viewModel.getFavPokemonIDs().size()){
            viewModel.clearFavsList();
            downloadPokemonFavs();
        }else {
            //populatePokemonAdapter(viewModel.getFavsList());
            listToUse = new ObservableArrayList<>();
            listToUse.addAll(viewModel.getFavsList());
            populatePokemonAdapter();
        }
    }

    protected void getPokemonByType(String type){
        //Se comprueba que el texto introducido sea un tipo que existe
        PokemonSearchHelper searchHelper = new PokemonSearchHelper(requireContext());
        boolean isValid = searchHelper.isValidType(type);

        //Se no es valido se muestra un mensaje indicando cuales son
        if(!isValid){
            //Se crea el dialogo de aviso
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Tipo incorrecto. Los tipos existentes son los siguientes:\n");
            for(String validType : searchHelper.getValidTypes()){
                stringBuilder.append(type + ", ");
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Tipo incorrecto").setMessage(stringBuilder)
                    .setNeutralButton("Aceptar", (dialog, which) -> dialog.dismiss());
            builder.create().show(); //Se muestra el dialogo
        }else {
            //Si es valido se busca
            //Se obtiene el nombre en ingles porque es el que acepta la API
            String englishType = searchHelper.getTranslatedType(type);

            viewModel.clearPokemonList(); //Se reinicia la lista de pokemon que habia
            //tabLayout.selectTab(tabLayout.getTabAt(0)); //Se selecciona la pestaña de la lista principal //TODO La pestaña se queda en la de favs, pero si se cambia asi se descargan todos

            downloadPokemonByType(englishType.toLowerCase());
        }
    }

    protected void getPokemonByMove(int moveId){
        viewModel.clearPokemonList();
        downloadPokemonByMove(moveId);

    }

    protected void getPokemonByHability(int abilityId){
        viewModel.clearPokemonList();
        downloadPokemonByAbility(abilityId);
    }

    private void downloadPokemonAll(){
        isPerformingSearch = false; //Se descargan todos indiscriminadamente
        //Se crea un dialog indicando que se están descargando cosas, el cual se eliminara
        //cuando acabe la descarga.
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Descargando");
        progressDialog.setMessage("Estudiando las características de los pokemon");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(pokemonCount);
        progressDialog.show();
        viewModel.updatePokemonList(pokemonCount);
    }

    private void downloadPokemonFavs(){
        isPerformingSearch = false; //Se descargan todos indiscriminadamente
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

    private void downloadPokemonByType(String type){
        Log.d(TAG, "downloadPokemonByType");
        isPerformingSearch = true; //Se descargan los que encajen con el parametro indicado
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Descargando");
        progressDialog.setMessage("Se está descargando la informacion necesaria");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        viewModel.updatePokemonListByType(pokemonCount, type);
    }


    private void downloadPokemonByMove(int moveID){
        Log.d(TAG, "downloadPokemonByMove");
        isPerformingSearch = true; //Se descargan los que encajen con el parametro indicado
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Descargando");
        progressDialog.setMessage("Se está descargando la informacion necesaria");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        viewModel.updatePokemonListByMove(pokemonCount, moveID);
    }

    private void downloadPokemonByAbility(int abilityID){
        Log.d(TAG, "downloadPokemonByAbility");
        isPerformingSearch = true; //Se descargan los que encajen con el parametro indicado
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Descargando");
        progressDialog.setMessage("Se está descargando la informacion necesaria");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        viewModel.updatePokemonListByAbility(pokemonCount, abilityID);
    }


    /**
     * Puebla el adaptador con los pokemon necesarios
     */
    public void populatePokemonAdapter(){
        Activity act = getActivity();
        if(isAdded() && (act != null)) {
            //Se ordena la lista por ID
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

    private void filterPokemonList(String newText){
        //Se crea una lista para filtrar
        ObservableArrayList<Pokemon> listToFilter = new ObservableArrayList<>();

        //Dependiendo de la pestaña en la que se esté, se parte de la lista normal o la de favoritos
        if(tabLayout.getSelectedTabPosition() == 0){
            listToFilter.addAll(viewModel.getPokemonList());
        }else if(tabLayout.getSelectedTabPosition() == 1){
            listToFilter.addAll(viewModel.getFavsList());
        }

        //Si se escriben números se filtra por id
        try{
            int idTofilter = Integer.parseInt(newText);

            //Filtrar si se escriben al menos 3 caracteres
            Iterator<Pokemon> it = listToFilter.iterator();
            while (it.hasNext()) {
                //Se obtiene el id y se pasa a String para comparar
                if (!String.valueOf(it.next().getId()).contains(String.valueOf(idTofilter))) {
                    it.remove();
                }
            }
        }catch (NumberFormatException ex){

            //Si se han escrito letras, se filtra por nombre
            //Filtrar si se escriben al menos 3 caracteres
            if(newText.length() >= 3) {
                Iterator<Pokemon> it = listToFilter.iterator();
                while (it.hasNext()) {
                    if (!it.next().getName().contains(newText)) {
                        it.remove();
                    }
                }
            }
        }
        //Se reinicia la lista de pokemon a usar y se añaden los filtrados
        listToUse = new ObservableArrayList<>();
        listToUse.addAll(listToFilter);
        populatePokemonAdapter();
    }

    /**
     * Metodo utilizado para descargar los movimientos existentes
     * y su traduccion
     */
    private void getMoves(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Descargando");
        progressDialog.setMessage("Aprendiendo movimientos de los Pokémon");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(MAX_MOVE_NUMBER);
        progressDialog.show();
        viewModel.updateMoves(MAX_MOVE_NUMBER);
    }

    /**
     * Metodo utilizado para descargar las habilidades existentes
     * y su traduccion
     */
    private void getAbilities(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Descargando");
        progressDialog.setMessage("Observando sus habilidades");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(MAX_ABILITY_NUMBER);
        progressDialog.show();
        viewModel.updateAbilities(MAX_ABILITY_NUMBER);
    }


    /**
     * Metodo que inicia los listeners de las ObservableArrayList
     * que existen en el viewmodel
     */
    private void initViewModelListeners(){
        //Lista de pokemon generica
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

                //En caso de estar descargando pokemon sabiendo el limite maximo,
                //si se han descargado todos los pokemon, se popula el recyclerView
                if(!isPerformingSearch) {
                    if (sender.size() == pokemonCount) {
                        listToUse = new ObservableArrayList<>();
                        listToUse.addAll(viewModel.getPokemonList());
                        populatePokemonAdapter();
                    }
                }else{
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


        //Lista de pokemon favoritos
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
                //Log.d(TAG, "A descargar " + viewModel.getFavPokemonIDs().size() + ", descargados " + viewModel.getFavsList().size());
                if(progressDialog != null){
                    progressDialog.setProgress(sender.size());
                }

                //Se marca el pokemon como favorito
                sender.get(positionStart).isFav = true;

                //Se toma el equipo y se indica si el pokemon insertado pertenece a el
                if(viewModel.getPokemonIDsInTeam() != null){
                    if(viewModel.getPokemonIDsInTeam().contains(sender.get(positionStart).getId())){
                        sender.get(positionStart).isInTeam = true;
                        viewModel.addPokemonToFavs(sender.get(positionStart));
                    }else{
                        sender.get(positionStart).isInTeam = false;
                    }
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


        //Lista de movimientos
        viewModel.getMoveList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<MoveDetail>>() {
            @Override
            public void onChanged(ObservableList<MoveDetail> sender) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<MoveDetail> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<MoveDetail> sender, int positionStart, int itemCount) {
                if(progressDialog != null){
                    progressDialog.setProgress(sender.size());
                }

                if(sender.size() == MAX_MOVE_NUMBER){
                    //Si se descargaron todos los movimientos, se ordenan por ID y se pasa a descargar los pokemon
                    Collections.sort(viewModel.getMoveList(), (m1, m2) -> m1.getId() - m2.getId());

                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }

                    getPokemonAll(); //Descargar pokemon
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList<MoveDetail> sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<MoveDetail> sender, int positionStart, int itemCount) {

            }
        });


        //Lista de habilidades
        viewModel.getAbilityList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<AbilityDetail>>() {
            @Override
            public void onChanged(ObservableList<AbilityDetail> sender) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<AbilityDetail> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<AbilityDetail> sender, int positionStart, int itemCount) {
                //Log.d(TAG, "Insertando habilidad "+sender.get(positionStart).getName());
                if(progressDialog != null){
                    progressDialog.setProgress(sender.size());
                }

                if(sender.size() == MAX_ABILITY_NUMBER){
                    //Si se descargaron todos los movimientos, se ordenan por ID y se pasa a descargar los pokemon
                    Collections.sort(viewModel.getAbilityList(), (a1, a2) -> a1.getId() - a2.getId());

                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }

                    //Si no se tienen los movimientos se descargan.
                    if(viewModel.getMoveList().isEmpty()) {
                        getMoves(); //Descargar los movimientos
                    }else{
                        //Si ya se tenian de antes, se descargan los pokemon
                        getPokemonAll();
                    }
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList<AbilityDetail> sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<AbilityDetail> sender, int positionStart, int itemCount) {

            }
        });
    }

}