package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InicioFragment extends Fragment {

    private Button btnCrearLiga;
    private TextView tvLigasCreadas, tvProximasJornadas;
    private LinearLayout llListaLigas, llListaJornadas;

    private FirebaseFirestore db;
    private String idUsuario;

    private List<String> ligas;

    public InicioFragment() {
        // Constructor vacío obligatorio
    }

    public static InicioFragment newInstance(String idUsuario) {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();
        args.putString("usuarioId", idUsuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            idUsuario = getArguments().getString("usuarioId");
        }

        db = FirebaseFirestore.getInstance();
        ligas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Inicializar vistas
        btnCrearLiga = view.findViewById(R.id.btnCrearLiga);
        tvLigasCreadas = view.findViewById(R.id.tvLigasCreadas);
        tvProximasJornadas = view.findViewById(R.id.tvProximasJornadas);
        llListaLigas = view.findViewById(R.id.llListaLigas);
        llListaJornadas = view.findViewById(R.id.llListaJornadas); // Nuevo LinearLayout para jornadas

        btnCrearLiga.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreacionDeLigaFantasy.class);
            intent.putExtra("usuarioId", idUsuario);
            startActivity(intent);
        });

        // Cargar datos
        cargarLigasDelUsuario();
        cargarJornadaUno(); // Cargar la jornada 1

        return view;
    }

    private void cargarLigasDelUsuario() {
        db.collection("ligas")
                .whereArrayContains("miembros", idUsuario)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ligas.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        if (document.exists()) {
                            String liga = document.getString("nombre");
                            if (liga != null) {
                                ligas.add(liga);
                            }
                        }
                    }
                    actualizarVistaLigas();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar ligas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void actualizarVistaLigas() {
        llListaLigas.removeAllViews();

        if (ligas.isEmpty()) {
            tvLigasCreadas.setText("Todavía no participas en ninguna liga");
        } else {
            tvLigasCreadas.setText("Participas en " + ligas.size() + " ligas");

            for (String liga : ligas) {
                TextView textView = new TextView(getContext());
                textView.setText(liga);
                textView.setPadding(16, 16, 16, 16);
                textView.setTextSize(16);
                textView.setBackgroundResource(R.drawable.borde_item_liga);
                llListaLigas.addView(textView);

                textView.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "Liga seleccionada: " + liga, Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    private void mostrarJornada(List<Map<String, Object>> partidos) {
        llListaJornadas.removeAllViews();

        for (Map<String, Object> partido : partidos) {
            Object localObj = partido.get("local");
            Object visitanteObj = partido.get("visitante");
            Long golesLocal = (Long) partido.get("golesLocal");
            Long golesVisitante = (Long) partido.get("golesVisitante");

            // Crear contenedor horizontal para los escudos y el resultado
            LinearLayout contenedorPartido = new LinearLayout(getContext());
            contenedorPartido.setOrientation(LinearLayout.HORIZONTAL);
            contenedorPartido.setPadding(16, 8, 16, 8);

            // Escudo del equipo local
            ImageView escudoLocal = new ImageView(getContext());
            escudoLocal.setLayoutParams(new LinearLayout.LayoutParams(100, 100)); // Tamaño de la imagen
            contenedorPartido.addView(escudoLocal);

            // Resultado
            TextView resultadoView = new TextView(getContext());
            resultadoView.setTextSize(16);
            resultadoView.setPadding(16, 0, 16, 0);
            contenedorPartido.addView(resultadoView);

            // Escudo del equipo visitante
            ImageView escudoVisitante = new ImageView(getContext());
            escudoVisitante.setLayoutParams(new LinearLayout.LayoutParams(100, 100)); // Tamaño de la imagen
            contenedorPartido.addView(escudoVisitante);

            // Obtener y cargar los escudos
            getEscudoEquipo(localObj, escudoLocal);
            getEscudoEquipo(visitanteObj, escudoVisitante);

            // Mostrar el resultado
            if (golesLocal != null && golesVisitante != null) {
                String resultado = golesLocal + " - " + golesVisitante;
                resultadoView.setText(resultado);
            } else {
                resultadoView.setText(" - : - ");
            }

            // Agregar al layout de jornadas
            llListaJornadas.addView(contenedorPartido);
        }
    }

    private void getEscudoEquipo(Object equipoObj, ImageView imageView) {
        if (equipoObj instanceof DocumentReference) {
            ((DocumentReference) equipoObj).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String urlEscudo = documentSnapshot.getString("escudo");
                            if (urlEscudo != null) {
                                // Verificar que el fragmento está agregado antes de cargar la imagen
                                if (isAdded() && getContext() != null) {
                                    Glide.with(getContext())
                                            .load(urlEscudo)
                                            .placeholder(R.drawable.placeholder) // Imagen por defecto mientras carga
                                            .error(R.drawable.error_image) // Imagen en caso de error
                                            .into(imageView);
                                } else {
                                    // Si el fragmento no está agregado, podemos manejarlo (por ejemplo, mostrar un error)
                                    imageView.setImageResource(R.drawable.error_image);
                                }
                            } else {
                                imageView.setImageResource(R.drawable.error_image);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error al cargar el escudo", Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.error_image);
                    });
        } else {
            imageView.setImageResource(R.drawable.error_image);
        }
    }



    // Interfaz para obtener el nombre del equipo de manera asíncrona
    interface OnNombreEquipoListener {
        void onNombreObtenido(String nombre);
    }
    private void cargarJornadaUno() {
        db.collection("jornadas")
                .document("jornada1") // Nombre del documento de la jornada (ajústalo según tu base de datos)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> partidos = (List<Map<String, Object>>) documentSnapshot.get("partidos");
                        if (partidos != null) {
                            mostrarJornada(partidos);
                        } else {
                            Toast.makeText(getContext(), "No hay datos de partidos para esta jornada", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar la jornada: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
