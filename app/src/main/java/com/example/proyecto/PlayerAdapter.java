package com.example.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private Context context;
    private List<Map<String, Object>> players;

    public PlayerAdapter(Context context, List<Map<String, Object>> players) {
        this.context = context;
        this.players = players;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewJugador;
        public TextView textViewNombre;
        public TextView textViewPuntuacion;
        public TextView textViewPrecio;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewJugador = itemView.findViewById(R.id.imageViewJugador);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewPuntuacion = itemView.findViewById(R.id.textViewPuntuacion);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecio);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.player_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> currentPlayer = players.get(position);

        String nombre = (String) currentPlayer.get("nombre");
        Long puntuacion = (Long) currentPlayer.get("puntuacion");
        Long precio = (Long) currentPlayer.get("precio");
        String urlImagen = (String) currentPlayer.get("imagenUrl");

        holder.textViewNombre.setText(nombre);
        holder.textViewPuntuacion.setText("Puntuación: " + puntuacion);
        holder.textViewPrecio.setText("Precio: " + precio);

        Glide.with(context)
                .load(urlImagen)
                .placeholder(R.drawable.placeholder) // Placeholder image
                .error(R.drawable.error_image) // Error image
                .into(holder.imageViewJugador);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}