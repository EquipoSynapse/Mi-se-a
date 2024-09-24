package com.synapse.dactilogo.P;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synapse.dactilogo.R;

import java.util.Calendar;
import java.util.HashMap;

public class p100 extends AppCompatActivity {

    private EditText Nombres, correoEditText, contrasenaEditText, confirmarContrasenaEditText;
    private TextView Edad;
    private Button REGISTRAUSUARIO;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p100);  // Asegúrate de que el layout se llama p100.xml

        // Inicializar Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Vincular los elementos de la UI con sus IDs
        Nombres = findViewById(R.id.Nombres);
        Edad = findViewById(R.id.Edad);
        correoEditText = findViewById(R.id.correoEditText);
        contrasenaEditText = findViewById(R.id.contrasenaEditText);
        confirmarContrasenaEditText = findViewById(R.id.confirmarContrasenaEditText);
        REGISTRAUSUARIO = findViewById(R.id.REGISTRARUSUARIO);

        // Establecer el onClickListener para el TextView de la edad
        Edad.setOnClickListener(this::mostrarCalendario);

        // Establecer el onClickListener para el botón de registro
        REGISTRAUSUARIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String correo = correoEditText.getText().toString();
        String pass = contrasenaEditText.getText().toString();
        String confirmPass = confirmarContrasenaEditText.getText().toString();

        if (!pass.equals(confirmPass)) {
            Toast.makeText(p100.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");
                        ref.child(user.getUid()).setValue(crearDatosUsuario());
                        Toast.makeText(p100.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        // Aquí puedes iniciar otra actividad o hacer lo que necesites tras el registro
                    } else {
                        Toast.makeText(p100.this, "Algo ha salido mal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private HashMap<String, String> crearDatosUsuario() {
        HashMap<String, String> datosUsuario = new HashMap<>();
        datosUsuario.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        datosUsuario.put("Correo", correoEditText.getText().toString());
        datosUsuario.put("Contraseña", contrasenaEditText.getText().toString());
        datosUsuario.put("Nombres", Nombres.getText().toString());
        datosUsuario.put("Edad", Edad.getText().toString());
        return datosUsuario;
    }

    private void mostrarCalendario(View view) {
        final Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int day = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year1, monthOfYear, dayOfMonth) -> {
                    String fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    Edad.setText(fechaSeleccionada);
                }, year, month, day);
        datePickerDialog.show();
    }
}