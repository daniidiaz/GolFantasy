package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ElegirCrearOUnirseLiga extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btnCrear, btnUnirse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_elegir_crear_ounirse_liga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Configurar el toolbar
        toolbar = findViewById(R.id.toolbarInicio);
        setSupportActionBar(toolbar);
        // Opcional: Habilitar botón de "Atrás"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCrear=findViewById(R.id.btnCrearNuevaLiga);
        btnCrear.setOnClickListener(this);

        btnUnirse=findViewById(R.id.btnUnirseLiga);
        btnUnirse.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.btnCrearNuevaLiga){
            Intent i=new Intent(this, PantallaJuegoPrincipal.class);
            startActivity(i);

        } else if (view.getId()==R.id.btnUnirseLiga) {
            Intent i=new Intent(this, UnirseLiga.class);
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