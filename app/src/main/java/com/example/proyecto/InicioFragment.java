package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragment extends Fragment implements View.OnClickListener {

    private LeagueAdapter leagueAdapter;
    private Button btnIrACrearLiga;
    private TextView tvLigasCreadas;
    private static final int CREACION_LIGA_REQUEST = 1; // Constante para identificar la solicitud


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InicioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        btnIrACrearLiga=view.findViewById(R.id.btnCrearLiga);
        btnIrACrearLiga.setOnClickListener(this);

        // Configura el RecyclerView
        RecyclerView leagueRecyclerView = view.findViewById(R.id.leagueRecyclerView);
        leagueRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        leagueAdapter = new LeagueAdapter();
        leagueRecyclerView.setAdapter(leagueAdapter);

        tvLigasCreadas = view.findViewById(R.id.tvLigasCreadas);


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnCrearLiga){
            Intent intent = new Intent(getActivity(), CreacionDeLigaFantasy.class);
            startActivity(intent);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREACION_LIGA_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            int contadorLigas = data.getIntExtra("contadorLigas", 0);
            actualizarLigas(contadorLigas);
        }
    }

    // Método para actualizar el contador de ligas
    public void actualizarLigas(int contadorLigas) {
        if (tvLigasCreadas != null) {
            tvLigasCreadas.setText("Participas en " + contadorLigas + " ligas");
        }
    }
}