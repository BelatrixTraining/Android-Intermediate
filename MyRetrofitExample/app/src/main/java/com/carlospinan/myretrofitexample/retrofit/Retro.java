package com.carlospinan.myretrofitexample.retrofit;

import com.carlospinan.myretrofitexample.retrofit.api.PokemonApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Carlos Piñan
 */

public class Retro {

    public static PokemonApi getPokemonApi() {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        /*
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return null;
            }
        });
        */
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("URL GOES HERE")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .build();
        return retrofit.create(PokemonApi.class);
    }
}
