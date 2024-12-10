package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragment extends Fragment implements View.OnClickListener {

    private Button btnIrACrearLiga;
    private TextView tvLigasCreadas;
    private LinearLayout llListaLigas;


    private FirebaseFirestore db;
    private String idUsuario = "idUsuarioEjemplo"; // Sustituir con el ID del usuario autenticado

    private List<String> ligas;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InicioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance(); // Inicializar Firestore
        ligas = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        btnIrACrearLiga = view.findViewById(R.id.btnCrearLiga);
        btnIrACrearLiga.setOnClickListener(this);

        tvLigasCreadas = view.findViewById(R.id.tvLigasCreadas);
        llListaLigas = view.findViewById(R.id.llListaLigas);

        cargarLigasDelUsuario();


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnCrearLiga){
            Intent intent = new Intent(getActivity(), CreacionDeLigaFantasy.class);
            startActivity(intent);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            String nuevaLiga = data.getStringExtra("nuevaLiga");
            if (nuevaLiga != null) {
                ligas.add(nuevaLiga);
                actualizarVistaLigas();
            }
        }
    }

    private void cargarLigasDelUsuario() {
        db.collection("ligas")
                .whereArrayContains("miembros", idUsuario)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ligas.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        if (document.exists()) { // Verifica que el documento es válido
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
                textView.setBackgroundResource(R.drawable.borde_item_liga); // Opcional: fondo personalizado
                llListaLigas.addView(textView);
            }
        }
    }
}