package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {

    private Button btnCrearLiga;
    private TextView tvLigasCreadas;
    private LinearLayout llListaLigas;

    private FirebaseFirestore db;
    private String idUsuario; // ID del usuario pasado como argumento

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
            idUsuario = getArguments().getString("usuarioId"); // Obtener el ID del usuario desde los argumentos
        }

        db = FirebaseFirestore.getInstance(); // Inicializar Firestore
        ligas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Inicializar vistas
        btnCrearLiga = view.findViewById(R.id.btnCrearLiga);
        tvLigasCreadas = view.findViewById(R.id.tvLigasCreadas);
        llListaLigas = view.findViewById(R.id.llListaLigas);

        // Configurar el botón para crear una liga
        btnCrearLiga.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreacionDeLigaFantasy.class);
            intent.putExtra("usuarioId", idUsuario); // Pasar el ID del usuario
            startActivity(intent);
        });

        // Cargar ligas desde Firestore
        cargarLigasDelUsuario();

        return view;
    }

    private void cargarLigasDelUsuario() {
        db.collection("ligas")
                .whereArrayContains("miembros", idUsuario) // Filtrar por el ID del usuario en el campo "miembros"
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
                    actualizarVistaLigas(); // Actualizar la vista con las ligas obtenidas
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar ligas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void actualizarVistaLigas() {
        llListaLigas.removeAllViews(); // Limpiar el layout antes de agregar nuevas ligas

        if (ligas.isEmpty()) {
            tvLigasCreadas.setText("Todavía no participas en ninguna liga");
        } else {
            tvLigasCreadas.setText("Participas en " + ligas.size() + " ligas");

            for (String liga : ligas) {
                TextView textView = new TextView(getContext());
                textView.setText(liga);
                textView.setPadding(16, 16, 16, 16);
                textView.setTextSize(16);
                textView.setBackgroundResource(R.drawable.borde_item_liga); // Fondo personalizado
                llListaLigas.addView(textView);

                // Opcional: Configurar un listener para cada TextView
                textView.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "Liga seleccionada: " + liga, Toast.LENGTH_SHORT).show();
                });
            }
        }
    }
}
