package netAccess;

import model.pokemonModel.Pokemon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestService {

    @GET("pokemon/{id}")
    Call<Pokemon> getPokemon(@Path("id") int pokedexNumber);
}
