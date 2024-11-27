package com.example.proyecto;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;   //esta biblioteca convierte los JSON en objetos Java

public class NewsApiClient {
    private static final String BASE_URL = "https://newsapi.org/";  //URL para la solicitudes de la API de noticias
    private static Retrofit retrofit = null;

    //método que comprueba si Retrofit es null
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder() //si es null crea una nueva instancía
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //convierte las respuestas JSON en objetos Java
                    .build();
        }
        return retrofit;
    }
}
