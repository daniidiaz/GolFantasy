package com.example.proyecto;

public class Noticia {

    private String titulo;         // Título de la noticia
    private String descripcion;    // Descripción de la noticia
    private String url;            // URL del artículo
    private String urlDeImagen;    // URL de la imagen asociada a la noticia
    private String contenido;       // Contenido completo de la noticia
    private String autor;          // Autor de la noticia

    // Constructor
    public Noticia(String titulo, String descripcion, String url, String urlDeImagen, String contenido, String autor) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.urlDeImagen = urlDeImagen;
        this.contenido = contenido;
        this.autor = autor;
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlDeImagen() {
        return urlDeImagen;
    }

    public void setUrlDeImagen(String urlDeImagen) {
        this.urlDeImagen = urlDeImagen;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
