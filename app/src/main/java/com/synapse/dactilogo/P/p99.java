package com.synapse.dactilogo.P;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synapse.dactilogo.R;

public class p99 extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button enterButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p99);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        enterButton = findViewById(R.id.enterButton);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                checkCategory(user.getUid());
                            } else {
                                Toast.makeText(p99.this, "Autenticación fallida.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void checkCategory(String userId) {
        databaseReference.child("users").child(userId).child("category")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String category = task.getResult().getValue(String.class);
                        Toast.makeText(p99.this, "Categoría: " + category, Toast.LENGTH_LONG).show();
                        // Procede según la categoría
                    } else {
                        Toast.makeText(p99.this, "Error al obtener la categoría.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
