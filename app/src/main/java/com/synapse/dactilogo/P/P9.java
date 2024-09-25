package com.synapse.dactilogo.P;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synapse.dactilogo.M.P3;
import com.synapse.dactilogo.R;

public class P9 extends AppCompatActivity {
    EditText cod1, cod2;
    TextView cod3;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p9);
        cod1 = findViewById(R.id.ID1);
        cod2 = findViewById(R.id.ID2);
        cod3 = findViewById(R.id.ID3);

        progressDialog = new ProgressDialog(P9.this);

        cod3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setCancelable(false);
                progressDialog.show();
                String correo = cod1.getText().toString();
                String pass = cod2.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    cod1.setError("Correo Invalido");
                    cod1.setFocusable(true);
                }else if (pass.length()<6){
                    cod2.setError("La Contraseña Debe Tener Mas De 6 Digitos");
                    cod2.setFocusable(true);
                }else {
                    login(correo, pass);
                }


            }
        });
    }


    public void login(String correo, String pass) {
        if (correo == null || pass == null || correo.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Correo o contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            return;
        }

        String correoSanitizado = correo.replace(".", ",");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP").child(correoSanitizado);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("Contraseña").getValue(String.class);
                    if (storedPassword != null && storedPassword.equals(pass)) {

                        // Guardar el estado de sesión en SharedPreferences
                        SharedPreferences sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("correo", correoSanitizado);
                        editor.putBoolean("loggedIn", true);
                        editor.apply();

                        // Login exitoso
                        progressDialog.dismiss();
                        startActivity(new Intent(P9.this, P3.class));
                    } else {
                        // Contraseña incorrecta
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // El correo no existe
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "El correo no está registrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error en la base de datos
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error de base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}