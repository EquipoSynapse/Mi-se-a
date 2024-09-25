package com.synapse.dactilogo.P;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.synapse.dactilogo.M.P4;
import com.synapse.dactilogo.R;

import java.time.LocalDate;
import java.util.HashMap;

public class P8 extends AppCompatActivity {
    EditText cod1;
    Spinner cod2;
    TextView cod3;

    FirebaseAuth firebaseAuth ;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p8);

        Intent intent = getIntent();

        cod1 = findViewById(R.id.ID1);
        cod2 = findViewById(R.id.ID2);
        cod3 = findViewById(R.id.ID3);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(P8.this);

        cod3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = intent.getStringExtra("clave_correo");
                String pass = intent.getStringExtra("clave_contraseña");
                REGISTRAR(correo,pass);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Profeciones,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cod2.setAdapter(adapter);

        cod2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No se hace nada si no se selecciona nada
            }
        });

    }

    private void REGISTRAR(String correo, String pass) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        Intent intent = getIntent();
        String modoElegido = intent.getStringExtra("modo");

        firebaseAuth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            String uid = user.getUid();
                            String nombres = cod1.getText().toString();
                            String profesión = cod2.getSelectedItem().toString();

                            // Verificar que se ha seleccionado un valor en ambos Spinners
                            if (!nombres.contains(" ") | nombres.length() < 8) {
                                Toast.makeText(P8.this, "Por favor escriba su nombre real", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            HashMap<Object, String> DatosUsuario = new HashMap<>();

                            DatosUsuario.put("ID", uid);
                            DatosUsuario.put("Correo", correo);
                            DatosUsuario.put("Contraseña", pass);
                            DatosUsuario.put("Nombres", nombres);
                            DatosUsuario.put("Modo", modoElegido);
                            DatosUsuario.put("Profesión", profesión);
                            DatosUsuario.put("Monedas", "0");
                            DatosUsuario.put("FotoPerfil", "");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference reference = database.getReference("USUARIOS_DE_APP");
                            String correoSanitizado = correo.replace(".", ",");
                            reference.child(correoSanitizado).setValue(DatosUsuario);

                            // Guardar el estado de sesión en SharedPreferences
                            SharedPreferences sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("correo", correoSanitizado);
                            editor.putBoolean("loggedIn", true);
                            editor.apply();
                            progressDialog.dismiss();
                            if(modoElegido.equals("3")){
                                startActivity(new Intent(P8.this, P3.class));
                            }else {
                                startActivity(new Intent(P8.this, P4.class));
                            }
                        } else {
                            Toast.makeText(P8.this, "Algo Ha Salido Mal", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(P8.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}