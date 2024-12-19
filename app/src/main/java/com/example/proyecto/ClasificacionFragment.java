package com.example.proyecto;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        db.collection("usuarios") // Reemplaza "usuarios" con el nombre de tu colección
                .orderBy("puntuacion", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int posicion = 1;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombreUsuario = document.getString("nombreUsuario");
                            long puntuacion = document.getLong("puntuacion");

                            TableRow tableRow = new TableRow(requireContext());
                            tableRow.addView(createTextView(posicion + "º"));
                            tableRow.addView(createTextView(nombreUsuario));
                            tableRow.addView(createTextView(String.valueOf(puntuacion)));
                            tableLayout.addView(tableRow);

                            posicion++;
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(requireContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        return textView;
    }
}