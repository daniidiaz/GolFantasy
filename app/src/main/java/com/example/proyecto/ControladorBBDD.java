package com.example.proyecto;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            temp.put("contraseña", usuario.getContrasenia());
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

    public interface CrearLigaCallback {
        void onSuccess(String idLiga);
        void onError(Exception e);
    }

    public void crearLigas(String nombreLiga, String idUsuarioCreador, CrearLigaCallback callback) {
        if (nombreLiga == null || nombreLiga.isEmpty() || idUsuarioCreador == null || idUsuarioCreador.isEmpty()) {
            callback.onError(new IllegalArgumentException("El nombre de la liga o el ID del usuario no pueden estar vacíos."));
            return;
        }

        // Crear los datos de la liga
        Map<String, Object> liga = new HashMap<>();
        liga.put("nombre", nombreLiga);
        liga.put("administrador", idUsuarioCreador);
        liga.put("miembros", new ArrayList<>()); // Lista vacía de miembros
        liga.put("equipos", new ArrayList<>()); // Lista vacía de equipos
        liga.put("reglasDePuntuacion", new HashMap<String, Integer>() {{
            put("goleador", 4);
            put("asistencia", 3);
        }});
        liga.put("fechaCreacion", FieldValue.serverTimestamp());

        // Guardar la liga en Firestore
        db.collection("ligas")
                .add(liga)
                .addOnSuccessListener(documentReference -> {
                    String idLiga = documentReference.getId();

                    // Actualizar el documento de la liga con su ID y añadir el creador como miembro
                    documentReference.update("idLiga", idLiga,
                                    "miembros", FieldValue.arrayUnion(idUsuarioCreador))
                            .addOnSuccessListener(aVoid -> {
                                // Añadir el ID de la liga al campo `ligasCreadas` del usuario
                                db.collection("usuarios")
                                        .document(idUsuarioCreador)
                                        .update("ligasCreadas", FieldValue.arrayUnion(idLiga))
                                        .addOnSuccessListener(aVoid2 -> {
                                            // Ahora actualizar todos los jugadores con el nuevo propietario
                                            actualizarJugadoresConLiga(idLiga, callback);
                                        })
                                        .addOnFailureListener(callback::onError);
                            })
                            .addOnFailureListener(callback::onError);
                })
                .addOnFailureListener(callback::onError);
    }

    private void actualizarJugadoresConLiga(String idLiga, CrearLigaCallback callback) {
        // Obtener todos los jugadores
        db.collection("jugadores").get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        // Actualizar el mapa 'propietarios' con el ID de la nueva liga
                        document.getReference()
                                .update("propietarios." + idLiga, null) // Asigna null al valor inicial del propietario
                                .addOnFailureListener(e -> {
                                    // Manejar el error individualmente si algún jugador falla
                                    e.printStackTrace();
                                });
                    }
                    callback.onSuccess(idLiga);
                })
                .addOnFailureListener(callback::onError);
    }


    // Callback para la operación de unirse a una liga
    public interface UnirseLigaCallback {
        void onSuccess();
        void onNotFound();
        void onError(Exception e);
    }

    // Unirse a una liga
    public void unirseALiga(String idLiga, String usuarioId, UnirseLigaCallback callback) {
        db.collection("ligas")
                .document(idLiga) // Buscar la liga por su ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        // Liga encontrada, añadir el usuario al array de miembros
                        db.collection("ligas").document(idLiga)
                                .update("miembros", FieldValue.arrayUnion(usuarioId)) // Agregar al usuario al array
                                .addOnSuccessListener(unused -> callback.onSuccess())
                                .addOnFailureListener(callback::onError);
                    } else {
                        // Liga no encontrada
                        callback.onNotFound();
                    }
                })
                .addOnFailureListener(callback::onError);
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