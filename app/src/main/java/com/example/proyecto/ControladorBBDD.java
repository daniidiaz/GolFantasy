package com.example.proyecto;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControladorBBDD {

    private FirebaseFirestore db;

    public ControladorBBDD() {
        db = FirebaseFirestore.getInstance();
    }

    public void crearEstructuraInicial() {
        // Verificar si la estructura de Firestore ya existe
        db.collection("usuarios").document("usuario1").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            // La estructura no existe, crearla
                            //crearEstructuraFirestore();
                        }
                    } else {
                        // Manejar el error
                        // Puedes agregar aquí la lógica para manejar el error,
                        // como mostrar un mensaje al usuario o registrar el error.
                        // Por ejemplo:
                        // Log.e("ControladorBBDD", "Error al verificar la estructura de Firestore: " + task.getException());
                    }
                });
    }

        public interface CrearUsuarioCallback {
            void onSuccess();
            void onUserExists();
            void onError(Exception e);
        }

        public void crearUsuarios(Usuario usuario, CrearUsuarioCallback callback) {
            // Verificar si el usuario ya existe por correo electrónico
            db.collection("usuarios")
                    .whereEqualTo("correo", usuario.getCorreo())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                // El usuario no existe, crearlo
                                crearNuevoUsuario(usuario, callback);
                            } else {
                                // El usuario ya existe
                                callback.onUserExists();
                            }
                        } else {
                            // Error en la consulta
                            callback.onError(task.getException());
                        }
                    });
        }


        private void crearNuevoUsuario(Usuario usuario, CrearUsuarioCallback callback) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("nombreUsuario", usuario.getNombreUsuario());
            temp.put("telefono", usuario.getTelefono());
            temp.put("correo", usuario.getCorreo());
            temp.put("ligasCreadas", new ArrayList<>());
            temp.put("ligasUnidas", new ArrayList<>());

            db.collection("usuarios").add(temp)
                    .addOnSuccessListener(documentReference -> {
                        documentReference.update("idUsuario", documentReference.getId());
                        callback.onSuccess();
                    })
                    .addOnFailureListener(callback::onError);
        }

    private void crearEquipoDeUsuario(String idUsuario, String idLiga, String idEquipo) {
        Map<String, Object> equipoDeUsuario = new HashMap<>();
        equipoDeUsuario.put("idUsuario", idUsuario);
        equipoDeUsuario.put("idLiga", idLiga);
        equipoDeUsuario.put("idEquipo", idEquipo);

        db.collection("equiposDeUsuario").add(equipoDeUsuario);
    }

    private void crearLigas(String nombreLiga, String idUsuarioCreador) {
        Map<String, Object> liga1 = new HashMap<>();
        liga1.put("nombre", nombreLiga);
        liga1.put("administrador", idUsuarioCreador);
        liga1.put("miembros", new ArrayList<>());
        liga1.put("equipos", new ArrayList<>());
        liga1.put("reglasDePuntuacion", new HashMap<String, Integer>() {{
            put("goleador", 4);
            put("asistencia", 3);
        }});

        db.collection("ligas").document("liga1").set(liga1);
    }

    private void crearEquipos() {
        Map<String, Object> equipoA = new HashMap<>();
        equipoA.put("nombre", "Equipo A");
        equipoA.put("usuario", "usuario1");
        equipoA.put("jugadores", new ArrayList<>());
        equipoA.put("liga", "liga1");
        equipoA.put("puntosTotales", 0);

        db.collection("equipos").document("equipoA").set(equipoA);
    }

    private void crearJugadores() {
        Map<String, Object> jugador1 = new HashMap<>();
        jugador1.put("nombre", "Jugador 1");
        jugador1.put("equipo", "Equipo A");
        jugador1.put("posición", "Delantero");
        jugador1.put("puntosPorPartido", 5);
        jugador1.put("precio", 10);

        db.collection("jugadores").document("jugador1").set(jugador1);
    }

    private void crearMembresias() {
        db.collection("ligas").document("liga1").collection("miembros").document("membresia1").set(new HashMap<String, Object>() {{
            put("usuario", "usuario1");
            put("rol", "admin");
        }});

    }

    // ... otros métodos de ControladorBBDD ...
}