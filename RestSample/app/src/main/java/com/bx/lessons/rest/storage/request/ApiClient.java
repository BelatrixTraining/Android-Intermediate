package com.bx.lessons.rest.storage.request;


import com.bx.lessons.rest.storage.entity.LogInRaw;
import com.bx.lessons.rest.storage.entity.LogInResponse;
import com.bx.lessons.rest.storage.entity.NoteRaw;
import com.bx.lessons.rest.storage.entity.NoteResponse;
import com.bx.lessons.rest.storage.entity.NotesResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * Created by em on 8/06/16.
 */
public class ApiClient {

    private static final String TAG = "ApiClient";
    private static final String API_BASE_URL="http://api.backendless.com";

    private static ServicesApiInterface servicesApiInterface;
    private static OkHttpClient.Builder httpClient;


    public static ServicesApiInterface getMyApiClient() {

        if (servicesApiInterface == null) {

            Retrofit.Builder builder =new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            httpClient =new OkHttpClient.Builder();
            httpClient.addInterceptor(interceptor());

            Retrofit retrofit = builder.client(httpClient.build()).build();
            servicesApiInterface = retrofit.create(ServicesApiInterface.class);
        }
        return servicesApiInterface;
    }

    public interface ServicesApiInterface {

        @Headers({
                "Content-Type: application/json",
                "application-id: FF14D613-D0B0-6A6A-FF40-29E7E8E8D100",
                "secret-key: AA499423-00EC-1F0B-FF2E-C78F36087700",
                "application-type: REST"
        })
        //v1/users/login
        @POST("/v1/users/login")
        Call<LogInResponse> login(@Body LogInRaw raw);


        @Headers({
                "Content-Type: application/json",
                "application-id: FF14D613-D0B0-6A6A-FF40-29E7E8E8D100",
                "secret-key: AA499423-00EC-1F0B-FF2E-C78F36087700",
                "application-type: REST"
        })
        //v1/data/Notes
        @GET("/v1/data/Notes")
        Call<NotesResponse> notes();


        @Headers({
                "Content-Type: application/json",
                "application-id: FF14D613-D0B0-6A6A-FF40-29E7E8E8D100",
                "secret-key: AA499423-00EC-1F0B-FF2E-C78F36087700",
                "application-type: REST"
        })
        @POST("/v1/data/Notes")
        Call<NoteResponse> addNote(@Body NoteRaw raw);

    }

    /*private static OkHttpClient.Builder client(){
        if(httpClient==null)httpClient=new OkHttpClient.Builder();
        return httpClient;
    }*/
    private  static  HttpLoggingInterceptor interceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor= new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}
