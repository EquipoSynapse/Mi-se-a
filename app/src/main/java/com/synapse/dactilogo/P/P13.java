package com.synapse.dactilogo.P;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synapse.dactilogo.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class P13 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_FILE_REQUEST = 2;

    private ImageView imageViewPerfil;
    private EditText editTextNombrePaquete, editTextAutorPaquete, editTextPrecio;
    private TextView textViewPeso, textViewCantidadLineas;

    private String fotoPerfilBase64;
    private String archivoBase64;
    private long archivoPesoMB;
    private int cantidadLineasArchivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p13);

        imageViewPerfil = findViewById(R.id.ID1);
        editTextNombrePaquete = findViewById(R.id.ID2);
        editTextAutorPaquete = findViewById(R.id.ID3);
        textViewPeso = findViewById(R.id.ID5);
        textViewCantidadLineas = findViewById(R.id.ID6);
        editTextPrecio = findViewById(R.id.ID7);

        imageViewPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        findViewById(R.id.ID4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        findViewById(R.id.ID8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatosFirebase();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    private void openFilePicker() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona un archivo"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            if (requestCode == PICK_IMAGE_REQUEST) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    fotoPerfilBase64 = bitmapToBase64(bitmap);

                    // Actualizar el ImageView
                    imageViewPerfil.setImageBitmap(base64ToBitmap(fotoPerfilBase64));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_FILE_REQUEST) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    archivoBase64 = inputStreamToBase64(inputStream);
                    archivoPesoMB = inputStream.available() / (1024 * 1024); // Peso en MB
                    textViewPeso.setText(archivoPesoMB + " MB");

                    // Contar líneas en el archivo
                    cantidadLineasArchivo = contarLineasArchivo(uri);
                    textViewCantidadLineas.setText(String.valueOf(cantidadLineasArchivo));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private String inputStreamToBase64(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    private int contarLineasArchivo(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        int lineCount = 0;
        while (reader.readLine() != null) {
            lineCount++;
        }
        reader.close();
        return lineCount;
    }

    private void enviarDatosFirebase() {
        String nombrePaquete = editTextNombrePaquete.getText().toString();
        String autorPaquete = editTextAutorPaquete.getText().toString();
        String precioCordobas = editTextPrecio.getText().toString();

        //if (nombrePaquete.isEmpty() || autorPaquete.isEmpty() || precioCordobas.isEmpty()) {
            // Validación para asegurarse de que los campos no estén vacíos
            //return;
        //}

        DatabaseReference tiendaRef = FirebaseDatabase.getInstance().getReference().child("TIENDA").child("2D").child(nombrePaquete);

        tiendaRef.child("FotoPerfil").setValue(fotoPerfilBase64);
        tiendaRef.child("AutorPaquete").setValue(autorPaquete);
        tiendaRef.child("ArchivoBase64").setValue(archivoBase64);
        tiendaRef.child("PesoArchivo").setValue(archivoPesoMB + " MB");
        tiendaRef.child("CantidadLineas").setValue(String.valueOf(cantidadLineasArchivo));
        tiendaRef.child("PrecioCordobas").setValue(precioCordobas);
    }
}
