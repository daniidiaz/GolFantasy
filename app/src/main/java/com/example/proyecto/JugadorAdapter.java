package com.example.proyecto;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class JugadorAdapter extends RecyclerView.Adapter<JugadorAdapter.ViewHolder> {

    private List<Jugador> listaJugadores;
    private List<Jugador> listaFiltrada;
    private String idUsuario;

    public JugadorAdapter(List<Jugador> listaJugadores, String idUsuario) {
        this.listaJugadores = listaJugadores;
        this.listaFiltrada = new ArrayList<>(listaJugadores);
        this.idUsuario = idUsuario;

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
            int precioJugador = jugador.getPrecio();
            ControladorBBDD controladorBBDD = new ControladorBBDD();
            controladorBBDD.getPresupuestoDeUsuario(idUsuario, new ControladorBBDD.PresupuestoCallback() {
                @Override
                public void onSuccess(int presupuesto) {
                    if (presupuesto >= precioJugador) {
                        // Realizar la compra
                        controladorBBDD.actualizarPresupuesto(idUsuario, presupuesto - precioJugador, new ControladorBBDD.PresupuestoActualizadoCallback() {
                            @Override
                            public void onPresupuestoActualizado() {
                                // Actualizar el presupuesto en el Toolbar
                                if (holder.itemView.getContext() instanceof PantallaJuegoPrincipal) {
                                    ((PantallaJuegoPrincipal) holder.itemView.getContext()).actualizarPresupuestoEnToolbar(idUsuario);
                                }
                                // Mostrar mensaje de éxito
                                Toast.makeText(holder.itemView.getContext(), "Jugador fichado con éxito", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("JugadorAdapter", "Error al actualizar presupuesto: " + e.getMessage());
                            }
                        });
                    } else {
                        // Mostrar mensaje de error
                        Toast.makeText(holder.itemView.getContext(), "No tienes suficiente presupuesto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e("JugadorAdapter", "Error al obtener presupuesto: " + e.getMessage());
                }
            });
        });



    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public void filtrar(String texto) {
        listaFiltrada.clear();
        if (texto.isEmpty() && listaJugadores.isEmpty()) {
            // If search is empty and listaJugadores is empty, display nothing
            return;
        } else if (texto.isEmpty()) {
            // If search is empty, display all players
            listaFiltrada.addAll(listaJugadores);
        } else {
            // If search is not empty, filter players
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

    public List<Jugador> getListaJugadores() {
        return listaJugadores;
    }

    public void setListaJugadores(List<Jugador> listaJugadores) {
        this.listaJugadores = listaJugadores;
    }



}
