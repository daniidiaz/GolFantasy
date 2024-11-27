package com.example.proyecto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeSoccerApiClient {
    private static final String BASE_URL = "https://api.besoccer.com/";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation() // Excluye campos no anotados
                    .setLenient() // Permite deserializar JSON flexible
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Cambia por la URL de tu API
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
