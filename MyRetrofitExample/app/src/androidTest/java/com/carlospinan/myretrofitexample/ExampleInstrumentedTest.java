package com.carlospinan.myretrofitexample;

import android.support.test.runner.AndroidJUnit4;

import com.carlospinan.myretrofitexample.models.Pokemon;
import com.carlospinan.myretrofitexample.retrofit.Retro;
import com.carlospinan.myretrofitexample.retrofit.api.PokemonApi;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void retrofitDemo() throws Exception {
        // Context appContext = InstrumentationRegistry.getTargetContext();
        PokemonApi pokemonApi = Retro.getPokemonApi();
        Call<List<Pokemon>> call = pokemonApi.getPokemons("desc");
        Response<List<Pokemon>> execute = call.execute();
        /*
        call.enqueue(new Callback<List<Pokemon>>() {
            @Override
            public void onResponse(Call<List<Pokemon>> call, Response<List<Pokemon>> response) {

            }

            @Override
            public void onFailure(Call<List<Pokemon>> call, Throwable t) {

            }
        });*/

    }
}
