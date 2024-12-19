package com.example.proyecto;

public class EquipoLaLiga {

    private String nombre;  // Corresponde al campo "nombre" en Firestore
    private String escudo;  // Corresponde al campo "escudo" en Firestore

    // Constructor vacío necesario para Firestore
    public EquipoLaLiga() {
    }

    // Constructor con parámetros (opcional)
    public EquipoLaLiga(String nombre, String escudo) {
        this.nombre = nombre;
        this.escudo = escudo;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEscudo() {
        return escudo;
    }

    public void setEscudo(String escudo) {
        this.escudo = escudo;
    }
}
