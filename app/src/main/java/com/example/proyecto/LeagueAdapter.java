package com.example.proyecto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.LeagueViewHolder>{
    private final List<LigaFicticia> listaLigasJuego = new ArrayList<>();

    // Método para añadir una liga
    public void addLeague(LigaFicticia liga) {
        listaLigasJuego.add(liga);//se añade una nueva liga a la lista
        notifyItemInserted(listaLigasJuego.size() - 1);//notifica al adaptador que se ha insertado un nuevo elemento
    }

    @NonNull
    @Override
    public LeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new LeagueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueViewHolder holder, int position) {
        holder.bind(listaLigasJuego.get(position));
    }

    @Override
    public int getItemCount() {
        return listaLigasJuego.size();
    }

    static class LeagueViewHolder extends RecyclerView.ViewHolder {
        private final TextView leagueNameTextView;

        public LeagueViewHolder(@NonNull View itemView) {
            super(itemView);
            leagueNameTextView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(LigaFicticia liga) {
            leagueNameTextView.setText(liga.getNombre());
        }
    }
}
