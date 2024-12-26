package com.example.proyecto;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ClasificacionFragment extends Fragment {

    private static final String TAG = "ClasificacionFragment";
    private TableLayout tableLayout;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clasificacion, container, false);
        tableLayout = view.findViewById(R.id.tableLayout);
        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarClasificacion();
    }

    private void cargarClasificacion() {
        db.collection("usuarios")
                .orderBy("puntuacion", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int posicion = 1;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombreUsuario = document.getString("nombreUsuario");
                            long puntuacion = document.getLong("puntuacion");

                            // Inflar el layout de la fila
                            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tr_clasificacion, tableLayout, false);

                            // Obtener referencias a los TextViews de la fila
                            TextView tvPosicion = tableRow.findViewById(R.id.tvPosicion);
                            TextView tvNombreUsuario = tableRow.findViewById(R.id.tvNombreUsuario);
                            TextView tvPuntuacion = tableRow.findViewById(R.id.tvPuntuacion);

                            // Configurar el texto de los TextViews
                            tvPosicion.setText(posicion + "º");
                            tvNombreUsuario.setText(nombreUsuario);
                            tvPuntuacion.setText(String.valueOf(puntuacion));

                            tableLayout.addView(tableRow);
                            posicion++;
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}