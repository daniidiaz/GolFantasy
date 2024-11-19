package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PaginaCrearUsuario extends AppCompatActivity implements View.OnClickListener {

    // Define una referencia a la base de datos
    private FirebaseFirestore db;

    private Spinner spinnerEquipos;
    private EditText editTextContrasenia;
    private ImageButton btnVerContraseña;
    private boolean isPasswordVisible = false;
    private Toolbar toolbar;
    private Button btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_crear_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();// Inicializa Firestore

        editTextContrasenia = findViewById(R.id.editTextText6);
        btnVerContraseña = findViewById(R.id.btnVerContraseña);
        btnVerContraseña.setOnClickListener(this);
        btnCrearCuenta=findViewById(R.id.btnCrearCuenta);
        btnCrearCuenta.setOnClickListener(this);

        // Configurar el toolbar
        toolbar = findViewById(R.id.toolbarInicio);
        setSupportActionBar(toolbar);

        // Opcional: Habilitar botón de "Atrás"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerEquipos = findViewById(R.id.spinnerEquipos);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opcEquipos, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_item);

        spinnerEquipos.setAdapter(adapter);

        spinnerEquipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // No hacer nada si se selecciona "Equipos..."
                    return;
                }
                // Obtener la opción seleccionada
                String opcionSeleccionada = parent.getItemAtPosition(position).toString();
                // Mostrar un mensaje con la opción seleccionada
                Toast.makeText(PaginaCrearUsuario.this, "Seleccionaste: " + opcionSeleccionada, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Código en caso de que no se seleccione ninguna opción
            }
        });

        }

    private void guardarDatosEnFirebase() {
        // Obtener los datos de los campos
        EditText etNombreUsuario = findViewById(R.id.etCrearNombre);
        EditText etEmail = findViewById(R.id.editTextText5);
        EditText etTelefono = findViewById(R.id.editTextText4);
        EditText etContrasenia = findViewById(R.id.editTextText6);
        Spinner spEquipoFavorito = findViewById(R.id.spinnerEquipos);

        String email = etEmail.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        // Verificar si el email o el teléfono ya existen
        db.collection("usuarios")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(taskEmail -> {
                    if (taskEmail.isSuccessful() && !taskEmail.getResult().isEmpty()) {
                        Toast.makeText(this, "El email ya existe", Toast.LENGTH_SHORT).show();
                    } else {
                        db.collection("usuarios")
                                .whereEqualTo("telefono", telefono)
                                .get()
                                .addOnCompleteListener(taskTelefono -> {
                                    if (taskTelefono.isSuccessful() && !taskTelefono.getResult().isEmpty()) {
                                        Toast.makeText(this, "El teléfono ya existe", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Convertir datos en un Map para guardarlos en Firebase
                                        Map<String, Object> usuario = new HashMap<>();
                                        usuario.put("nombreUsuario", etNombreUsuario.getText().toString().trim());
                                        usuario.put("email", email);
                                        usuario.put("telefono", telefono);
                                        usuario.put("contrasenia", etContrasenia.getText().toString().trim());
                                        usuario.put("equipoFavorito", spEquipoFavorito.getSelectedItem().toString());

                                        // Añadir datos a Firebase
                                        db.collection("usuarios").add(usuario)
                                                .addOnSuccessListener(documentReference -> {
                                                    Toast.makeText(PaginaCrearUsuario.this, "Cuenta creada con éxito!", Toast.LENGTH_SHORT).show();
                                                    // Navegar a la siguiente pantalla solo si se crea la cuenta
                                                    Intent i = new Intent(PaginaCrearUsuario.this, ElegirCrearOUnirseLiga.class);
                                                    startActivity(i);
                                                })
                                                .addOnFailureListener(e ->
                                                        Toast.makeText(PaginaCrearUsuario.this, "Error al crear cuenta: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                    }
                                });
                    }
                });
    }



    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.btnVerContraseña){
            if (isPasswordVisible) {
                // Cambiar a contraseña oculta
                editTextContrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnVerContraseña.setImageResource(R.drawable.baseline_visibility_off_24); // Cambia a ojo cerrado
            } else {
                // Cambiar a contraseña visible
                editTextContrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnVerContraseña.setImageResource(R.drawable.baseline_remove_red_eye_24); // Cambia a ojo abierto
            }
            isPasswordVisible = !isPasswordVisible; // Alternar el estado
            // Mover el cursor al final del texto
            editTextContrasenia.setSelection(editTextContrasenia.getText().length());
        } else if (view.getId()==R.id.btnCrearCuenta) {
            guardarDatosEnFirebase();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Acción al hacer clic en el botón de "Atrás" del toolbar
            finish(); // Cierra la actividad actual y vuelve a la anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
