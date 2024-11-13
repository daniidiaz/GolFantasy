package com.example.proyecto;

import java.util.ArrayList;

//Clase para las ligas creadas por el usuario dentro del juego
public class LigaFicticia {
    private final String nombre;
    private String idLiga;
    private ArrayList<Usuario> usuarios;
    private Usuario admin;


    public LigaFicticia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
