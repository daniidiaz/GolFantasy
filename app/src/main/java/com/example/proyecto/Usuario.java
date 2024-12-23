package com.example.proyecto;


import android.widget.ImageView;

import java.util.ArrayList;

/*Clase que representa a los usuarios de la aplicación.*/
public class Usuario {
    private String idUsuario;
    private String nombreUsuario;
    private String contrasenia;
    private String correo;
    private String telefono;
    private int puntuacion;
    private String equipoFavorito;
   // private ArrayList<String> ligasCreadas;
   // private ArrayList<String> ligasUnidas;

    public Usuario(String nombreUsuario, String contrasenia, String correo, String telefono, String equipoFavorito) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.correo = correo;
        this.telefono = telefono;
        this.equipoFavorito = equipoFavorito;
        this.puntuacion = 0;
      //  this.ligasCreadas = new ArrayList<>();
       // this.ligasUnidas = new ArrayList<>();
    }
    public Usuario() {
        // Constructor vacío requerido por Firebase
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
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
    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

   /* public ArrayList<String> getLigasCreadas() {
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
    }*/

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getEquipoFavorito() {
        return equipoFavorito;
    }

    public void setEquipoFavorito(String equipoFavorito) {
        this.equipoFavorito = equipoFavorito;
    }

}
