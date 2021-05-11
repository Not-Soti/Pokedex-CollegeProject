package com.example.pokedex.presentation;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pokedex.R;
import com.example.pokedex.model.pokeApiModel.FlavorTextEntry;
import com.example.pokedex.model.pokeApiModel.Language;
import com.example.pokedex.model.pokeApiModel.PokemonSpeciesDetail;
import com.example.pokedex.model.pokeApiModel.Stat;
import com.example.pokedex.model.pokeApiModel.Stat__1;
import com.example.pokedex.model.pokeApiModel.Type;
import com.example.pokedex.netAccess.RestService;
import com.example.pokedex.model.pokeApiModel.Pokemon;
import com.example.pokedex.presentation.Pokedex.PokedexViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PokemonDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokemonDetailFragment extends Fragment {

    public static final String PARAM_POKEMON_ID = "pokemon_id";
    private final String TAG = "PokemonDetailFragment";
    private int pokemonID;

    private ImageView pokemonSprite;
    private TextView nameTv, idTv;
    private TextView flavorTextTv;
    private TextView weighTv, heightTv;

    private TextView type1Tv, type2Tv;
    
    private TextView healthTv, attackTv, speAttackTv, defenseTv, speDefenseTv, speedTv;
    private ProgressBar healthBar, attackBar, speAttackBar, defenseBar, speDefenseBar, speedBar;

    private String systemLanguaje; //Campo que indica el lenguaje del sistema, usado para obtener
                                   //la descripción en el idioma correspondiente

    public PokemonDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pokemonID PokemonID number to represent in the fragment
     * @return A new instance of fragment PokemonDetailFragment.
     */
    public static PokemonDetailFragment newInstance(int pokemonID) {
        PokemonDetailFragment fragment = new PokemonDetailFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_POKEMON_ID, pokemonID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pokemonID = getArguments().getInt(PARAM_POKEMON_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_pokemon_detail, container, false);

        pokemonSprite = theView.findViewById(R.id.poke_detail_sprite);
        nameTv = theView.findViewById(R.id.poke_detail_nameTv);
        idTv = theView.findViewById(R.id.poke_detail_idTv);
        flavorTextTv = theView.findViewById(R.id.poke_detail_flavorTextTv);
        weighTv = theView.findViewById(R.id.poke_detail_weightTv);
        heightTv = theView.findViewById(R.id.poke_detail_heightTv);

        type1Tv = theView.findViewById(R.id.poke_detail_type1Tv);
        type2Tv = theView.findViewById(R.id.poke_detail_type2Tv);
        
        healthTv = theView.findViewById(R.id.poke_detail_healthTv);
        attackTv = theView.findViewById(R.id.poke_detail_attackTv);
        speAttackTv = theView.findViewById(R.id.poke_detail_speAttackTv);
        defenseTv = theView.findViewById(R.id.poke_detail_deffenseTv);
        speDefenseTv = theView.findViewById(R.id.poke_detail_speDeffenseTv);
        speedTv = theView.findViewById(R.id.poke_detail_speedTv);

        healthBar = theView.findViewById(R.id.poke_detail_healthBar);
        attackBar = theView.findViewById(R.id.poke_detail_attackBar);
        speAttackBar = theView.findViewById(R.id.poke_detail_speAttackBar);
        defenseBar = theView.findViewById(R.id.poke_detail_deffenseBar);
        speDefenseBar = theView.findViewById(R.id.poke_detail_speDeffenseBar);
        speedBar = theView.findViewById(R.id.poke_detail_speedBar);

        //Se pone el maximo en cada barra de estadisticas
        healthBar.setMax(255);
        attackBar.setMax(255);
        defenseBar.setMax(255);
        speAttackBar.setMax(255);
        speDefenseBar.setMax(255);
        speedBar.setMax(255);

        //Se obtiene el lenguaje del sistema. Si no es español, se usa ingles por defecto
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals(new Locale("es").getLanguage())){
            systemLanguaje = "es";
        }else{
            systemLanguaje = "en";
        }

        getPokemonInfo();

        return theView;
    }

    private void getPokemonInfo(){
        RestService restService;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        restService = retrofit.create(RestService.class);

        restService.getPokemonById(pokemonID).enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if(isAdded()) {
                    int responseCode = response.code();
                    Log.d(TAG, "Response code: " + responseCode);
                    Pokemon pokemon = response.body();

                    idTv.setText(String.format(Locale.getDefault(), "Nº %d", pokemon.getId()));

                    String name = pokemon.getName();
                    String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1); //Poner la primera letra en mayuscula
                    nameTv.setText(capitalizedName);

                    getFlavorText(pokemon);

                    getPokemonSprite(pokemon);

                    double pokeHeight = ((double) pokemon.getHeight()) / 10;
                    String pokeHeightStr = getResources().getString(R.string.poke_detail_height) + String.format(Locale.getDefault(), " %.02f m", pokeHeight);
                    heightTv.setText(pokeHeightStr);
                    double pokeWeight = ((double) pokemon.getWeight()) / 10;
                    String pokeWeightStr = getResources().getString(R.string.poke_detail_weight) + String.format(Locale.getDefault(), " %.02f Kg", pokeWeight);
                    weighTv.setText(pokeWeightStr);

                    getTypeNames(pokemon);

                    getStats(pokemon);
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                if(isAdded()){

                }
            }
        });
    }

    private void getStats(Pokemon pokemon){
        List<Stat> stats = pokemon.getStats();

        for(Stat stat : stats){
            Stat__1 aux = stat.getStat();
            int numericStat = stat.getBaseStat();
            String numericStatStr = String.valueOf(numericStat);
            switch (aux.getName()){
                case "hp":
                    healthTv.setText(numericStatStr);
                    healthBar.setProgress(numericStat);
                    break;
                case "attack":
                    attackTv.setText(numericStatStr);
                    attackBar.setProgress(numericStat);
                    break;
                case "defense":
                    defenseTv.setText(numericStatStr);
                    defenseBar.setProgress(numericStat);
                    break;
                case "special-attack":
                    speAttackTv.setText(numericStatStr);
                    speAttackBar.setProgress(numericStat);
                    break;
                case "special-defense":
                    speDefenseTv.setText(numericStatStr);
                    speDefenseBar.setProgress(numericStat);
                    break;
                case "speed":
                    speedTv.setText(numericStatStr);
                    speedBar.setProgress(numericStat);
                    break;
            }
        }
    }

    private void getFlavorText(Pokemon pokemon){
        RestService restService;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        restService = retrofit.create(RestService.class);

        //Obtener el id de la especie del pokemon para buscar su descripcion
        //Este id se optiene de la URL del objeto species, en el ultimo campo
        //Ejemplo de dicha url: https://pokeapi.co/api/v2/pokemon-species/1/

        int specie = 0;
        String[] urlParts = pokemon.getSpecies().getUrl().split("/");
        try {
            specie = Integer.parseInt(urlParts[urlParts.length - 1]);
        }catch (NumberFormatException e){
            specie = 0;
        }

        Log.d("AAAAAAAA", "Specie: "+urlParts[urlParts.length - 1]);

        restService.getPokemonSpeciesDetail(specie).enqueue(new Callback<PokemonSpeciesDetail>() {
            @Override
            public void onResponse(Call<PokemonSpeciesDetail> call, Response<PokemonSpeciesDetail> response) {
                PokemonSpeciesDetail detail = response.body();
                String flavor = getContext().getString(R.string.poke_detail_flavorTextUnknown);

                //Si se ha obtenido una respuesta válida se busca la entrada en español
                if(detail!=null) {
                    List<FlavorTextEntry> entries = detail.getFlavorTextEntries();
                    boolean hasEntrie = false;
                    int pos = 0;
                    //Se recorren todas las entradas hasta encontrar la que esta en español
                    while ((!hasEntrie) && (pos < entries.size())) {
                        //Se obtiene el lenguaje de la entrada
                        Language lang = entries.get(pos).getLanguage();

                        if (lang.getName().equals(systemLanguaje)) {
                            flavor = entries.get(pos).getFlavorText();
                            hasEntrie = true;
                        }
                        pos++;
                    }
                }
                flavorTextTv.setText(flavor); //Se pone el texto
            }

            @Override
            public void onFailure(Call<PokemonSpeciesDetail> call, Throwable t) {
                String flavor = getContext().getString(R.string.poke_detail_flavorTextUnknown);
                flavorTextTv.setText(flavor);
            }
        });
    }

    private void getPokemonSprite(Pokemon pokemon){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Log.d(TAG, "Pidiendo sprite del pokemon "+pokemon.getId());
                String spriteURL = pokemon.getSprites().getOther().getOfficialArtwork().getFrontDefault();
                Drawable sprite = null;
                try {
                    InputStream is = (InputStream) new URL(spriteURL).getContent();
                    //Imagen obtenida de internet
                    sprite = Drawable.createFromStream(is, "PokeApi");
                } catch (IOException e) {
                    e.printStackTrace();
                    //Si no se obtiene, se pone la guardada en la aplicacion
                    sprite = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.pokemock, null);
                }

                Drawable finalSprite = sprite;
                Activity act = getActivity();
                if(isAdded() && (act != null))
                    act.runOnUiThread(() -> pokemonSprite.setBackground(finalSprite));

            }
        });
        thread.start();
    }

    private void getTypeNames(Pokemon pokemon){
        //Todos los pokemon tienen 1 tipo, y algunos tienen 2
        //Obtener el nombre del primer tipo
        List<Type> types = pokemon.getTypes();
        Type type1 = types.get(0);
        String type1Name = type1.getType().getName();
        pokemon.type1Str = type1Name;

        //Obtener el segundo, si tiene
        if(types.size()==2){
            Type type2 = types.get(1);
            String type2Name = type2.getType().getName();
            pokemon.type2Str = type2Name;
            //Log.d(TAG,"tipo2 "+ type2Name);
        }else{
            pokemon.type2Str = null;
        }

        PresentationUtils.setTypeTextViewFormat(getContext(), type1Tv, pokemon.type1Str);
        PresentationUtils.setTypeTextViewFormat(getContext(), type2Tv, pokemon.type2Str);
    }
}