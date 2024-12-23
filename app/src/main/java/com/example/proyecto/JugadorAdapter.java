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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JugadorAdapter extends RecyclerView.Adapter<JugadorAdapter.ViewHolder> {

    private List<Jugador> listaJugadores;
    private List<Jugador> listaFiltrada;
    private String idUsuario;
    private MercadoFragment mercadoFragment;

    public JugadorAdapter(List<Jugador> listaJugadores, String idUsuario, MercadoFragment mercadoFragment) {
        this.listaJugadores = listaJugadores;
        this.listaFiltrada = new ArrayList<>(listaJugadores);
        this.idUsuario = idUsuario;
        this.mercadoFragment = mercadoFragment;
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

        holder.textViewNombre.setText(jugador.getNombre());
        holder.textViewPuntuacion.setText("Puntos: " + jugador.getPuntuacion());
        holder.textViewPrecio.setText("Precio: " + jugador.getPrecio());

        Glide.with(holder.itemView.getContext())
                .load(jugador.getImagenUrl())
                .into(holder.imageViewJugador);

        holder.buttonFichar.setOnClickListener(v -> {
            // Verificar si el jugador ya está fichado
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("formaciones").document(idUsuario)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            List<Map<String, Object>> jugadoresArray = (List<Map<String, Object>>) documentSnapshot.get("jugadores");
                            boolean jugadorRepetido = false;
                            if (jugadoresArray != null) {
                                for (Map<String, Object> jugadorData : jugadoresArray) {
                                    if (jugadorData.get("idJugador").equals(jugador.getIdJugador())) {
                                        jugadorRepetido = true;
                                        break;
                                    }
                                }
                            }

                            if (jugadorRepetido) {
                                // Si el jugador está repetido, mostrar un Toast y no realizar ninguna acción
                                Toast.makeText(holder.itemView.getContext(), "Este jugador ya está fichado", Toast.LENGTH_SHORT).show();
                            } else {
                                // Si el jugador no está repetido, mostrar el diálogo de confirmación
                                mercadoFragment.mostrarDialogoConfirmacion(jugador, () -> {
                                    mercadoFragment.registrarFichaje(jugador);

                                    // Realizar el fichaje aquí si se confirma
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

                                                        agregarJugadorAlEquipo(jugador, idUsuario, holder);
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
                        } else {
                            // Si el documento no existe, mostrar el diálogo de confirmación (el jugador no está fichado)
                            mercadoFragment.mostrarDialogoConfirmacion(jugador, () -> {
                                mercadoFragment.registrarFichaje(jugador);

                                // Realizar el fichaje aquí si se confirma
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

                                                    agregarJugadorAlEquipo(jugador, idUsuario, holder);
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
                    })
                    .addOnFailureListener(e -> {
                        Log.e("JugadorAdapter", "Error al verificar si el jugador está fichado", e);
                    });
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

    private void agregarJugadorAlEquipo(Jugador jugador, String idUsuario, ViewHolder holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Verificar si el documento del usuario ya existe en la colección "formaciones"
        db.collection("formaciones").document(idUsuario)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Si el documento existe, agregamos el jugador al arreglo
                        db.collection("formaciones").document(idUsuario)
                                .update("jugadores", FieldValue.arrayUnion(jugador))
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(holder.itemView.getContext(), "Jugador fichado con éxito", Toast.LENGTH_SHORT).show();
                                    Log.d("JugadorAdapter", "Jugador añadido correctamente a formaciones.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("JugadorAdapter", "Error al agregar jugador al equipo", e);
                                });
                    } else {
                        // Si el documento no existe, lo creamos y añadimos el jugador
                        Formacion formacion = new Formacion(idUsuario, new ArrayList<Jugador>());
                        db.collection("formaciones").document(idUsuario)
                                .set(formacion)
                                .addOnSuccessListener(aVoid -> {
                                    // Ahora agregamos el jugador al arreglo
                                    db.collection("formaciones").document(idUsuario)
                                            .update("jugadores", FieldValue.arrayUnion(jugador))
                                            .addOnSuccessListener(aVoid1 -> {
                                                Toast.makeText(holder.itemView.getContext(), "Jugador fichado con éxito", Toast.LENGTH_SHORT).show();
                                                Log.d("JugadorAdapter", "Jugador añadido a nueva formacion.");
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("JugadorAdapter", "Error al agregar jugador al equipo en nueva formacion", e);
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("JugadorAdapter", "Error al crear documento de formaciones", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("JugadorAdapter", "Error al verificar documento de formaciones", e);
                });
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

