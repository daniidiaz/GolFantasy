package com.example.proyecto;

//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.proyecto.Jugador;
//import com.example.proyecto.JugadorAdapter;
//import com.example.proyecto.R;
//import com.google.firebase.firestore.DocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MercadoFragment extends Fragment {
//
//    private RecyclerView recyclerViewMercado;
//    private JugadorAdapter jugadorAdapter;
//    private List<Jugador> jugadoresDisponibles = new ArrayList<>();

//    // Método que obtiene los jugadores disponibles para una liga
//    public void obtenerJugadoresDisponibles(String idLiga) {
//        db.collection("jugadores")
//                .whereEqualTo("propietarios." + idLiga, null) // Filtra jugadores sin propietario en la liga
//                .get()
//                .addOnSuccessListener(querySnapshot -> {
//                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
//                        // Crear el objeto Jugador con los datos obtenidos
//                        Jugador jugador = document.toObject(Jugador.class);
//                        jugadoresDisponibles.add(jugador);
//                    }
//                    // Mostrar los jugadores en el RecyclerView
//                    mostrarJugadoresEnRecyclerView(jugadoresDisponibles);
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("MercadoFragment", "Error al obtener jugadores disponibles", e);
//                });
//    }
//
//    // Método para configurar el RecyclerView
//    private void mostrarJugadoresEnRecyclerView(List<Jugador> jugadores) {
//        jugadorAdapter = new JugadorAdapter(getContext(), jugadores);
//        recyclerViewMercado.setAdapter(jugadorAdapter);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_mercado, container, false);
//        recyclerViewMercado = rootView.findViewById(R.id.recyclerViewMercado);
//        recyclerViewMercado.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // Llamar al método para obtener los jugadores disponibles
//        String idLiga = "some_liga_id"; // El ID de la liga, que debe pasarse correctamente
//        obtenerJugadoresDisponibles(idLiga);
//
//        return rootView;
//    }
//}
