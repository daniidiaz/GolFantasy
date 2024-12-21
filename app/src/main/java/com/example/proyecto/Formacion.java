package com.example.proyecto;

import java.util.List;

public class Formacion {
    private String idUsuario;
    private List<Jugador> jugadores;

    public Formacion(String idUsuario, List<Jugador> jugadores) {
        this.idUsuario = idUsuario;
        this.jugadores = jugadores;
    }

    // Getters y setters
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }
}
