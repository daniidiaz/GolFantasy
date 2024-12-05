package com.example.proyecto;


import android.widget.ImageView;

import java.util.ArrayList;

/*Clase que representa a los usuarios de la aplicación.*/
public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasenia;
    private String correo;


    private String telefono;
    private String equipoFavorito;
    private ArrayList<String> ligasCreadas;
    private ArrayList<String> ligasUnidas;

    public Usuario(String nombreUsuario, String contrasenia, String correo, String telefono, String equipoFavorito) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.correo = correo;
        this.telefono = telefono;
        this.equipoFavorito = equipoFavorito;
        this.ligasCreadas = new ArrayList<>();
        this.ligasUnidas = new ArrayList<>();
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public ArrayList<String> getLigasCreadas() {
        return ligasCreadas;
    }

    public void setLigasCreadas(ArrayList<String> ligasCreadas) {
        this.ligasCreadas = ligasCreadas;
    }

    public ArrayList<String> getLigasUnidas() {
        return ligasUnidas;
    }

    public void setLigasUnidas(ArrayList<String> ligasUnidas) {
        this.ligasUnidas = ligasUnidas;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
