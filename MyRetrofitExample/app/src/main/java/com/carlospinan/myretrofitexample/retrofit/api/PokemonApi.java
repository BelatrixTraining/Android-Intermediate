package com.carlospinan.myretrofitexample.retrofit.api;

import com.carlospinan.myretrofitexample.models.Pokemon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Carlos Piñan
 */

public interface PokemonApi {

    @GET("pokemon/list")
    Call<List<Pokemon>> getPokemons(@Query("sort") String sortType);

    @GET("pokemon/{pokemonId}")
    Call<Pokemon> getPokemon(@Path("pokemonId") int pokemonId);

    @POST("pokemon/create")
    Call<Pokemon> createPokemon(@Body Pokemon pokemon);
}
