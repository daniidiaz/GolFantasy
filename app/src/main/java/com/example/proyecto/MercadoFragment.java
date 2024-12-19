package com.example.proyecto;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MercadoFragment extends Fragment {

    private RecyclerView recyclerViewJugadores;
    private SearchView searchViewJugadores;
    private JugadorAdapter jugadorAdapter;
    private List<Jugador> listaJugadores = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mercado, container, false);

        // Inicializar vistas
        recyclerViewJugadores = rootView.findViewById(R.id.recyclerViewJugadores);
        searchViewJugadores = rootView.findViewById(R.id.searchViewJugadores);

        // Configurar RecyclerView
        recyclerViewJugadores.setLayoutManager(new LinearLayoutManager(getContext()));
        jugadorAdapter = new JugadorAdapter(listaJugadores);
        recyclerViewJugadores.setAdapter(jugadorAdapter);

        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();

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
                        jugador.setPuntuacion(calcularPuntuacion(jugador)); // Calcular la puntuación
                        listaJugadores.add(jugador);
                    }
                    Log.d("MercadoFragment", "Número de jugadores obtenidos: " + listaJugadores.size()); // Añadir log
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

        return (goles * 4) + (asistencias * 3) - (amarillas * 1) - (rojas * 3);
    }
}
