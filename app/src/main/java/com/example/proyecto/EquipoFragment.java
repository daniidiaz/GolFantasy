package com.example.proyecto;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class EquipoFragment extends Fragment {

    private FirebaseFirestore db;
    private final List<Jugador> jugadoresDefensas = new ArrayList<>();
    private final List<Jugador> jugadoresMediocentros = new ArrayList<>();
    private final List<Jugador> jugadoresDelanteros = new ArrayList<>();

    private List<Jugador> jugadoresLiga;

    private LinearLayout lineaDefensas, lineaMediocentros, lineaDelanteros;
    private ImageView portero;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EquipoFragment() {
        // Required empty public constructor
    }

    public static EquipoFragment newInstance(String param1, String param2) {
        EquipoFragment fragment = new EquipoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        obtenerJugadoresDeFirestore();
    }

    private void obtenerJugadoresDeFirestore() {
        db.collection("formacion") // nombre de colección en Firestore
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Mapea el documento a un objeto Jugador
                            Jugador jugador = document.toObject(Jugador.class);

                            // Clasifica a los jugadores según su posición
                            agregarJugadorPorPosicion(jugador);
                        }
                        // Actualiza la vista con los jugadores obtenidos
                        cambiarFormacion("4-3-3"); // Cambiar por la formación inicial deseada
                    } else {
                        Log.e("Firestore Error", "Error obteniendo documentos: ", task.getException());
                    }
                });
    }

    private void agregarJugadorPorPosicion(Jugador jugador) {
        switch (jugador.getPosicion().name().toLowerCase()) {
            case "portero":
                Glide.with(this)
                        .load(jugador.getImagenUrl())
                        .placeholder(R.drawable.jugador) // Imagen por defecto
                        .into(portero);
                break;
            case "defensa":
                jugadoresDefensas.add(jugador);
                break;
            case "mediocentro":
                jugadoresMediocentros.add(jugador);
                break;
            case "delantero":
                jugadoresDelanteros.add(jugador);
                break;
            default:
                Log.e("Posición inválida", "Posición no reconocida: " + jugador.getPosicion());
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipo, container, false);

        Spinner formacionSpinner = view.findViewById(R.id.spinner_formacion);
        lineaDefensas = view.findViewById(R.id.linea_defensas);
        lineaMediocentros = view.findViewById(R.id.linea_mediocentros);
        lineaDelanteros = view.findViewById(R.id.linea_delanteros);
        portero = view.findViewById(R.id.portero); // Portero fijo

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(),R.array.formaciones, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        formacionSpinner.setAdapter(adapter);

        formacionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String formacionSeleccionada = parent.getItemAtPosition(position).toString();
                cambiarFormacion(formacionSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona nada
            }
        });


        return view;
    }

    private void cambiarFormacion(String formacion) {
        lineaDefensas.removeAllViews();
        lineaMediocentros.removeAllViews();
        lineaDelanteros.removeAllViews();

        // Cambiar la disposición según la formación seleccionada
        if (formacion.equals("3-3-4")) {
            colocarJugadores(3, jugadoresDefensas, lineaDefensas);
            colocarJugadores(3, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(4, jugadoresDelanteros, lineaDelanteros);
        } else if (formacion.equals("3-4-3")) {
            colocarJugadores(3, jugadoresDefensas, lineaDefensas);
            colocarJugadores(4, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(3, jugadoresDelanteros, lineaDelanteros);
        } else if (formacion.equals("3-5-2")) {
            colocarJugadores(3, jugadoresDefensas, lineaDefensas);
            colocarJugadores(5, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(2, jugadoresDelanteros, lineaDelanteros);
        } else if (formacion.equals("3-6-1")) {
            colocarJugadores(3, jugadoresDefensas, lineaDefensas);
            colocarJugadores(6, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(1, jugadoresDelanteros, lineaDelanteros);
        } else if (formacion.equals("4-2-4")) {
            colocarJugadores(4, jugadoresDefensas, lineaDefensas);
            colocarJugadores(2, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(4, jugadoresDelanteros, lineaDelanteros);
        } else if (formacion.equals("4-3-3")) {
            colocarJugadores(4, jugadoresDefensas, lineaDefensas);
            colocarJugadores(3, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(3, jugadoresDelanteros, lineaDelanteros);
        } else if (formacion.equals("4-4-2")) {
            colocarJugadores(4, jugadoresDefensas, lineaDefensas);
            colocarJugadores(4, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(2, jugadoresDelanteros, lineaDelanteros);
        } else if (formacion.equals("4-5-1")) {
            colocarJugadores(4, jugadoresDefensas, lineaDefensas);
            colocarJugadores(5, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(1, jugadoresDelanteros, lineaDelanteros);
        } else if (formacion.equals("5-2-3")) {
            colocarJugadores(5, jugadoresDefensas, lineaDefensas);
            colocarJugadores(2, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(3, jugadoresDelanteros, lineaDelanteros);
        } else if (formacion.equals("5-3-2")) {
            colocarJugadores(5, jugadoresDefensas, lineaDefensas);
            colocarJugadores(3, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(2, jugadoresDelanteros, lineaDelanteros);
        } else if (formacion.equals("5-4-1")) {
            colocarJugadores(5, jugadoresDefensas, lineaDefensas);
            colocarJugadores(4, jugadoresMediocentros, lineaMediocentros);
            colocarJugadores(1, jugadoresDelanteros, lineaDelanteros);
        }

    }

    private void colocarJugadores(int cantidad, List<Jugador> jugadores, LinearLayout linea) {
        for (int i = 0; i < cantidad; i++) {
            ImageView jugadorView = new ImageView(requireActivity());

            if (i < jugadores.size()) {
                Jugador jugador = jugadores.get(i);

                Glide.with(this)
                        .load(jugador.getImagenUrl()) // URL desde Firestore
                        .placeholder(R.drawable.jugador) // Imagen por defecto
                        .into(jugadorView);
            } else {
                jugadorView.setImageResource(R.drawable.jugador); // Imagen por defecto si no hay jugadores suficientes
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
            params.setMargins(8, 0, 8, 0);
            jugadorView.setLayoutParams(params);

            linea.addView(jugadorView);
        }
    }


}