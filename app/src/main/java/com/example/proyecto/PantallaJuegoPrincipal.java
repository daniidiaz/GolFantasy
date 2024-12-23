package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.google.firebase.firestore.FirebaseFirestore;

public class PantallaJuegoPrincipal extends AppCompatActivity {

    ActivityPantallaJuegoPrincipalBinding binding;
    Toolbar toolbarInicio;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    private String idUsuario; // Variable para almacenar el ID del usuario
    private TextView presupuestoTextView;
    private ControladorBBDD controladorBBDD;
    private TextView tvNombreUsuario;
    private TextView tvCorreoElectronico;
    private TextView tvPuntuacion;


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
        controladorBBDD = new ControladorBBDD();
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvCorreoElectronico = findViewById(R.id.tvCorreoElectronico);
        tvPuntuacion = findViewById(R.id.tvPuntuacion);

        toolbarInicio = findViewById(R.id.toolbarInicio);
        setSupportActionBar(toolbarInicio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);

        presupuestoTextView = findViewById(R.id.presupuestoTextView);
        mostrarPresupuesto(idUsuario);

        // Configura el ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarInicio, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // Reemplazar el fragmento inicial con el ID del usuario
        remplazarFragment(new InicioFragment(), idUsuario);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.inicio) {
                remplazarFragment(new InicioFragment(), idUsuario);
            } else if (item.getItemId() == R.id.clasificacion) {
                remplazarFragment(new ClasificacionFragment(), idUsuario);
            } else if (item.getItemId() == R.id.equipo) {
                remplazarFragment(new EquipoFragment(), idUsuario);
            } else if (item.getItemId() == R.id.mercado) {
              remplazarFragment(new MercadoFragment(), idUsuario);
            } else if (item.getItemId() == R.id.noticias) {
                remplazarFragment(new NoticiasFragment(), idUsuario);
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idUsuario != null) { // Verificar si idUsuario es nulo
            mostrarPresupuesto(idUsuario);
        }

        obtenerUsuarioPorId(idUsuario, new UsuarioCallback() {
            @Override
            public void onSuccess(Usuario usuario) {
                tvNombreUsuario.setText(usuario.getNombreUsuario());
                tvCorreoElectronico.setText(usuario.getCorreo());
                tvPuntuacion.setText("Puntuación: " + usuario.getPuntuacion());
                Log.d("Usuario", "Nombre: " + usuario.getNombreUsuario());
            }

            @Override
            public void onError(Exception e) {
                // Manejar el error
                Log.e("Usuario", "Error al obtener usuario: " + e.getMessage());
            }
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

    private void mostrarPresupuesto(String usuarioId) {
        controladorBBDD.getPresupuestoDeUsuario(usuarioId, new ControladorBBDD.PresupuestoCallback() {
            @Override
            public void onSuccess(int presupuesto) {
                presupuestoTextView.setText(String.valueOf(presupuesto) + " €");
            }

            @Override
            public void onError(Exception e) {
                Log.e("MainActivity", "Error al obtener presupuesto: " + e.getMessage());
            }
        });
    }
    public void actualizarPresupuestoEnToolbar(String usuarioId) {
        mostrarPresupuesto(usuarioId);
    }

    public void obtenerUsuarioPorId(String idUsuario, UsuarioCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios").document(idUsuario)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Usuario usuario = documentSnapshot.toObject(Usuario.class);
                        callback.onSuccess(usuario);
                    } else {
                        callback.onError(new Exception("Usuario no encontrado"));
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onError(e);
                });
    }

    // Interfaz para el callback
    public interface UsuarioCallback {
        void onSuccess(Usuario usuario);
        void onError(Exception e);
    }


}
