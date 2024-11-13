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

public class PaginaCrearUsuario extends AppCompatActivity implements View.OnClickListener {

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
            Intent i=new Intent(this, ElegirCrearOUnirseLiga.class);
            startActivity(i);
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
