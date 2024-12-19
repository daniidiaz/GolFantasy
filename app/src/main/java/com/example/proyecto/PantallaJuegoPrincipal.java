package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyecto.databinding.ActivityPantallaJuegoPrincipalBinding;

public class PantallaJuegoPrincipal extends AppCompatActivity {

    ActivityPantallaJuegoPrincipalBinding binding;
    Toolbar toolbarInicio;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    private String idUsuario; // Variable para almacenar el ID del usuario

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

        // Recibir el ID del usuario desde el Intent
        idUsuario = getIntent().getStringExtra("usuarioId");

        toolbarInicio = findViewById(R.id.toolbarInicio);
        setSupportActionBar(toolbarInicio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);

        //ImageButton btDesplegableInicio = findViewById(R.id.btDesplegableInicio);
        Button btnCrearLiga = findViewById(R.id.btnCrearLiga);
        Button btnUnirseLiga = findViewById(R.id.btnUnirseLiga);

        // Configura el ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarInicio, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

       // btDesplegableInicio.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

      /*  btnCrearLiga.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaJuegoPrincipal.this, CreacionDeLigaFantasy.class);
            intent.putExtra("usuarioId", idUsuario); // Pasar el ID del usuario
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });*/

      /*  btnUnirseLiga.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaJuegoPrincipal.this, UnirseLiga.class);
            intent.putExtra("usuarioId", idUsuario); // Pasar el ID del usuario
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });*/

        // Reemplazar el fragmento inicial con el ID del usuario
        remplazarFragment(new InicioFragment(), idUsuario);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.inicio) {
                remplazarFragment(new InicioFragment(), idUsuario);
            } else if (item.getItemId() == R.id.clasificacion) {
                remplazarFragment(new ClasificacionFragment(), idUsuario);
            } else if (item.getItemId() == R.id.equipo) {
                remplazarFragment(new EquipoFragment(), idUsuario);
//            } else if (item.getItemId() == R.id.mercado) {
//                remplazarFragment(new MercadoFragment(), idUsuario);
            } else if (item.getItemId() == R.id.noticias) {
                remplazarFragment(new NoticiasFragment(), idUsuario);
            }
            return true;
        });
    }

    private void remplazarFragment(Fragment fragment, String idUsuario) {
        // Crear un Bundle con el ID del usuario
        Bundle bundle = new Bundle();
        bundle.putString("usuarioId", idUsuario);

        // Pasar el Bundle al fragmento
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
