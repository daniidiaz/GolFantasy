package com.example.proyecto;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivityCreacionFirestore {

    public void crearEstructuraFirestore(FirebaseFirestore db) {
        // Crear colección de Usuarios
        Map<String, Object> usuario1 = new HashMap<>();
        usuario1.put("nombreUsuario", "Usuario 1");
        usuario1.put("correo", "usuario1@ejemplo.com");
        usuario1.put("ligasCreadas", new ArrayList<>());
        usuario1.put("ligasUnidas", new ArrayList<>());
        usuario1.put("equipoActual", null);

        Map<String, Object> usuario2 = new HashMap<>();
        usuario2.put("nombreUsuario", "Usuario 2");
        usuario2.put("correo", "usuario2@ejemplo.com");
        usuario2.put("ligasCreadas", new ArrayList<>());
        usuario2.put("ligasUnidas", new ArrayList<>());
        usuario2.put("equipoActual", null);

        db.collection("usuarios").document("usuario1").set(usuario1);
        db.collection("usuarios").document("usuario2").set(usuario2);

        // Crear colección de Ligas
        Map<String, Object> liga1 = new HashMap<>();
        liga1.put("nombre", "Liga de Amigos");
        liga1.put("creador", "usuario1"); // Puedes cambiar esto por una referencia de usuario
        liga1.put("miembros", new ArrayList<>());
        liga1.put("equipos", new ArrayList<>());
        liga1.put("reglasDePuntuación", new HashMap<String, Integer>() {{
            put("goleador", 4);
            put("asistencia", 3);
        }});

        Map<String, Object> liga2 = new HashMap<>();
        liga2.put("nombre", "Liga Profesional");
        liga2.put("creador", "usuario2");
        liga2.put("miembros", new ArrayList<>());
        liga2.put("equipos", new ArrayList<>());
        liga2.put("reglasDePuntuación", new HashMap<String, Integer>() {{
            put("goleador", 5);
            put("asistencia", 2);
        }});

        db.collection("ligas").document("liga1").set(liga1);
        db.collection("ligas").document("liga2").set(liga2);

        // Crear colección de Equipos
        Map<String, Object> equipoA = new HashMap<>();
        equipoA.put("nombre", "Equipo A");
        equipoA.put("usuario", "usuario1"); // Puedes cambiar esto por una referencia de usuario
        equipoA.put("jugadores", new ArrayList<>());
        equipoA.put("liga", "liga1"); // Puedes cambiar esto por una referencia de liga
        equipoA.put("puntosTotales", 0);

        Map<String, Object> equipoB = new HashMap<>();
        equipoB.put("nombre", "Equipo B");
        equipoB.put("usuario", "usuario2");
        equipoB.put("jugadores", new ArrayList<>());
        equipoB.put("liga", "liga2");
        equipoB.put("puntosTotales", 0);

        db.collection("equipos").document("equipoA").set(equipoA);
        db.collection("equipos").document("equipoB").set(equipoB);

        // Crear colección de Jugadores
        Map<String, Object> jugador1 = new HashMap<>();
        jugador1.put("nombre", "Jugador 1");
        jugador1.put("equipo", "Equipo A");
        jugador1.put("posición", "Delantero");
        jugador1.put("puntosPorPartido", 5);
        jugador1.put("precio", 10);

        Map<String, Object> jugador2 = new HashMap<>();
        jugador2.put("nombre", "Jugador 2");
        jugador2.put("equipo", "Equipo B");
        jugador2.put("posición", "Centrocampista");
        jugador2.put("puntosPorPartido", 4);
        jugador2.put("precio", 12);

        db.collection("jugadores").document("jugador1").set(jugador1);
        db.collection("jugadores").document("jugador2").set(jugador2);

        // Crear subcolección de Membresías dentro de la Liga de Amigos
        db.collection("ligas").document("liga1").collection("miembros").document("membresia1").set(new HashMap<String, Object>() {{
            put("usuario", "usuario1");
            put("rol", "admin");
            put("fechaDeIngreso", FieldValue.serverTimestamp());
        }});

        db.collection("ligas").document("liga1").collection("miembros").document("membresia2").set(new HashMap<String, Object>() {{
            put("usuario", "usuario2");
            put("rol", "miembro");
            put("fechaDeIngreso", FieldValue.serverTimestamp());
        }});
    }
}