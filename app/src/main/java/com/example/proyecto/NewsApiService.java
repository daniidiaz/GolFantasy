package com.example.proyecto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("v2/everything")       //solicitud HTTP de tipo GET
    Call<ApiResponse> obtenerNoticias(      //llamada HTTP a la API
            @Query("q") String query,       //palabra para buscar noticias relacionadas en la API (Por ejemplo: football, fútbol, laliga)
            @Query("apiKey") String apiKey,     //clave que nos pasan desde la api para poder acceder a ella
            @Query("language") String language      //lenguaje en el que queremos que nos muestren las noticias en nuestro caso el español
    );
}
