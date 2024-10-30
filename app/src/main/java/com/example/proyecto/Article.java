package com.example.proyecto;

//Clase para representar los Articulos o noticias de la API

public class Article {
    private String title;           // Título de la noticia
    private String description;     // Descripción de la noticia
    private String url;             // URL del artículo
    private String urlToImage;      // URL de la imagen
    private String content;         // Contenido completo de la noticia
    private String author;          // Autor de la noticia

    // Constructor
    public Article(String title, String description, String url, String urlToImage, String content, String author) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.content = content;
        this.author = author;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
