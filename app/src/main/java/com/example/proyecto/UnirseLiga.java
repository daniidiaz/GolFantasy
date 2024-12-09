package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UnirseLiga extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etIDLiga;
    private Button btUnirseLiga;
    private ControladorBBDD controladorBBDD;
    private String usuarioId; // Recuperar el ID del usuario actual al iniciar sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unirse_liga);

        // Inicializar el controlador de la base de datos
        controladorBBDD = new ControladorBBDD();

        // Configurar el Toolbar
        toolbar = findViewById(R.id.tbUnirseLiga);
        setSupportActionBar(toolbar);

        // Referencias de la interfaz
        etIDLiga = findViewById(R.id.etIDLiga);
        btUnirseLiga = findViewById(R.id.btIDLiga);

        // Recuperar el usuario ID pasado como intent extra
        usuarioId = getIntent().getStringExtra("usuarioId");

        // Asignar el listener al botón
        btUnirseLiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idLiga = etIDLiga.getText().toString().trim();

                if (idLiga.isEmpty()) {
                    Toast.makeText(UnirseLiga.this, "Pon el ID de una Liga", Toast.LENGTH_SHORT).show();
                } else {
                    // Llamar al método de la clase ControladorBBDD
                    controladorBBDD.unirseALiga(idLiga, usuarioId, new ControladorBBDD.UnirseLigaCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(UnirseLiga.this, "Te has unido a la liga con éxito", Toast.LENGTH_SHORT).show();

                            // Redirigir a la pantalla principal del juego
                            Intent intent = new Intent(UnirseLiga.this, PantallaJuegoPrincipal.class);
                            intent.putExtra("idLiga", idLiga); // Pasar el ID de la liga como extra
                            intent.putExtra("usuarioId", usuarioId); // Pasar el ID del usuario como extra
                            startActivity(intent);
                        }

                        @Override
                        public void onNotFound() {
                            Toast.makeText(UnirseLiga.this, "No se encontró la liga con ese ID", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(UnirseLiga.this, "Error al unirse a la liga: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
