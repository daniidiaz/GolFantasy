package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsuario;
    private EditText etContrasenia;
    private Button boton;
    private TextView linkRegistrar;

    private FirebaseFirestore db;  // Instancia de Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Referencias a los campos de la interfaz
        etUsuario = findViewById(R.id.etUsuario);
        etContrasenia = findViewById(R.id.etContraseña);
        boton = findViewById(R.id.botonEntrar);
        boton.setOnClickListener(this);
        linkRegistrar = findViewById(R.id.linkRegistrar);
        linkRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.botonEntrar) {
            String usuario = etUsuario.getText().toString().trim();
            String contrasenia = etContrasenia.getText().toString().trim();

            if (usuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(this, "Campos vacíos, rellene todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Verificar si el usuario y contraseña existen en la base de datos
                verificarUsuarioYContraseña(usuario, contrasenia);
            }
        } else if (view.getId() == R.id.linkRegistrar) {
            Intent intent = new Intent(this, PaginaCrearUsuario.class);
            startActivity(intent);
        }
    }

    private void verificarUsuarioYContraseña(String usuario, String contrasenia) {
        // Realizar la consulta para buscar el usuario en Firestore
        db.collection("usuarios")  // Asegúrate de que el nombre de la colección sea "usuarios"
                .whereEqualTo("nombreUsuario", usuario)  // Buscar por nombre de usuario
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        if (querySnapshot.isEmpty()) {
                            // Si no hay coincidencias con el nombre de usuario
                            Toast.makeText(MainActivity.this, "No existe la cuenta", Toast.LENGTH_SHORT).show();
                        } else {
                            // Si se encuentra un documento, verificamos la contraseña
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String contraseniaGuardada = document.getString("contrasenia");

                                if (contrasenia.equals(contraseniaGuardada)) {
                                    // Si la contraseña coincide
                                    String usuarioId = document.getId(); // Obtén el ID del documento
                                    Intent intent = new Intent(MainActivity.this, PantallaJuegoPrincipal.class);
                                    intent.putExtra("usuarioId", usuarioId);
                                    startActivity(intent);

                                    // Verificar los arrays ligasCreadas y ligasUnidas
                                  //  verificarLigas(document, usuarioId);
                                } else {
                                    // Si la contraseña no coincide
                                    Toast.makeText(MainActivity.this, "La contraseña no es correcta", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } else {
                        // Si ocurre algún error en la consulta
                        Toast.makeText(MainActivity.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*private void verificarLigas(QueryDocumentSnapshot document, String usuarioId) {
        // Obtener los arrays de ligas creadas y unidas
        List<String> ligasCreadas = (List<String>) document.get("ligasCreadas");
        List<String> ligasUnidas = (List<String>) document.get("ligasUnidas");

        // Si ambos arrays están vacíos, ir a ElegirCrearOUnirseLiga
        if ((ligasCreadas == null || ligasCreadas.isEmpty()) && (ligasUnidas == null || ligasUnidas.isEmpty())) {
            Intent intent = new Intent(MainActivity.this, ElegirCrearOUnirseLiga.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
        } else {
            // Si los arrays no están vacíos, ir a PaginaJuegoPrincipal
            Intent intent = new Intent(MainActivity.this, PantallaJuegoPrincipal.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
        }
    }*/
}
