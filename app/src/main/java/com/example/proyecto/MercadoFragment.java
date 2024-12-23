package com.example.proyecto;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MercadoFragment extends Fragment {

    private RecyclerView recyclerViewJugadores;
    private SearchView searchViewJugadores;
    private JugadorAdapter jugadorAdapter;
    private List<Jugador> listaJugadores = new ArrayList<>();
    private FirebaseFirestore db;
    private String idUsuario; // Declaramos la variable idUsuario

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mercado, container, false);

        // Recuperar el ID del usuario del Bundle
        if (getArguments() != null) {
            idUsuario = getArguments().getString("usuarioId");
        }

        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializar vistas
        recyclerViewJugadores = rootView.findViewById(R.id.recyclerViewJugadores);
        searchViewJugadores = rootView.findViewById(R.id.searchViewJugadores);

        // Configurar RecyclerView
        recyclerViewJugadores.setLayoutManager(new LinearLayoutManager(getContext()));
        jugadorAdapter = new JugadorAdapter(listaJugadores, idUsuario, this); // Pasamos la referencia al fragmento
        recyclerViewJugadores.setAdapter(jugadorAdapter);

        // Cargar jugadores de Firebase
        cargarJugadoresDeFirebase();

        // Configurar búsqueda
        configurarBarraBusqueda();

        return rootView;
    }

    private void cargarJugadoresDeFirebase() {
        db.collection("jugadores")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    listaJugadores.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Jugador jugador = document.toObject(Jugador.class);
                        jugador.setIdJugador(document.getId());
                        jugador.setPuntuacion(calcularPuntuacion(jugador));
                        listaJugadores.add(jugador);
                    }
                    Log.d("MercadoFragment", "Número de jugadores obtenidos: " + listaJugadores.size());

                    // Trigger initial display of all players
                    jugadorAdapter.filtrar("");
                    jugadorAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("MercadoFragment", "Error al cargar jugadores", e));
    }

    private void configurarBarraBusqueda() {
        searchViewJugadores.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No realizar acción al enviar
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                jugadorAdapter.filtrar(newText); // Filtrar jugadores
                return true;
            }
        });
    }

    private int calcularPuntuacion(Jugador jugador) {
        // Ejemplo de cálculo de puntuación basado en condiciones
        int goles = jugador.getGoles();
        int asistencias = jugador.getAsistencias();
        int amarillas = jugador.getTarjetasAmarillas();
        int rojas = jugador.getTarjetasRojas();
        int partidosJugados = jugador.getPartidosJugados();

        return (partidosJugados * 1) + (goles * 4) + (asistencias * 3) - (amarillas * 1) - (rojas * 3);
    }

    public void mostrarDialogoConfirmacion(Jugador jugador, Runnable onConfirmar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar fichaje");
        builder.setMessage("¿Estás seguro de que quieres fichar a " + jugador.getNombre() + "?");
        builder.setPositiveButton("Sí", (dialog, which) -> onConfirmar.run());
        builder.setNegativeButton("No", null);
        builder.show();
    }


    public void registrarFichaje(Jugador jugador) {
        // Crear un mapa con los datos del fichaje
        Map<String, Object> fichaje = new HashMap<>();
        fichaje.put("idUsuario", idUsuario);
        fichaje.put("idJugador", jugador.getIdJugador());
        fichaje.put("nombreJugador", jugador.getNombre());
        fichaje.put("fecha", FieldValue.serverTimestamp());

        // Guardar en Firebase
        db.collection("fichajes")
                .add(fichaje)
                .addOnSuccessListener(documentReference -> {
                    //Toast.makeText(getContext(), "¡Has fichado a " + jugador.getNombre() + "!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al fichar jugador: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
