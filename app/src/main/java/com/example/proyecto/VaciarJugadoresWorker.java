package com.example.proyecto;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Collections;

public class VaciarJugadoresWorker extends Worker {

    private static final String TAG = "VaciarJugadoresWorker";

    public VaciarJugadoresWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Iniciando el trabajo de vaciado de jugadores y reseteo de presupuesto...");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        // Operación 1: Vaciar la lista de jugadores en la colección "formaciones"
        CollectionReference formacionesRef = db.collection("formaciones");
        formacionesRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        batch.update(document.getReference(), "jugadores", Collections.emptyList());
                    }

                    // Operación 2: Establecer el presupuesto a 500.000.000 en la colección "jugadores"
                    CollectionReference jugadoresRef = db.collection("jugadores");
                    jugadoresRef.get()
                            .addOnSuccessListener(queryDocumentSnapshots2 -> {
                                for (QueryDocumentSnapshot document2 : queryDocumentSnapshots2) {
                                    batch.update(document2.getReference(), "presupuesto", 500000000);
                                }

                                // Ejecutar todas las operaciones en el batch
                                batch.commit()
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "Listas de jugadores vaciadas y presupuestos reseteados correctamente.");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Error al vaciar las listas de jugadores o resetear los presupuestos: ", e);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error al obtener los documentos de 'jugadores': ", e);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al obtener los documentos de 'formaciones': ", e);
                });

        return Result.success();
    }
}