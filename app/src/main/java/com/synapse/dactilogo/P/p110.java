package com.synapse.dactilogo.P;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synapse.dactilogo.R;

public class p110 extends AppCompatActivity {

    private EditText searchBar;
    private Button backButton;
    private DatabaseReference mDatabase; // Referencia a la base de datos de Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p110);

        // Inicialización de Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Vinculación de vistas
        backButton = findViewById(R.id.backButton);
        searchBar = findViewById(R.id.searchBar);

        // Configuración del botón de regreso
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Configurar los botones de cada categoría
        setupCategoryButtons();
    }

    private void setupCategoryButtons() {
        // Por ejemplo, configuración de botones para la primera categoría
        Button buttonCat1 = findViewById(R.id.butt);
        buttonCat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón de la categoría 1
                savePackageToFirebase("Abecedarios extranjeros", "Paquete 1");
            }
        });

        // Configura aquí más botones para otras categorías
    }

    private void savePackageToFirebase(String categoryName, String packageName) {
        // Guarda o actualiza información en Firebase
        mDatabase.child("Categories").child(categoryName).setValue(packageName);
    }
}
