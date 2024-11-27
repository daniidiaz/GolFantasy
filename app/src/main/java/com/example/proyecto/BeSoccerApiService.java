package com.example.proyecto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BeSoccerApiService {
    @GET("soccer/players")
    Call<BeSoccerResponse> obtenerJugadoresDeLiga(
            @Query("apikey") String apiKey,
            @Query("season") String temporada,
            @Query("country") String pais
    );

}
