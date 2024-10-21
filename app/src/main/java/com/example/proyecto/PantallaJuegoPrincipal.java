package com.example.proyecto;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyecto.databinding.ActivityPantallaJuegoPrincipalBinding;

public class PantallaJuegoPrincipal extends AppCompatActivity {


    ActivityPantallaJuegoPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPantallaJuegoPrincipalBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        remplazarFragment(new InicioFragment());

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.inicio) {
                remplazarFragment(new InicioFragment());

            } else if (item.getItemId() == R.id.clasificacion) {
                remplazarFragment(new ClasificacionFragment());

            } else if (item.getItemId() == R.id.equipo) {
                remplazarFragment(new EquipoFragment());

            } else if (item.getItemId() == R.id.mercado) {
                remplazarFragment(new MercadoFragment());

            } else if (item.getItemId() == R.id.noticias) {
                remplazarFragment(new NoticiasFragment());
            }

            return  true;
        });

    }

    private void remplazarFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();


    }


}