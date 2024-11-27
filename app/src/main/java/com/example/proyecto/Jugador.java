package com.example.proyecto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Jugador {
    private int idJugador; // ID único del jugador
    private String nombre; // Nombre completo del jugador
    private int puntuacion; // Puntuación acumulada del jugador
    private int precio; // Precio del jugador
    private DisponibilidadJugador disponibilidad; // Disponibilidad del jugador (enum o clase)
    private Posicion posicion; // Posición del jugador (enum o clase)
    private EquipoLaLiga equipo; // Equipo al que pertenece
    private Usuario propietario; // Usuario propietario del jugador (fantasy)
    private int edad; // Edad del jugador
    private String nacionalidad; // Nacionalidad del jugador
    private double mediaPuntuacion; // Media de puntuación en la temporada
    private String imagenUrl; // URL de la imagen del jugador
    private ArrayList<Partido> partidosJugados; // Lista de partidos jugados
    private ArrayList<Partido> partidosPendientes; // Lista de partidos pendientes

    // Constructor vacío
    public Jugador() {
    }

    // Constructor con parámetros
    public Jugador(int idJugador, String nombre, int puntuacion, int precio,
                   DisponibilidadJugador disponibilidad, Posicion posicion,
                   EquipoLaLiga equipo, Usuario propietario, int edad,
                   String nacionalidad, double mediaPuntuacion, String imagenUrl,
                   ArrayList<Partido> partidosJugados, ArrayList<Partido> partidosPendientes) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.puntuacion = puntuacion;
        this.precio = precio;
        this.disponibilidad = disponibilidad;
        this.posicion = posicion;
        this.equipo = equipo;
        this.propietario = propietario;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
        this.mediaPuntuacion = mediaPuntuacion;
        this.imagenUrl = imagenUrl;
        this.partidosJugados = partidosJugados;
        this.partidosPendientes = partidosPendientes;
    }

    // Getters y setters
    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
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

    public EquipoLaLiga getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoLaLiga equipo) {
        this.equipo = equipo;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
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

    public double getMediaPuntuacion() {
        return mediaPuntuacion;
    }

    public void setMediaPuntuacion(double mediaPuntuacion) {
        this.mediaPuntuacion = mediaPuntuacion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public ArrayList<Partido> getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(ArrayList<Partido> partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public ArrayList<Partido> getPartidosPendientes() {
        return partidosPendientes;
    }

    public void setPartidosPendientes(ArrayList<Partido> partidosPendientes) {
        this.partidosPendientes = partidosPendientes;
    }
}
