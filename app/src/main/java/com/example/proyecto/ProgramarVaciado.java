package com.example.proyecto;
import android.content.Context;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ProgramarVaciado {

    private static final String TAG = "ProgramarVaciado";

    public static void programarVaciadoJugadores(Context context) {
        Log.d(TAG, "Programando el vaciado de jugadores...");
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // Requiere conexión a internet
                .build();

        PeriodicWorkRequest vaciarJugadoresRequest = new PeriodicWorkRequest.Builder(VaciarJugadoresWorker.class, 7, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInitialDelay(calcularTiempoHastaMartes2359(), TimeUnit.MILLISECONDS) // Calcula el tiempo hasta el próximo martes a las 23:59
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "vaciarJugadoresWork", // Nombre único para el trabajo
                ExistingPeriodicWorkPolicy.KEEP, // Mantiene el trabajo existente si ya está programado
                vaciarJugadoresRequest
        );
        Log.d(TAG, "Vaciado de jugadores programado correctamente.");
    }

    private static long calcularTiempoHastaMartes2359() {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysUntilTuesday = (Calendar.FRIDAY - currentDayOfWeek + 7) % 7; // Calcula los días hasta el próximo martes
        calendar.add(Calendar.DAY_OF_MONTH, daysUntilTuesday);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long timeUntilTuesday2359 = calendar.getTimeInMillis() - System.currentTimeMillis();
        Log.d(TAG, "Tiempo hasta el próximo martes a las 23:59: " + timeUntilTuesday2359 + " ms");
        return timeUntilTuesday2359;
    }
}