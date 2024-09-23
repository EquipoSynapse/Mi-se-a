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
    int REQUEST_CODE = 200; // Código de solicitud para los permisos (usado al solicitar permisos)
    private Handler H1; // Handler para gestionar tareas en segundo plano y cambiar de actividad sin conflictos
    private ProgressBar cod1; // Barra de progreso que se mostrará durante la carga
    int I1 = 0; // Valor inicial del porcentaje de progreso de la barra
    int I2 = 300; // Valor inicial (en milisegundos) que toma la barra de progreso para completarse
    @RequiresApi(api = Build.VERSION_CODES.M) // Indicamos que el código requiere API 23 (Marshmallow) o superior
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p5); //Conectamos con el loyut
        
        cod1 = findViewById(R.id.progressBar); //Vinculamos la barra progresiva
        H1 =  new Handler(); //Un handler unico para cambiar de activad y así no entre en conflicto si el usuario cancela la actividad antes
        cod1.setProgress(0); //Iniciamos la barra progresiva desde 0
        verificarPermisos(); //Verificamos si los permisos están activos
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {
        int permisoInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int permisoAlmacenamiento = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Verificar si ambos permisos están otorgados
        if (permisoInternet == PackageManager.PERMISSION_GRANTED && permisoAlmacenamiento == PackageManager.PERMISSION_GRANTED) {
            CARGAR(); // Si los permisos están otorgados, continúa con la carga
        } else {
            // Si no, solicita los permisos
            AceptarPermisos();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AceptarPermisos() {
        // Solicitar los permisos que faltan
        requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    private void CARGAR() {
        // Bucle que aumenta progresivamente el valor de I2 hasta 4300, simulando una carga gradual
        while (I2 < 4300) {
            // Crear un Handler que ejecute el Runnable con un retraso para actualizar el progreso
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    I1 = I1 + 5; // Incrementar el valor de I1 en 5 cada vez
                    cod1.setProgress(I1); // Actualizar el progreso de la barra de progreso (cod1)
                }
            }, I2);
            I2 = I2 + 200; // Aumentar el tiempo de espera en 200 ms para cada paso del progreso
        }

        // Ejecutar un Runnable después de 4300 ms para cambiar a la siguiente actividad
        H1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(P5.this, P1.class); // Intent para iniciar la actividad P1
                startActivity(intent); // Iniciar la nueva actividad
                finish(); // Finalizar la actividad actual (P5) para que al regresar no se vuealva a iniciar
            }
        }, 4300);
    }

    public void onBackPressed() {
        // Al presionar el botón de retroceso, eliminar todos los callbacks y mensajes pendientes
        H1.removeCallbacksAndMessages(null);
        finish(); // Finalizar la actividad actual
    }

}