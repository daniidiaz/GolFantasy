package com.example.proyecto;

import java.util.List;

//Clase que obtiene una lista de articulos de noticias desde la API de NewsApi

public class ApiResponse {
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }
}

