package com.synapse.dactilogo.P;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.dactilogo.R;

public class P6 extends AppCompatActivity {
    TextView cod1, cod2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p6);

        Intent intent = getIntent();
        String modoElegido = intent.getStringExtra("modo");

        cod1 = findViewById(R.id.ID1);
        cod2 = findViewById(R.id.ID2);

        cod1.setOnClickListener(view -> {
            Intent i = new Intent(P6.this, P9.class);
            i.putExtra("modo", modoElegido);
            startActivity(i); // Lanzar la nueva actividad después de la animación
        });

        cod2.setOnClickListener(view -> {
            Intent i = new Intent(P6.this, P100.class);
            i.putExtra("modo", modoElegido);
            startActivity(i); // Lanzar la nueva actividad después de la animación
        });
    }
}