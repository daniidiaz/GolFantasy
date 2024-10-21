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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsuario;
    private EditText etContrasenia;
    private Button boton;
    private TextView linkRegistrar;

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

        etUsuario = findViewById(R.id.etUsuario);
        etContrasenia = findViewById(R.id.etContraseña);
        boton=findViewById(R.id.botonEntrar);
        boton.setOnClickListener(this);
        linkRegistrar = findViewById(R.id.linkRegistrar);
        linkRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.botonEntrar){
            String usuario = etUsuario.getText().toString();
            String contrasenia = etContrasenia.getText().toString();

            if (usuario.isEmpty() || contrasenia.isEmpty()){
                Toast.makeText(this, "Campos vacíos, rellene todos los campos", Toast.LENGTH_SHORT).show();
            } else{
                Intent intent=new Intent(this, PantallaJuegoPrincipal.class);
                startActivity(intent);

            }
        } else if (view.getId() == R.id.linkRegistrar) {
            Intent intent=new Intent(this, PaginaCrearUsuario.class);
            startActivity(intent);
        }

    }
}