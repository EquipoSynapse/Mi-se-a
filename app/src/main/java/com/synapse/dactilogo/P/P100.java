package com.synapse.dactilogo.P;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.synapse.dactilogo.R;

public class P100 extends AppCompatActivity {

    private ImageView cod0, imagenBasico1, imagenBasico2, imagenBasico3;
    private TextView textBasico, textPerfilTitle, textPremiumTitle;
    LinearLayout cod1, cod2,cod3,cod4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p100);  // Conectar con el layout XML existente

        // Referenciar los elementos del layout XML
        imagenBasico1 = findViewById(R.id.imagen_basico_1);
        imagenBasico2 = findViewById(R.id.imagen_basico_2);
        imagenBasico3 = findViewById(R.id.imagen_basico_3);
        cod0 = findViewById(R.id.ID1);
        cod1 = findViewById(R.id.ID2);
        cod2 = findViewById(R.id.ID3);
        cod3 = findViewById(R.id.ID4);
        cod4 = findViewById(R.id.ID5);

        // Listener para redirigir a P300 con la categoría Básico
        imagenBasico1.setOnClickListener(v -> abrirP300("Básico"));
        imagenBasico2.setOnClickListener(v -> abrirP300("Básico"));
        imagenBasico3.setOnClickListener(v -> abrirP300("Básico"));

        // Listener para redirigir a P300 con la categoría Perfil
        cod1.setOnClickListener(v -> abrirP300("Perfil"));
        cod2.setOnClickListener(v -> abrirP300("Perfil"));

        // Listener para redirigir a P300 con la categoría Premium
        cod3.setOnClickListener(v -> abrirP300("Premium"));
        cod4.setOnClickListener(v -> abrirP300("Premium"));

        cod0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P100.this, P13.class); // Intent para iniciar la actividad P1
                startActivity(intent); // Iniciar la nueva actividad
            }
        });
    }

    // Método para abrir la pantalla P300
    private void abrirP300(String categoria) {
        Intent intent = new Intent(P100.this, P300.class);
        intent.putExtra("categoria", categoria);  // Pasar la categoría seleccionada
        startActivity(intent);
    }
}
