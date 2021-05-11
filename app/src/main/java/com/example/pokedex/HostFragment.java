package com.example.pokedex;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostFragment extends Fragment {

    private Button pokedexBtn;
    private Button teamBtn;
    private Button settingsBtn;

    public HostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HostFragment newInstance(String param1, String param2) {
        HostFragment fragment = new HostFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_host, container, false);

        pokedexBtn = theView.findViewById(R.id.host_pokedexBtn);
        teamBtn = theView.findViewById(R.id.host_teamBtn);
        settingsBtn = theView.findViewById(R.id.host_configBtn);

        pokedexBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_hostFragment_to_pokedexFragment));
        teamBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_hostFragment_to_teamFragment));
        settingsBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_hostFragment_to_settingsActivity));

        return theView;
    }


}