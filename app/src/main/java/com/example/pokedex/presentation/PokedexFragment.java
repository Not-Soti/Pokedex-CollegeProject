package com.example.pokedex.presentation;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import com.example.pokedex.Persistence.Repository;
import com.example.pokedex.model.pokemonModel.Pokemon;
import com.google.android.material.tabs.TabLayout;

import java.util.Collections;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PokedexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokedexFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecycleViewAdapter pokemonAdapter;
    private PokedexViewModel pokedexViewModel;
    private ProgressDialog progressDialog;
    private String TAG = "--PokedexFragment--";
    private Repository repository;
    private TabLayout tabLayout;

    private int pokemonCount = 10;
    private int pokemonFavCount = 0;
    private HashSet<Integer> favouritePokemonSet;

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

        /*
                TODO *      CAMBIAR A LA SETTINGS ACTIVITY HACE QUE EL
                TODO *      VIEWMODEL SE REINICIE PORQUE ESO, SE CAMBIA DE ACTIVITY
         */

        pokedexViewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class);
        //viewModel.getPokemonList().clear();
        pokedexViewModel.getPokemonList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Pokemon>>() {
            @Override
            public void onChanged(ObservableList<Pokemon> sender) {
            }
            @Override
            public void onItemRangeChanged(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
            }
            @Override
            public void onItemRangeInserted(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
                if(favouritePokemonSet != null){
                    if(favouritePokemonSet.contains(sender.get(positionStart).getId())){
                        sender.get(positionStart).isFav = true;
                    }
                }
                if(progressDialog != null){
                    progressDialog.setProgress(sender.size());
                }
                if(sender.size() == pokemonCount){
                    Collections.sort(pokedexViewModel.getPokemonList(), (p1, p2) -> p1.getId() - p2.getId());
                    populatePokemonAdapter(0);
                }
            }
            @Override
            public void onItemRangeMoved(ObservableList<Pokemon> sender, int fromPosition, int toPosition, int itemCount) {
            }
            @Override
            public void onItemRangeRemoved(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
            }
        });

        pokedexViewModel.getFavsList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Pokemon>>() {
            @Override
            public void onChanged(ObservableList<Pokemon> sender) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<Pokemon> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<Pokemon> sender, int positionStart, int itemCount) {
                sender.get(positionStart).isFav = true;
                if(progressDialog != null){
                    progressDialog.setProgress(sender.size());
                }
                if(sender.size() == pokemonFavCount){
                    Collections.sort(pokedexViewModel.getFavsList(), (p1, p2) -> p1.getId() - p2.getId());
                    populatePokemonAdapter(1);
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList<Pokemon> sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<Pokemon> sender, int positionStart, int itemCount) {

            }
        });

        recyclerView = theView.findViewById(R.id.pokedexFrag_recyclerView);
        tabLayout = theView.findViewById(R.id.pokedexFrag_TabLayout);

        repository = new Repository(getContext(), pokedexViewModel);
        favouritePokemonSet = repository.getFavouritePokemon();

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
        }catch (Exception e){
            Log.d("TAAAAAG", "Mal numero: "+number);
        }
        pokemonCount=number;
        pokemonFavCount=favouritePokemonSet.size();

        getPokemonAll();

        return theView;
    }

    private void getPokemonAll(){
        //Descargar los pokemon si no se han descargado ya antes
        if(pokedexViewModel.getPokemonList().isEmpty()) {
            downloadPokemonAll();
        }else{
            //Si estan descargados se muestran en pantalla
            populatePokemonAdapter(0);
        }
    }

    private void getPokemonFavs(){
        //Descargar los pokemon si no se han descargado ya antes
        if(pokedexViewModel.getFavsList().isEmpty()) {
            downloadPokemonFavs();
        }else{
            //Si estan descargados se muestran en pantalla
            populatePokemonAdapter(1);
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
        repository.getPokemonListFromRest(pokemonCount);
    }

    private void downloadPokemonFavs(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Descargando");
        progressDialog.setMessage("Se está descargando la informacion necesaria");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(pokemonFavCount);
        progressDialog.show();
        repository.getPokemonFavsFromRest(favouritePokemonSet);
    }

    public void populatePokemonAdapter(int type){
        final int finalType = type; //0 = All pokemon
                                    //1 = Favourite pokemon
        Log.d(TAG, "Se llegaaaaaaaaa");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Log.d(TAG, "tam: " + viewModel.getPokemonList().size());
                if (progressDialog!=null) progressDialog.dismiss();
                pokemonAdapter = new RecycleViewAdapter(getContext(), pokedexViewModel, type);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(pokemonAdapter);
            }
        });
    }

}