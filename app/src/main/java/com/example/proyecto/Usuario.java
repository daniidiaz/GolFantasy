package com.example.proyecto;


import android.widget.ImageView;

import java.util.ArrayList;

/*Clase que representa a los usuarios de la aplicación.*/
public class Usuario {
    private int idUsuario;
    private String nickname;
    private ImageView avatar;
    private boolean esAdmin = false;
    private int puntuacionTotal;
    private int saldo;
    private ArrayList<Jugador> jugadores;
    private int nJugadores;
    private ArrayList<Jornada> jornadas;
    private int posicion;
    private int valorEquipo;


}
