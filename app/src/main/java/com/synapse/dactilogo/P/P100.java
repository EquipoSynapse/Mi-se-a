package com.synapse.dactilogo.P;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.synapse.dactilogo.R;

public class P100 extends AppCompatActivity {

    private ImageView imagenBasico1, imagenBasico2, imagenBasico3, imagenPerfil1, imagenPerfil2, imagenPremium1, imagenPremium2;
    private TextView textBasico, textPerfilTitle, textPremiumTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p100);  // Conectar con el layout XML existente

        // Referenciar los elementos del layout XML
        imagenBasico1 = findViewById(R.id.imagen_basico_1);
        imagenBasico2 = findViewById(R.id.imagen_basico_2);
        imagenBasico3 = findViewById(R.id.imagen_basico_3);
        imagenPerfil1 = findViewById(R.id.imagen_perfil_1);
        imagenPerfil2 = findViewById(R.id.imagen_perfil_2);
        imagenPremium1 = findViewById(R.id.imagen_premium_1);
        imagenPremium2 = findViewById(R.id.imagen_premium_2);
        textBasico = findViewById(R.id.text_basico);
        textPerfilTitle = findViewById(R.id.text_perfil_title);
        textPremiumTitle = findViewById(R.id.text_premium_title);

        // Cargar imágenes desde drawable (en lugar de Firebase)
        imagenBasico1.setImageResource(R.drawable.b1);
        imagenBasico2.setImageResource(R.drawable.b2);
        imagenBasico3.setImageResource(R.drawable.b3);

        imagenPerfil1.setImageResource(R.drawable.p1);
        imagenPerfil2.setImageResource(R.drawable.p2);

        imagenPremium1.setImageResource(R.drawable.pr1);
        imagenPremium2.setImageResource(R.drawable.pr2);

        // Listener para redirigir a P300 con la categoría Básico
        imagenBasico1.setOnClickListener(v -> abrirP300("Básico"));
        imagenBasico2.setOnClickListener(v -> abrirP300("Básico"));
        imagenBasico3.setOnClickListener(v -> abrirP300("Básico"));

        // Listener para redirigir a P300 con la categoría Perfil
        imagenPerfil1.setOnClickListener(v -> abrirP300("Perfil"));
        imagenPerfil2.setOnClickListener(v -> abrirP300("Perfil"));

        // Listener para redirigir a P300 con la categoría Premium
        imagenPremium1.setOnClickListener(v -> abrirP300("Premium"));
        imagenPremium2.setOnClickListener(v -> abrirP300("Premium"));
    }

    // Método para abrir la pantalla P300
    private void abrirP300(String categoria) {
        Intent intent = new Intent(P100.this, P300.class);
        intent.putExtra("categoria", categoria);  // Pasar la categoría seleccionada
        startActivity(intent);
    }
}
