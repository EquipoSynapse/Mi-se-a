package com.synapse.dactilogo.P;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.dactilogo.R;

public class P7 extends AppCompatActivity {
    EditText cod1, cod2;
    TextView cod3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p7);
        cod1 = findViewById(R.id.ID1);
        cod2 = findViewById(R.id.ID2);
        cod3 = findViewById(R.id.ID3);

        Intent intent = getIntent();
        String modoElegido = intent.getStringExtra("modo");

        cod3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = cod1.getText().toString();
                String pass = cod2.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    cod1.setError("El Correo No Es Valido, Por Favor Introduce Un Correo Valido");
                    cod1.setFocusable(true);
                }else if (pass.length()<6){
                    cod2.setError("La Contraseña Debe Tener Mas De 6 Digitos");
                    cod2.setFocusable(true);
                }else {
                    Intent intent = new Intent(P7.this, P8.class);
                    intent.putExtra("clave_correo", correo);
                    intent.putExtra("clave_contraseña", pass);
                    intent.putExtra("modo", modoElegido);
                    startActivity(intent);
                }
            }
        });
    }
}