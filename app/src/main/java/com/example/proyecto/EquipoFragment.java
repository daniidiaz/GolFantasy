package com.example.proyecto;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class EquipoFragment extends Fragment {

    private LinearLayout lineaDefensas, lineaMediocentros, lineaDelanteros;
    private ImageView portero;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EquipoFragment() {
        // Required empty public constructor
    }

    public static EquipoFragment newInstance(String param1, String param2) {
        EquipoFragment fragment = new EquipoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipo, container, false);

        Spinner formacionSpinner = view.findViewById(R.id.spinner_formacion);
        lineaDefensas = view.findViewById(R.id.linea_defensas);
        lineaMediocentros = view.findViewById(R.id.linea_mediocentros);
        lineaDelanteros = view.findViewById(R.id.linea_delanteros);
        portero = view.findViewById(R.id.portero); // Portero fijo

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(),R.array.formaciones, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        formacionSpinner.setAdapter(adapter);

        formacionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String formacionSeleccionada = parent.getItemAtPosition(position).toString();
                cambiarFormacion(formacionSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona nada
            }
        });


        return view;
    }

    private void cambiarFormacion(String formacion) {
        lineaDefensas.removeAllViews();
        lineaMediocentros.removeAllViews();
        lineaDelanteros.removeAllViews();

        // Cambiar la disposición según la formación seleccionada
        if (formacion.equals("3-3-4")) {
            colocarJugadores(3, lineaDefensas);
            colocarJugadores(3, lineaMediocentros);
            colocarJugadores(4, lineaDelanteros);
        } else if (formacion.equals("3-4-3")) {
            colocarJugadores(3, lineaDefensas);
            colocarJugadores(4, lineaMediocentros);
            colocarJugadores(3, lineaDelanteros);
        } else if (formacion.equals("3-5-2")) {
            colocarJugadores(3, lineaDefensas);
            colocarJugadores(5, lineaMediocentros);
            colocarJugadores(2, lineaDelanteros);
        } else if (formacion.equals("3-6-1")) {
            colocarJugadores(3, lineaDefensas);
            colocarJugadores(6, lineaMediocentros);
            colocarJugadores(1, lineaDelanteros);
        } else if (formacion.equals("4-2-4")) {
            colocarJugadores(4, lineaDefensas);
            colocarJugadores(2, lineaMediocentros);
            colocarJugadores(4, lineaDelanteros);
        } else if (formacion.equals("4-3-3")) {
            colocarJugadores(4, lineaDefensas);
            colocarJugadores(3, lineaMediocentros);
            colocarJugadores(3, lineaDelanteros);
        } else if (formacion.equals("4-4-2")) {
            colocarJugadores(4, lineaDefensas);
            colocarJugadores(4, lineaMediocentros);
            colocarJugadores(2, lineaDelanteros);
        } else if (formacion.equals("4-5-1")) {
            colocarJugadores(4, lineaDefensas);
            colocarJugadores(5, lineaMediocentros);
            colocarJugadores(1, lineaDelanteros);
        } else if (formacion.equals("5-2-3")) {
            colocarJugadores(5, lineaDefensas);
            colocarJugadores(2, lineaMediocentros);
            colocarJugadores(3, lineaDelanteros);
        } else if (formacion.equals("5-3-2")) {
            colocarJugadores(5, lineaDefensas);
            colocarJugadores(3, lineaMediocentros);
            colocarJugadores(2, lineaDelanteros);
        } else if (formacion.equals("5-4-1")) {
            colocarJugadores(5, lineaDefensas);
            colocarJugadores(4, lineaMediocentros);
            colocarJugadores(1, lineaDelanteros);
        }

    }

    private void colocarJugadores(int cantidad, LinearLayout linea) {
        for (int i = 0; i < cantidad; i++) {
            ImageView jugador = new ImageView(requireActivity());
            jugador.setImageResource(R.drawable.jugador);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    200, 200, 1);

            params.setMargins(8, 0, 8, 0); //margenes entre fotos

            jugador.setLayoutParams(params);
            linea.addView(jugador);
        }
    }
}
