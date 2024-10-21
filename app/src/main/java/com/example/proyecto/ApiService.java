package com.example.proyecto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("v2/everything")
    Call<ApiResponse> obtenerNoticias(
            @Query("q") String query,
            @Query("apiKey") String apiKey,
            @Query("language") String language
    );
}
