package com.synapse.dactilogo.P;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synapse.dactilogo.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class P10 extends AppCompatActivity {
    ImageView cod2;
    TextView cod3, cod4; // TextViews para mostrar nombre y profesión
    LinearLayout cod5, cod6, cod12;  // Layout para manejar el cierre de sesión

    private static final int PICK_IMAGE_REQUEST = 1; // Código para la selección de imágenes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p10); // Imagen de perfil
        cod2 = findViewById(R.id.ID2);  // ImageView para la imagen de perfil
        cod3 = findViewById(R.id.ID3);  // TextView para el nombre
        cod4 = findViewById(R.id.ID4);  // TextView para la profesión
        cod5 = findViewById(R.id.ID5);  // Cambiar su Nombre de usuario mínimo de 10 caracteres
        cod6 = findViewById(R.id.ID6);  // Cambiar su profesión, mínimo 6 caracteres
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
                        String base64 = snapshot.child("FotoPerfil").getValue(String.class);

                        // Asignar la imagen de perfil usando Base64
                        if (base64 != null) {
                            byte[] decodedString = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            cod2.setImageBitmap(decodedByte);
                        }

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

        // Abrir la galería al hacer clic en la imagen de perfil
        cod2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        // Mostrar diálogo para cambiar nombre al tocar el layout cod5
        cod5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeNameDialog();
            }
        });

        // Mostrar diálogo para cambiar profesión al tocar el layout cod6
        cod6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeProfessionDialog();
            }
        });
    }

    // Método para abrir la galería
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    // Manejar el resultado de la selección de imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                cod2.setImageBitmap(bitmap);
                updateImageInFirebase(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar la imagen.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para actualizar la imagen en Firebase
    private void updateImageInFirebase(Uri imageUri) {
        // Convertir la imagen a Base64
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Cambiar el formato si es necesario
            byte[] bytes = baos.toByteArray();
            String base64Image = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);

            // Acceder a Firebase y actualizar el campo de la imagen
            SharedPreferences sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            String correoSanitizado = sharedPref.getString("correo", null);
            if (correoSanitizado != null) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");
                reference.child(correoSanitizado).child("FotoPerfil").setValue(base64Image);
                Toast.makeText(this, "Imagen actualizada correctamente.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al actualizar la imagen.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para mostrar diálogo para cambiar nombre
    private void showChangeNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar Nombre");

        // Crear EditText para el nuevo nombre
        final EditText input = new EditText(this);
        builder.setView(input);
        input.setHint("Escribe tu nuevo nombre");

        // Obtener el nombre actual
        input.setText(cod3.getText().toString());

        builder.setPositiveButton("Cambiar", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty() && newName.length() >= 10) {
                // Actualizar el nombre en Firebase
                updateUserNameInFirebase(newName);
                cod3.setText(newName); // Actualizar TextView
                Toast.makeText(P10.this, "Nombre actualizado correctamente.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(P10.this, "El nombre debe tener al menos 10 caracteres.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Método para actualizar el nombre en Firebase
    private void updateUserNameInFirebase(String newName) {
        SharedPreferences sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String correoSanitizado = sharedPref.getString("correo", null);
        if (correoSanitizado != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");
            reference.child(correoSanitizado).child("Nombres").setValue(newName);
        }
    }

    // Método para mostrar diálogo para cambiar profesión
    private void showChangeProfessionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar Profesión");

        // Crear EditText para la nueva profesión
        final EditText input = new EditText(this);
        builder.setView(input);
        input.setHint("Escribe tu nueva profesión");

        // Obtener la profesión actual
        input.setText(cod4.getText().toString());

        builder.setPositiveButton("Cambiar", (dialog, which) -> {
            String newProfession = input.getText().toString().trim();
            if (!newProfession.isEmpty() && newProfession.length() >= 6) {
                // Actualizar la profesión en Firebase
                updateUserProfessionInFirebase(newProfession);
                cod4.setText(newProfession); // Actualizar TextView
                Toast.makeText(P10.this, "Profesión actualizada correctamente.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(P10.this, "La profesión debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Método para actualizar la profesión en Firebase
    private void updateUserProfessionInFirebase(String newProfession) {
        SharedPreferences sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String correoSanitizado = sharedPref.getString("correo", null);
        if (correoSanitizado != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");
            reference.child(correoSanitizado).child("Profesión").setValue(newProfession);
        }
    }
}
