package com.example.proyecto;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Map;

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

                            // Find the button and set OnClickListener
                            Button btVerEquipo = tableRow.findViewById(R.id.btVerEquipo);
                            btVerEquipo.setOnClickListener(v -> {
                                String username = tvNombreUsuario.getText().toString(); // Get username

                                // Query Firebase to get the idUsuario for the given username
                                db.collection("usuarios")
                                        .whereEqualTo("nombreUsuario", username)
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                if (task1.getResult().size() > 0) {
                                                    String idUsuario = task1.getResult().getDocuments().get(0).getId(); // Get idUsuario

                                                    // Now query Firebase to get the 'jugadores' array from 'formaciones' collection
                                                    db.collection("formaciones")
                                                            .whereEqualTo("idUsuario", idUsuario) // Filter by idUsuario
                                                            .get()
                                                            .addOnCompleteListener(task2 -> {
                                                                if (task2.isSuccessful()) {
                                                                    if (task2.getResult().size() > 0) {
                                                                        // Get the first document (assuming there's only one matching document)
                                                                        QueryDocumentSnapshot formacionDocument = (QueryDocumentSnapshot) task2.getResult().getDocuments().get(0);

                                                                        // Retrieve the 'jugadores' array
                                                                        List<Map<String, Object>> jugadores = (List<Map<String, Object>>) formacionDocument.get("jugadores");

                                                                        // Create and display the popup window
                                                                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                                        View popupView = inflater.inflate(R.layout.popup_window_layout, null);

                                                                        RecyclerView recyclerView = popupView.findViewById(R.id.recyclerViewPlayers);
                                                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                                        PlayerAdapter adapter = new PlayerAdapter(getContext(), jugadores); // Use your custom PlayerAdapter
                                                                        recyclerView.setAdapter(adapter);

                                                                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                                                                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                                                        boolean focusable = true; // Let taps outside the popup also dismiss it
                                                                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                                                                        // Show the popup window below the anchor view (button)
                                                                        popupWindow.showAsDropDown(btVerEquipo);

                                                                    } else {
                                                                        Toast.makeText(getContext(), " Este jugador aún no ha fichado.", Toast.LENGTH_SHORT).show();
                                                                        Log.d(TAG, "Array de jugadores vacio para el usuario con id: " + idUsuario);
                                                                    }
                                                                } else {
                                                                    Log.d(TAG, "get failed with ", task2.getException());
                                                                }
                                                            });
                                                } else {
                                                    Log.d(TAG, "No user found with username: " + username);
                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task1.getException());
                                            }
                                        });
                            });

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