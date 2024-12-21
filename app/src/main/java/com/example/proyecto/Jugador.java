package com.example.proyecto;

import com.google.firebase.firestore.DocumentReference;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Jugador {
    private String idJugador; // ID único del jugador
    private String nombre; // Nombre completo del jugador
    private int puntuacion; // Puntuación acumulada del jugador
    private int precio; // Precio del jugador
    private DisponibilidadJugador disponibilidad; // Disponibilidad del jugador (enum o clase)
    private Posicion posicion; // Posición del jugador (enum o clase)
    private DocumentReference equipo; // Equipo al que pertenece
    private int edad; // Edad del jugador
    private String nacionalidad; // Nacionalidad del jugador
    private String imagenUrl; // URL de la imagen del jugador
    private int partidosJugados;
    private int goles;
    private int asistencias;
    private int tarjetasAmarillas;
    private int tarjetasRojas;

    // Constructor vacío
    public Jugador() {
    }

    // Constructor con parámetros
    public Jugador(String idJugador, String nombre, int puntuacion, int precio, DisponibilidadJugador disponibilidad, Posicion posicion, DocumentReference  equipo, int edad, String nacionalidad, String imagenUrl, int partidosJugados, int goles, int asistencias, int tarjetasAmarillas, int tarjetasRojas) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.puntuacion = puntuacion;
        this.precio = precio;
        this.disponibilidad = disponibilidad;
        this.posicion = posicion;
        this.equipo = equipo;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
        this.imagenUrl = imagenUrl;
        this.partidosJugados = partidosJugados;
        this.goles = goles;
        this.asistencias = asistencias;
        this.tarjetasAmarillas = tarjetasAmarillas;
        this.tarjetasRojas = tarjetasRojas;
    }


    //getter y setter
    public String getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(String idJugador) {
        this.idJugador = idJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public DisponibilidadJugador getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(DisponibilidadJugador disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public DocumentReference  getEquipo() {
        return equipo;
    }

    public void setEquipo(DocumentReference  equipo) {
        this.equipo = equipo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = goles;
    }

    public int getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(int asistencias) {
        this.asistencias = asistencias;
    }

    public int getTarjetasAmarillas() {
        return tarjetasAmarillas;
    }

    public void setTarjetasAmarillas(int tarjetasAmarillas) {
        this.tarjetasAmarillas = tarjetasAmarillas;
    }

    public int getTarjetasRojas() {
        return tarjetasRojas;
    }

    public void setTarjetasRojas(int tarjetasRojas) {
        this.tarjetasRojas = tarjetasRojas;
    }
}
