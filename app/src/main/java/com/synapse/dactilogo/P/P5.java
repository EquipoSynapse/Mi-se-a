package com.synapse.dactilogo.P;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.synapse.dactilogo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import pl.droidsonroids.gif.GifDrawable;

public class P5 extends AppCompatActivity {
    int REQUEST_CODE = 200;
    String Permisos = "DazzuXAXF.dzz";
    String contenido1 = "SI";
    String Separador = "OLR2";
    private Handler cARGARINTRO3;
    private ProgressBar progressBar;
    int Porcentaje = 0;
    int Tiempo = 300; // 0300 no es un valor válido, lo he corregido a 300

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p5);
        progressBar = findViewById(R.id.progressBar);
        cARGARINTRO3 = new Handler();
        progressBar.setProgress(0);

        verificarPermisos(); // Verifica los permisos al inicio
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {
        // Archivo de permisos
        File archivo1 = new File(getExternalFilesDir(null), Permisos);

        if (archivo1.exists()) {
            try {
                FileInputStream fIn = new FileInputStream(archivo1);
                InputStreamReader archivo = new InputStreamReader(fIn);
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                StringBuilder todo = new StringBuilder();
                while (linea != null) {
                    todo.append(linea);
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
                String[] contenedor1 = todo.toString().split(Separador);
                String terminosYCondiciones = contenedor1[0];
                if (terminosYCondiciones.equals("SI")) {
                    CARGAR();
                } else {
                    AceptarPermisos();
                }
            } catch (IOException e) {
                AceptarPermisos();
            }
        } else {
            AceptarPermisos();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AceptarPermisos() {
        int permisosInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int permisosAlmacenamiento = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Solicita permisos solo si no están concedidos
        if (permisosInternet != PackageManager.PERMISSION_GRANTED || permisosAlmacenamiento != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            CARGAR(); // Si ya tiene permisos, llama a CARGAR directamente
        }
    }

    // Manejo de resultado de solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permisos concedidos
                guardarPermisos();
                CARGAR();
            } else {
                // Permisos denegados, muestra un mensaje o realiza una acción adecuada
                Toast.makeText(this, "Permisos necesarios para continuar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guardarPermisos() {
        File archivo1 = new File(getExternalFilesDir(null), Permisos);
        try {
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(archivo1));
            osw.write(contenido1 + Separador + "x0x0");
            osw.flush();
            osw.close();
        } catch (IOException ioe) {
            Toast.makeText(P5.this, "Ha ocurrido un error al guardar permisos", Toast.LENGTH_SHORT).show();
        }
    }

    private void CARGAR() {
        // Simulación de carga incrementando el progreso cada 200 ms
        cARGARINTRO3.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Porcentaje < 100) {
                    Porcentaje += 5;
                    progressBar.setProgress(Porcentaje);
                    CARGAR(); // Llama recursivamente hasta llegar al 100%
                } else {
                    // Cuando el progreso llega a 100, cambiar a la siguiente actividad
                    Intent intent = new Intent(P5.this, P1.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 200);
    }

    @Override
    public void onBackPressed() {
        // Detener la ejecución del Handler al retroceder
        cARGARINTRO3.removeCallbacksAndMessages(null);
        finish();

        File gifFile = new File(getExternalFilesDir(null), "yourfile.gif");
        if (gifFile.exists()) {
            try {
                GifDrawable gifDrawable = new GifDrawable(gifFile);
                // Establecer el gifDrawable en una ImageView o donde corresponda
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar el archivo GIF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "El archivo GIF no existe", Toast.LENGTH_SHORT).show();
        }

    }
}