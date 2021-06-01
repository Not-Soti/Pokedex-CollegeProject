package com.example.pokedex.presentation.Pokedex;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.AbilityDetail.AbilityDetail;
import com.example.pokedex.model.pokeApiModel.AbilityDetail.AbilityDetail_Language__3;
import com.example.pokedex.model.pokeApiModel.AbilityDetail.AbilityDetail_Name;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail_Language__3;
import com.example.pokedex.model.pokeApiModel.MoveDetail.MoveDetail_Name;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Fragmento de opciones de búsqueda
 */
public class SearchFragment extends DialogFragment {

    private RadioGroup radioGroup;
    private Button searchButton;
    //private EditText editText;
    private Spinner spinner;
    private PokedexViewModel viewModel;

    private String TAG = "--SearchFragment--";

    private String systemLanguaje; //Lenguaje del sistema, para obtener traducciones

    public SearchFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pokedex_search, container, false);

        radioGroup = view.findViewById(R.id.search_diag_radioGroup);
        searchButton = view.findViewById(R.id.search_diag_searchBtn);
        //editText = view.findViewById(R.id.search_diag_inputText);
        spinner = view.findViewById(R.id.search_diag_spinner);

        viewModel = new ViewModelProvider(requireActivity()).get(PokedexViewModel.class);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.search_diag_moveBtn){
                    ArrayList<String> translatedMoves = getTranslatedMoves(); //Se obtienen las traducciones de los movimientos
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, translatedMoves);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }else if(checkedId == R.id.search_diag_habilityBtn){
                    ArrayList<String> translatedAbilities = getTranslatedAbilities(); //Se obtienen las traducciones de las habilidades
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, translatedAbilities);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }else if(checkedId == R.id.search_diag_typeBtn){
                    ArrayList<String> translatedTypes = getTranslatedTypes(); //Se obtienen los tipos de los pokemon traducidos
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, translatedTypes);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }else if(checkedId == R.id.search_diag_allBtn){
                    spinner.setAdapter(null);
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View v) {
                int checkedId = radioGroup.getCheckedRadioButtonId(); //Obtener que radio button esta seleccionado
                switch (checkedId) {
                    case R.id.search_diag_allBtn:
                        viewModel.clearPokemonList(); //Se reinicia para que se descarguen todos
                        ((PokedexFragment) getParentFragment()).getPokemonAll();
                        dismiss();
                        break;
                    case R.id.search_diag_typeBtn:
                        String selectedType = spinner.getSelectedItem().toString();
                        ((PokedexFragment) getParentFragment()).getPokemonByType(selectedType);
                        dismiss();
                        break;
                    case R.id.search_diag_moveBtn:
                        int selectedMove = spinner.getSelectedItemPosition();
                        int moveIdToSearch = viewModel.getMoveList().get(selectedMove).getId();

                        ((PokedexFragment) getParentFragment()).getPokemonByMove(moveIdToSearch);
                        dismiss();
                        break;
                    case R.id.search_diag_habilityBtn:
                        int selectedAbility = spinner.getSelectedItemPosition();
                        int abilityIdToSearch = viewModel.getAbilityList().get(selectedAbility).getId();
                        ((PokedexFragment) getParentFragment()).getPokemonByHability(abilityIdToSearch);
                        dismiss();
                        break;
                }
            }
        });

        //Se obtiene el lenguaje del sistema. Si no es español, se usa ingles por defecto
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals(new Locale("es").getLanguage())){
            systemLanguaje = "es";
        }else{
            systemLanguaje = "en";
        }

        return view;
    }

    private ArrayList<String> getTranslatedMoves(){
        ArrayList<String> result = new ArrayList<>();

        //Se recorren todos los movimientos buscando su traduccion
        for(MoveDetail moveDetail : viewModel.getMoveList()) {
            String translatedMove = "";

            //Se busca la entrada en el idioma correcto
            List<MoveDetail_Name> listOfLanguajes = moveDetail.getMoveDetailNames(); //Lista con el nombre en los diferentes idiomas
            boolean hasEntrie = false;
            int pos = 0;
            //Se recorren todas las entradas hasta encontrar la que esta en el idioma requerido
            while ((!hasEntrie) && (pos < listOfLanguajes.size())) {
                //Se obtiene el lenguaje de la entrada
                MoveDetail_Language__3 lang = listOfLanguajes.get(pos).getLanguage();

                if (lang.getName().equals(systemLanguaje)) {
                    translatedMove = listOfLanguajes.get(pos).getName();
                    hasEntrie = true;
                }
                pos++;
            }

            result.add(translatedMove);
        }
        return result;
    }

    private ArrayList<String> getTranslatedAbilities(){
        ArrayList<String> result = new ArrayList<>();

        for(AbilityDetail abilityDetail : viewModel.getAbilityList()){
            String translatedAbility = "";

            //Se busca el idioma correcto
            List<AbilityDetail_Name> listOfLanguajes = abilityDetail.getAbilityDetailNames();
            boolean hasEntrie = false;
            int pos = 0;
            //Se recorren todas las entradas hasta encontrar la que esta en el idioma requerido
            while ((!hasEntrie) && (pos < listOfLanguajes.size())) {
                //Se obtiene el lenguaje de la entrada
                AbilityDetail_Language__3 lang = listOfLanguajes.get(pos).getLanguage();

                if (lang.getName().equals(systemLanguaje)) {
                    translatedAbility = listOfLanguajes.get(pos).getName();
                    hasEntrie = true;
                }
                pos++;
            }
            result.add(translatedAbility);
        }

        return result;
    }
    
    private ArrayList<String> getTranslatedTypes(){
        ArrayList<String> result = new ArrayList<>();

        result.add(getString(R.string.type_steel));
        result.add(getString(R.string.type_water));
        result.add(getString(R.string.type_bug));
        result.add(getString(R.string.type_dragon));
        result.add(getString(R.string.type_electric));
        result.add(getString(R.string.type_normal));
        result.add(getString(R.string.type_fire));
        result.add(getString(R.string.type_grass));
        result.add(getString(R.string.type_ice));
        result.add(getString(R.string.type_fighting));
        result.add(getString(R.string.type_poison));
        result.add(getString(R.string.type_ground));
        result.add(getString(R.string.type_flying));
        result.add(getString(R.string.type_psychic));
        result.add(getString(R.string.type_rock));
        result.add(getString(R.string.type_ghost));
        result.add(getString(R.string.type_dark));
        result.add(getString(R.string.type_fairy));
        
        return result;
    }
}
