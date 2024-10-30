package com.example.proyecto;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.NoticiaViewHolder>  {

    private List<Noticia> noticias;

    public NoticiasAdapter(List<Noticia> noticias) {
        this.noticias = noticias;
    }

    //Usa el layout item_noticia.xml
    @NonNull
    @Override
    public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noticia, parent, false);
        return new NoticiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaViewHolder holder, int position) {
        Noticia noticia = noticias.get(position);
        Log.d("NoticiasAdapter", "Asignando noticia: " + noticia.getTitulo());

        holder.tituloTextView.setText(noticia.getTitulo());
        holder.descripcionTextView.setText(noticia.getDescripcion());
        holder.contenidoTextView.setText(noticia.getContenido());
        holder.autorTextView.setText(noticia.getAutor());
        holder.urlTextView.setText(noticia.getUrl());

        // Cargar la imagen usando Glide
        Glide.with(holder.imagenView.getContext())
                .load(noticia.getUrlDeImagen())
                .into(holder.imagenView);

        // Configura el TextView de la URL y su clic
        holder.urlTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(noticia.getUrl()));
            v.getContext().startActivity(browserIntent);
        });
    }

    //método que muestra el numero de noticias que se van a mostrar
    @Override
    public int getItemCount() {
        return noticias.size();
    }

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView;
        TextView descripcionTextView;
        TextView contenidoTextView;
        TextView autorTextView;
        TextView urlTextView;
        ImageView imagenView;

        public NoticiaViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.titulo);
            descripcionTextView = itemView.findViewById(R.id.descripcion);
            contenidoTextView = itemView.findViewById(R.id.contenido);
            autorTextView = itemView.findViewById(R.id.autor);
            urlTextView = itemView.findViewById(R.id.url);
            imagenView = itemView.findViewById(R.id.image_noticia);

            // Log para verificar si los TextViews son null
            Log.d("NoticiaViewHolder", "Titulo TextView: " + (tituloTextView != null));
            Log.d("NoticiaViewHolder", "Descripcion TextView: " + (descripcionTextView != null));
            Log.d("NoticiaViewHolder", "Contenido TextView: " + (contenidoTextView != null));
            Log.d("NoticiaViewHolder", "Autor TextView: " + (autorTextView != null));
            Log.d("NoticiaViewHolder", "URL TextView: " + (urlTextView != null));
            Log.d("NoticiaViewHolder", "Imagen View: " + (imagenView != null));
        }
    }
}
