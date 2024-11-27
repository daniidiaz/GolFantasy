package com.example.proyecto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoticiasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticiasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private NoticiasAdapter adapter;
    private List<Noticia> noticias;

    public NoticiasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoticiasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoticiasFragment newInstance(String param1, String param2) {
        NoticiasFragment fragment = new NoticiasFragment();
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
           // mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        // Inicializa el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa la lista y el adaptador
        noticias = new ArrayList<>();
        adapter = new NoticiasAdapter(noticias);
        recyclerView.setAdapter(adapter);

        // Llama a la API para obtener noticias
        obtenerNoticiasDeAPI();

        return view;
    }

    private void obtenerNoticiasDeAPI() {
        NewsApiService apiService = NewsApiClient.getClient().create(NewsApiService.class);

        Call<ApiResponse> call = apiService.obtenerNoticias("LALIGA FANTASY", "72693b8e807142edb14ba3471e6d8a2b", "es");
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Article> articles = response.body().getArticles();
                    for (Article article : articles) {
                    // Crea una nueva instancia de Noticia con todos los datos
                        Noticia noticia = new Noticia(
                                article.getTitle(),
                                article.getDescription(),
                                article.getUrl(),
                                article.getUrlToImage(),
                                article.getContent(),
                                article.getAuthor()
                        );
                        noticias.add(noticia);
                    }
                    adapter.notifyDataSetChanged(); // Actualizar el RecyclerView

                } else {
                    Log.e("NoticiasFragment", "Respuesta no exitosa o cuerpo nulo");
                    Toast.makeText(getContext(), "No se encontraron noticias", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error al obtener noticias", Toast.LENGTH_SHORT).show();
            }
        });
    }

}