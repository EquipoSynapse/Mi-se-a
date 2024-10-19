package com.synapse.dactilogo.E;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.synapse.dactilogo.R;

import java.util.List;

public class PaqueteAdapter extends RecyclerView.Adapter<PaqueteAdapter.PaqueteViewHolder> {

    private Context context;
    private List<Paquete> paqueteList;
    private OnPaqueteClickListener listener;

    public PaqueteAdapter(Context context, List<Paquete> paqueteList, OnPaqueteClickListener listener) {
        this.context = context;
        this.paqueteList = paqueteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaqueteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_paquete, parent, false);
        return new PaqueteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaqueteViewHolder holder, int position) {
        Paquete paquete = paqueteList.get(position);
        holder.nombrePaquete.setText(paquete.getNombre());
        holder.autorPaquete.setText(paquete.getAutor());
        holder.pesoPaquete.setText("Peso: " + paquete.getPeso() + " MB");
        holder.valorPaquete.setText("Valor: $" + paquete.getValor());
        holder.cantidadSenias.setText("Señas: " + paquete.getCantidadSenias());


        holder.itemView.setOnClickListener(v -> listener.onPaqueteClick(paquete));
    }

    @Override
    public int getItemCount() {
        return paqueteList.size();
    }

    public static class PaqueteViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePaquete, autorPaquete, pesoPaquete, valorPaquete, cantidadSenias;


        public PaqueteViewHolder(@NonNull View itemView) {
            super(itemView);

            nombrePaquete = itemView.findViewById(R.id.nombre_paquete);
            autorPaquete = itemView.findViewById(R.id.autor_paquete);
            pesoPaquete = itemView.findViewById(R.id.peso_paquete);
            valorPaquete = itemView.findViewById(R.id.valor_paquete);
            cantidadSenias = itemView.findViewById(R.id.cantidad_senias);
        }
    }

    public interface OnPaqueteClickListener {
        void onPaqueteClick(Paquete paquete);
    }
}
