package com.example.pokedex.presentation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokedex.Persistence.PokemonTeamEntity;
import com.example.pokedex.Persistence.Repository;
import com.example.pokedex.R;

import java.util.List;

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

        TeamRecyclerAdapter adapter = new TeamRecyclerAdapter();

        Log.d(TAG, "Obteniendo pokemon");
        Repository repo = new Repository(getContext());

        LiveData<List<PokemonTeamEntity>> pokes = repo.getPokemonTeam();
        Log.d(TAG, "getPokes");
        pokes.observe(getViewLifecycleOwner(), new Observer<List<PokemonTeamEntity>>() {
            @Override
            public void onChanged(List<PokemonTeamEntity> pokemonTeamEntities) {
                for (PokemonTeamEntity p : pokemonTeamEntities){
                    Log.d(TAG, "Pokemon "+p.getId() +"- "+p.getNombre());
                }

            }
        });


        return theView;
    }
}