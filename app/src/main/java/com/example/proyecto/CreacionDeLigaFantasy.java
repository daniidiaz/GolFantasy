package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreacionDeLigaFantasy extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btnCrear;
    private EditText etNombreLiga;

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

        btnCrear=findViewById(R.id.btnCreaLiga);
        btnCrear.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbarCrearLiga);
        setSupportActionBar(toolbar);
        // Opcional: Habilitar botón de "Atrás"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNombreLiga=findViewById(R.id.etNombreLigaFantasy);


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

    @Override
    public void onClick(View view) {

        String nombreLiga = etNombreLiga.getText().toString();
        int contadorLigas=0;

        if (view.getId() == R.id.btnCreaLiga) {
            if (!nombreLiga.isEmpty()){
                contadorLigas+=1;

            // Intent para devolver el contador a la actividad anterior
                Intent intent = new Intent();
                intent.putExtra("contadorLigas", contadorLigas);
                setResult(RESULT_OK, intent);

                finish();
                Toast.makeText(this, "Liga creada", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "No puedes crear una liga sin nombre", Toast.LENGTH_SHORT).show();
            }

        }

    }
}