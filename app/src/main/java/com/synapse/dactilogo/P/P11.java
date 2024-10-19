package com.synapse.dactilogo.P;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.os.Handler;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synapse.dactilogo.R;

import java.util.Arrays;

public class P11 extends AppCompatActivity { // Declara la clase P11 que extiende de AppCompatActivity, lo que permite crear una actividad.

    private SurfaceView surfaceView; // Declara una variable para el visor de la cámara.
    private SurfaceHolder surfaceHolder; // Declara un objeto para manejar el holder de la SurfaceView.
    private CameraDevice cameraDevice; // Declara un objeto para la cámara.
    private CaptureRequest.Builder captureRequestBuilder; // Declara un constructor de solicitudes de captura.
    private CameraCaptureSession cameraCaptureSession; // Declara una sesión de captura de la cámara.


    private TextView textView;
    private DatabaseReference databaseReference;
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) { // Método llamado al crear la actividad.
        super.onCreate(savedInstanceState); // Llama al método onCreate de la clase padre.
        setContentView(R.layout.p11); // Establece el layout de la actividad a 'p11'.

        surfaceView = findViewById(R.id.ID1); // Inicializa surfaceView con la vista correspondiente del layout.


        // Verificar permisos de cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) // Comprueba si se tienen los permisos de cámara.
                != PackageManager.PERMISSION_GRANTED) { // Si no se tienen los permisos...
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100); // Solicita permisos de cámara.
        } else {
            startCamera(); // Si ya se tienen permisos, inicia la cámara.
        }



        textView = findViewById(R.id.ID2); // Asegúrate de tener un TextView con este ID en tu layout
        databaseReference = FirebaseDatabase.getInstance().getReference("cache").child("x"); // Usando .child() para acceder a "x"

        // Inicializa el Handler
        handler = new Handler();

        // Crea el Runnable para obtener datos cada 1000 ms
        runnable = new Runnable() {
            @Override
            public void run() {
                fetchData();
                handler.postDelayed(this, 1000); // Repite cada 1000 ms
            }
        };

        // Comienza a ejecutar el Runnable
        handler.post(runnable);
    }

    private void fetchData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class); // Obtener el valor de "x"
                if (value != null) {
                    textView.setText(value); // Actualiza el TextView
                } else {
                    textView.setText("..."); // Mensaje si no se encuentra
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja cualquier error al acceder a la base de datos
                textView.setText("Error: " + databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detiene el handler al destruir la actividad
        handler.removeCallbacks(runnable);
    }

    private void startCamera() { // Método para iniciar la cámara.
        surfaceHolder = surfaceView.getHolder(); // Obtiene el holder de la SurfaceView.
        surfaceHolder.addCallback(new SurfaceHolder.Callback() { // Añade un callback para el holder de la SurfaceView.
            @Override
            public void surfaceCreated(SurfaceHolder holder) { // Método llamado cuando la Surface es creada.
                openCamera(); // Abre la cámara.
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { // Método llamado cuando la Surface cambia.
                // No hace nada en este caso.
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { // Método llamado cuando la Surface es destruida.
                closeCamera(); // Cierra la cámara.
            }
        });
    }

    private void openCamera() { // Método para abrir la cámara.
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE); // Obtiene el servicio de cámara.
        try {
            String cameraId = cameraManager.getCameraIdList()[0]; // Selecciona la primera cámara disponible.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) { // Verifica si se tienen permisos.
                cameraManager.openCamera(cameraId, cameraStateCallback, null); // Abre la cámara utilizando el ID.
            }
        } catch (CameraAccessException e) { // Maneja excepciones relacionadas con el acceso a la cámara.
            e.printStackTrace(); // Imprime la traza de la excepción en caso de error.
        }
    }

    private CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() { // Callback para el estado de la cámara.
        @Override
        public void onOpened(@NonNull CameraDevice camera) { // Método llamado cuando la cámara se ha abierto.
            cameraDevice = camera; // Asigna la cámara abierta al objeto cameraDevice.
            createCameraPreviewSession(); // Crea la sesión de vista previa de la cámara.
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) { // Método llamado cuando la cámara se desconecta.
            cameraDevice.close(); // Cierra la cámara.
            cameraDevice = null; // Establece cameraDevice a null.
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) { // Método llamado en caso de error en la cámara.
            cameraDevice.close(); // Cierra la cámara.
            cameraDevice = null; // Establece cameraDevice a null.
        }
    };

    private void createCameraPreviewSession() { // Método para crear la sesión de vista previa de la cámara.
        try {
            Surface surface = surfaceHolder.getSurface(); // Obtiene la superficie de la SurfaceHolder.
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW); // Crea una solicitud de captura para la vista previa.
            captureRequestBuilder.addTarget(surface); // Añade la superficie como objetivo de la solicitud de captura.

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() { // Crea una sesión de captura.
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) { // Método llamado cuando la sesión está configurada.
                    cameraCaptureSession = session; // Asigna la sesión configurada al objeto cameraCaptureSession.
                    try {
                        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO); // Establece el modo de control a automático.
                        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null); // Inicia la solicitud de captura repetitiva.
                    } catch (CameraAccessException e) { // Maneja excepciones relacionadas con el acceso a la cámara.
                        e.printStackTrace(); // Imprime la traza de la excepción en caso de error.
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) { // Método llamado si la configuración falla.
                    Toast.makeText(P11.this, "Configuración fallida", Toast.LENGTH_SHORT).show(); // Muestra un mensaje de error al usuario.
                }
            }, null); // Proporciona un hilo en el que ejecutar la configuración (null usa el hilo predeterminado).
        } catch (CameraAccessException e) { // Maneja excepciones relacionadas con el acceso a la cámara.
            e.printStackTrace(); // Imprime la traza de la excepción en caso de error.
        }
    }

    private void closeCamera() { // Método para cerrar la cámara.
        if (cameraCaptureSession != null) { // Si la sesión de captura no es nula...
            cameraCaptureSession.close(); // Cierra la sesión de captura.
            cameraCaptureSession = null; // Establece cameraCaptureSession a null.
        }
        if (cameraDevice != null) { // Si el dispositivo de cámara no es nulo...
            cameraDevice.close(); // Cierra el dispositivo de cámara.
            cameraDevice = null; // Establece cameraDevice a null.
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { // Método llamado cuando se recibe la respuesta a la solicitud de permisos.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Llama al método onRequestPermissionsResult de la clase padre.
        if (requestCode == 100) { // Verifica si el código de solicitud es el correcto.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Si se ha concedido el permiso...
                startCamera(); // Inicia la cámara.
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show(); // Muestra un mensaje de error si se deniega el permiso.
            }
        }
    }
}
