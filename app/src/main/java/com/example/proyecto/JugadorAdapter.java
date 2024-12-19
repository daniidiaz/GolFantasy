package com.example.proyecto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class JugadorAdapter extends RecyclerView.Adapter<JugadorAdapter.ViewHolder> {

    private List<Jugador> listaJugadores;
    private List<Jugador> listaFiltrada;

    public JugadorAdapter(List<Jugador> listaJugadores) {
        this.listaJugadores = listaJugadores;
        this.listaFiltrada = new ArrayList<>(listaJugadores);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jugador_mercado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Jugador jugador = listaFiltrada.get(position);

        // Mostrar datos del jugador
        holder.textViewNombre.setText(jugador.getNombre());
        holder.textViewPuntuacion.setText("Puntos: " + jugador.getPuntuacion());
        holder.textViewPrecio.setText("Precio: " + jugador.getPrecio());

        // Cargar imagen con Glide
        Glide.with(holder.itemView.getContext())
                .load(jugador.getImagenUrl())
                .into(holder.imageViewJugador);

        holder.buttonFichar.setOnClickListener(v -> {
            // Acciones al pulsar el botón "Fichar"
        });
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public void filtrar(String texto) {
        listaFiltrada.clear();
        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaJugadores);
        } else {
            for (Jugador jugador : listaJugadores) {
                if (jugador.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                    listaFiltrada.add(jugador);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewPuntuacion, textViewPrecio;
        ImageView imageViewJugador;
        Button buttonFichar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewPuntuacion = itemView.findViewById(R.id.textViewPuntuacion);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecio);
            imageViewJugador = itemView.findViewById(R.id.imageViewJugador);
            buttonFichar = itemView.findViewById(R.id.buttonFichar);
        }
    }
}
