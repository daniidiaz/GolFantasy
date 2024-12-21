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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private String idUsuario;
    private EquipoFragment equipoFragment;

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

        // Obtener el idUsuario de los argumentos
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            idUsuario = getArguments().getString("usuarioId");  // Asumiendo que el ID de usuario se pasa como 'usuarioId'
        }

        if (idUsuario == null) {
            Log.e("ID Usuario", "El ID del usuario no ha sido proporcionado.");
            return;
        }

        db = FirebaseFirestore.getInstance();
        obtenerJugadoresDeFirestore(idUsuario);
    }

    private void obtenerJugadoresDeFirestore(String idUsuario) {
        // Consulta el documento en la colección "formaciones" que coincide con el idUsuario
        db.collection("formaciones")
                .document(idUsuario) // Busca el documento que coincide con el idUsuario
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Verifica si el documento tiene un array llamado "jugadores"
                        List<Map<String, Object>> jugadoresArray = (List<Map<String, Object>>) documentSnapshot.get("jugadores");

                        if (jugadoresArray != null) {
                            // Recorre cada jugador en el array
                            for (Map<String, Object> jugadorData : jugadoresArray) {
                                Jugador jugador = convertirAModeloJugador(jugadorData);
                                agregarJugadorPorPosicion(jugador);  // Agregar el jugador a la lista correspondiente
                            }

                            // Una vez que se obtienen los jugadores, podemos actualizar la vista
                            // Esto se hará cuando se seleccione una formación
                            cambiarFormacion("4-3-3");  // Establecer una formación inicial, por ejemplo
                        } else {
                            Log.e("Firestore", "El array 'jugadores' no existe o está vacío.");
                        }
                    } else {
                        Log.e("Firestore", "No se encontró el documento con idUsuario: " + idUsuario);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error al obtener documento", e));
    }


    private Jugador convertirAModeloJugador(Map<String, Object> jugadorData) {
        // Extraemos los datos del jugador del mapa
        String idJugador = (String) jugadorData.get("idJugador");
        String nombre = (String) jugadorData.get("nombre");
        int puntuacion = ((Long) jugadorData.get("puntuacion")).intValue();
        int precio = ((Long) jugadorData.get("precio")).intValue();
        DisponibilidadJugador disponibilidad = DisponibilidadJugador.valueOf((String) jugadorData.get("disponibilidad"));
        Posicion posicion = Posicion.valueOf((String) jugadorData.get("posicion"));
        DocumentReference equipo = (DocumentReference) jugadorData.get("equipo");
        int edad = ((Long) jugadorData.get("edad")).intValue();
        String nacionalidad = (String) jugadorData.get("nacionalidad");
        String imagenUrl = (String) jugadorData.get("imagenUrl");
        int partidosJugados = ((Long) jugadorData.get("partidosJugados")).intValue();
        int goles = ((Long) jugadorData.get("goles")).intValue();
        int asistencias = ((Long) jugadorData.get("asistencias")).intValue();
        int tarjetasAmarillas = ((Long) jugadorData.get("tarjetasAmarillas")).intValue();
        int tarjetasRojas = ((Long) jugadorData.get("tarjetasRojas")).intValue();

        // Creamos y retornamos una instancia del objeto Jugador
        return new Jugador(idJugador, nombre, puntuacion, precio, disponibilidad, posicion, equipo, edad, nacionalidad, imagenUrl, partidosJugados, goles, asistencias, tarjetasAmarillas, tarjetasRojas);
    }



    private void agregarJugadorPorPosicion(Jugador jugador) {
        switch (jugador.getPosicion().name().toLowerCase()) {
            case "portero":
                // Verificar si el portero ya ha sido fichado
                if (portero != null && portero.getTag() != null && portero.getTag().equals(jugador.getIdJugador())) {
                    return; // Salir del método si el portero ya ha sido fichado
                }
                // Cargar la imagen del portero si no ha sido fichado
                Glide.with(this)
                        .load(jugador.getImagenUrl())
                        .placeholder(R.drawable.jugador) // Imagen por defecto
                        .into(portero);
                portero.setTag(jugador.getIdJugador()); // Establecer el ID del jugador como tag del ImageView
                break;
            case "defensa":
                // Verificar si el defensa ya ha sido fichado
                if (jugadoresDefensas.stream().anyMatch(j -> j.getIdJugador().equals(jugador.getIdJugador()))) {
                    return; // Salir del método si el defensa ya ha sido fichado
                }
                jugadoresDefensas.add(jugador);
                break;
            case "mediocentro":
                // Verificar si el mediocentro ya ha sido fichado
                if (jugadoresMediocentros.stream().anyMatch(j -> j.getIdJugador().equals(jugador.getIdJugador()))) {
                    return; // Salir del método si el mediocentro ya ha sido fichado
                }
                jugadoresMediocentros.add(jugador);
                break;
            case "delantero":
                // Verificar si el delantero ya ha sido fichado
                if (jugadoresDelanteros.stream().anyMatch(j -> j.getIdJugador().equals(jugador.getIdJugador()))) {
                    return; // Salir del método si el delantero ya ha sido fichado
                }
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
        portero = view.findViewById(R.id.portero);

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

    public boolean yaEstaFichado(Jugador jugador) {
        switch (jugador.getPosicion().name().toLowerCase()) {
            case "portero":
                return portero != null && portero.getTag() != null && portero.getTag().equals(jugador.getIdJugador());
            case "defensa":
                return jugadoresDefensas.stream().anyMatch(j -> j.getIdJugador().equals(jugador.getIdJugador()));
            case "mediocentro":
                return jugadoresMediocentros.stream().anyMatch(j -> j.getIdJugador().equals(jugador.getIdJugador()));
            case "delantero":
                return jugadoresDelanteros.stream().anyMatch(j -> j.getIdJugador().equals(jugador.getIdJugador()));
            default:
                return false;
        }
    }

    public void actualizarListasJugadores(Jugador jugador) {
        switch (jugador.getPosicion().name().toLowerCase()) {
            case "portero":
                // ... (código para actualizar la lista de porteros) ...
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


}