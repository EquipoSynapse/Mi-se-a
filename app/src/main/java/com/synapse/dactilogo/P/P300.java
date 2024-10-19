package com.synapse.dactilogo.P;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.synapse.dactilogo.M.Paquete;
import com.synapse.dactilogo.M.PaqueteAdapter;
import com.synapse.dactilogo.R;

import java.util.ArrayList;
import java.util.List;

public class P300 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaqueteAdapter adapter;
    private List<Paquete> paqueteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Cambié onCreateState a onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p300);  // Conectado con 'p300.xml'

        SearchView searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recycler_view_paquetes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lista de paquetes de ejemplo
        paqueteList = new ArrayList<>();
        paqueteList.add(new Paquete("Paquete 1", "Autor 1", 10.5, 5, 100));
        paqueteList.add(new Paquete("Paquete 2", "Autor 2", 8.2, 6, 120));
        paqueteList.add(new Paquete("Paquete 3", "Autor 3", 15.7, 7, 200));

        // Configurar el adaptador
        adapter = new PaqueteAdapter(this, paqueteList, this::mostrarDialogoPaquete);
        recyclerView.setAdapter(adapter);

        // Listener para el SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarPaquetes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarPaquetes(newText);
                return false;
            }
        });
    }

    // Filtrar la lista de paquetes según la búsqueda
    private void filtrarPaquetes(String query) {
        List<Paquete> listaFiltrada = new ArrayList<>();
        for (Paquete paquete : paqueteList) {
            if (paquete.getNombre().toLowerCase().contains(query.toLowerCase())) {
                listaFiltrada.add(paquete);
            }
        }
        adapter = new PaqueteAdapter(this, listaFiltrada, this::mostrarDialogoPaquete);
        recyclerView.setAdapter(adapter);
    }

    // Mostrar un diálogo cuando se selecciona un paquete
    private void mostrarDialogoPaquete(Paquete paquete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Descargar " + paquete.getNombre())
                .setMessage("¿Quieres descargar e instalar este paquete?")
                .setPositiveButton("Descargar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Simular instalación
                        Toast.makeText(P300.this, "Descargando " + paquete.getNombre(), Toast.LENGTH_SHORT).show();

                        // Simular la creación de un nuevo paquete después de la descarga
                        Paquete nuevoPaquete = new Paquete("Nuevo Paquete", "Autor Simulado", 5.0, 8, 150);
                        paqueteList.add(nuevoPaquete);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(P300.this, "Instalación completada", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
