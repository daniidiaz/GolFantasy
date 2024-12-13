package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreacionDeLigaFantasy extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btnCreaLiga;
    private EditText etNombreLigaFantasy;
    private FirebaseFirestore db; // Instancia de Firestore
    private String usuarioId; // Identificador del usuario actual (recupéralo al iniciar sesión)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creacion_de_liga_fantasy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar el toolbar
        toolbar = findViewById(R.id.toolbarCrearLiga);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Referencias de la interfaz
        btnCreaLiga = findViewById(R.id.btnCreaLiga);
        btnCreaLiga.setOnClickListener(this);
        etNombreLigaFantasy = findViewById(R.id.etNombreLigaFantasy);

        // Recuperar usuarioId (este valor debe pasarse desde la pantalla de inicio de sesión)
        usuarioId = getIntent().getStringExtra("usuarioId");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Cierra la actividad actual y vuelve a la anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnCreaLiga) {
            String nombreLiga = etNombreLigaFantasy.getText().toString().trim();

            if (nombreLiga.isEmpty()) {
                Toast.makeText(this, "No puedes crear una liga sin nombre", Toast.LENGTH_SHORT).show();
                return;
            }

            if (usuarioId == null || usuarioId.isEmpty()) {
                Toast.makeText(this, "No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show();
                return;
            }

            // Instancia del controlador de base de datos
            ControladorBBDD controladorBBDD = new ControladorBBDD();

            // Llamar al método para crear la liga
            controladorBBDD.crearLigas(nombreLiga, usuarioId, new ControladorBBDD.CrearLigaCallback() {
                @Override
                public void onSuccess(String idLiga) {
                    Toast.makeText(CreacionDeLigaFantasy.this, "Liga creada con ID: " + idLiga, Toast.LENGTH_SHORT).show();

                    // Actualizar el array `ligasCreadas` en el usuario
                    db.collection("usuarios").document(usuarioId)
                            .update("ligasCreadas", FieldValue.arrayUnion(idLiga))
                            .addOnSuccessListener(aVoid -> {
                                // Redirigir a PantallaJuegoPrincipal
                                Intent intent = new Intent(CreacionDeLigaFantasy.this, PantallaJuegoPrincipal.class);
                                intent.putExtra("idLiga", idLiga); // Puedes pasar el ID de la liga si es necesario
                                intent.putExtra("nombreLiga", nombreLiga); // También puedes pasar el nombre de la liga
                                startActivity(intent);

                               // finish(); // Cierra esta actividad para que no esté en la pila de actividades
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(CreacionDeLigaFantasy.this, "Error al actualizar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(CreacionDeLigaFantasy.this, "Error al crear la liga: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



}
