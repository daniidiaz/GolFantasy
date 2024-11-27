package com.example.proyecto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BeSoccerResponse {

    @SerializedName("players")
    private List<Jugador> jugadores;

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }
}

