package com.synapse.dactilogo.P;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synapse.dactilogo.R;

public class P10 extends AppCompatActivity {
    TextView cod3, cod4; // TextViews para mostrar nombre y profesión
    LinearLayout cod12;  // Layout para manejar el cierre de sesión
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p10);
        cod3 = findViewById(R.id.ID3);  // TextView para el nombre
        cod4 = findViewById(R.id.ID4);  // TextView para la profesión
        cod12 = findViewById(R.id.ID12); // Layout para cerrar sesión

        // Acceder a SharedPreferences para obtener el correo
        SharedPreferences sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String correoSanitizado = sharedPref.getString("correo", null);

        if (correoSanitizado != null) {
            // Acceder a Firebase usando el correo como clave
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");

            reference.child(correoSanitizado).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Extraer nombre y profesión del usuario desde Firebase
                        String nombre = snapshot.child("Nombres").getValue(String.class);
                        String profesion = snapshot.child("Profesión").getValue(String.class);

                        // Insertar los valores en los TextViews
                        cod3.setText(nombre != null ? nombre : "Sin nombre");
                        cod4.setText(profesion != null ? profesion : "Sin profesión");
                    } else {
                        Toast.makeText(P10.this, "No se encontró información del usuario.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(P10.this, "Error al cargar datos del usuario.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Si no hay un correo guardado, redirigir a la pantalla de inicio de sesión
            startActivity(new Intent(P10.this, P6.class));
            finish();
        }

        // Cerrar sesión al hacer clic en el Layout cod12
        cod12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Borrar las preferencias para cerrar la sesión
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.apply();

                // Redirigir a la pantalla de inicio de sesión
                Intent intent = new Intent(P10.this, P6.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar el stack de actividades
                startActivity(intent);
                finish(); // Finalizar la actividad actual
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}