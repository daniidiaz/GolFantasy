package com.example.proyecto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UnirseLiga extends AppCompatActivity implements View.OnClickListener{

    private Button btIDLiga;
    private TextView tvUnirseLiga;
    private EditText etIDLiga;
    private Toolbar tbUnirseLiga;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unirse_liga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvUnirseLiga = findViewById(R.id.tvUnirseLiga);
        etIDLiga = findViewById(R.id.etIDLiga);

        tbUnirseLiga = findViewById(R.id.toolbarInicio);
        setSupportActionBar(tbUnirseLiga);

        btIDLiga = findViewById(R.id.btIDLiga);
        btIDLiga.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btIDLiga) {
            Intent i = new Intent(this, PantallaJuegoPrincipal.class);
            startActivity(i);
        }
    }
}
