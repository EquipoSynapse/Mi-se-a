package com.synapse.dactilogo.P;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.synapse.dactilogo.R;
import com.synapse.dactilogo.WelcomeActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class P5 extends AppCompatActivity {
    int REQUEST_CODE = 200;
    String Permisos = "DazzuXAXF.dzz";
    String contenido1 = "SI";
    String Separador = "OLR2";
    private Handler CARGARINTRO3;
    private ProgressBar progressBar;
    int Porcentaje = 0;
    int Tiempo = 0300;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p5);
        progressBar = findViewById(R.id.progressBar);
        CARGARINTRO3 =  new Handler();
        progressBar.setProgress(0);

        verificarPermisos();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {
        File Archivo1 =new  File(getExternalFilesDir(null), Permisos);

        if(Archivo1.exists()){
            try {
                FileInputStream fIn = new FileInputStream(Archivo1);
                InputStreamReader archivo = new InputStreamReader(fIn);
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                while (linea != null) {
                    todo = todo + linea + "";
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
                String[] CONTENEDOR1 = todo.split(Separador);
                String TerminosYCondiciones = CONTENEDOR1[0];
                if(TerminosYCondiciones.equals("SI")){
                    CARGAR();
                }else {
                    AceptarPermisos();
                }
            } catch (IOException e) {
                AceptarPermisos();
            }
        }else {
            AceptarPermisos();
        }






    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AceptarPermisos(){
        File Archivo1 =new File(getExternalFilesDir(null), Permisos);
        int PermisosInternet= ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int PermisosAlmacenamiento= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (PermisosInternet == PackageManager.PERMISSION_GRANTED && PermisosAlmacenamiento == PackageManager.PERMISSION_GRANTED){
        }else{
            requestPermissions(new String[]{Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            try {
                OutputStreamWriter osw = new OutputStreamWriter(
                        new FileOutputStream(Archivo1));
                osw.write(contenido1 + Separador + "x0x0");
                osw.flush();
                osw.close();
                CARGAR();
            } catch (IOException ioe) {
                Toast.makeText(P5.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void CARGAR() {
        while (Tiempo < 4300) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Porcentaje = Porcentaje + 5;
                    progressBar.setProgress(Porcentaje);
                }
            }, Tiempo);
            Tiempo = Tiempo + 200;
        }

        CARGARINTRO3.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(P5.this, P1.class);
                startActivity(intent);
                finish();

            }
        }, 4300);
    }

    public void  onBackPressed () {
        CARGARINTRO3.removeCallbacksAndMessages(null);
        finish();
    }
}