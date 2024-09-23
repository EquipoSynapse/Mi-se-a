package com.synapse.dactilogo.M;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.dactilogo.P.P1;
import com.synapse.dactilogo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

public class P4 extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "DimensionesPrefs";
    private static final String MODE_KEY = "modo_dimensiones";

    ImageView cod1, cod2, cod3, cod4; //Boton atrás
    ImageView cod5, cod6; //Visor de señas - grabar o reproducir
    TextView cod7; //Subtitulos
    ImageView cod8, cod9, cod10; //Visor de señas - grabar o reproducir
    ImageView cod11, cod12, cod13; //Boton teclado dactilo - Boton convertir a voz - Boton teclado estandar
    private Switch cod14;
    String s1 = " "; //Palabras analizadas ya sean escritas o habladas
    String s2 = "2D"; // Modo inicial
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p4);

        //Iniciamos el paquete de señas inicial
        PaquetePorDefecto();
        //Iniciamos la voz digital
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(new Locale("es", "ES"));
            }
        });

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Recuperar el estado guardado del Switch
        boolean is3D = sharedPreferences.getBoolean(MODE_KEY, false); // Default a "2D"
        s2 = is3D ? "3D" : "2D";


        //Verica los permisos de audio
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            initializeSpeechRecognizer();
        }


        //    --Vinculación de las ID del layout--
        cod1 = findViewById(R.id.ID1); //Botón atrás
        cod2 = findViewById(R.id.ID2); //Botón notificaciones
        cod3 = findViewById(R.id.ID3); //Botón tienda
        cod4 = findViewById(R.id.ID4); //Botón menu
        cod5 = findViewById(R.id.ID5); //Visor
        cod6 = findViewById(R.id.ID6); //grabar o reproducir
        cod7 = findViewById(R.id.ID7); //Subtitulos
        cod8 = findViewById(R.id.ID8); //Botón velocidad
        cod9 = findViewById(R.id.ID9); //Boton grabar Persona
        cod10 = findViewById(R.id.ID10); //Boton grabar datos
        cod11 = findViewById(R.id.ID11); //Boton teclado dactilo
        cod12 = findViewById(R.id.ID12); //Boton convertir a voz
        cod13 = findViewById(R.id.ID13); //Boton teclado estandar
        cod14 = findViewById(R.id.ID14); //Definir modo 2D o 3D para la animación de señas
        //    --Vinculación de las ID del layout--


        // Recuperar el estado guardado del Switch
        cod14.setChecked(is3D); // Ajustar el estado del Switch según lo guardado

        //    --Botones--


        //Activar función para regresar a la pantala anterior (Eleccion de modos)
        cod1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P4.this, P1.class);
                startActivity(intent); // Lanzar la nueva actividad
                finish();
            }
        });

        //Activar función para grabar voz
        cod6.setOnClickListener(view -> startSpeechRecognition());

        //Activar función para teclado dactilogo
        cod11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TecladoDactilogo();
            }
        });

        //Activar función para convertir a voz
        cod12.setOnClickListener(v -> textToSpeech.speak(s1, TextToSpeech.QUEUE_FLUSH, null, null));

        //Activar función para teclado estandar
        cod13.setOnClickListener(v -> {
            TecladoEstandar();
        });

        //Activar función para cambiar de modo 2D/3D
        cod14.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                s2 = "3D"; // Cambiar a 3D si está activado
            } else {
                s2 = "2D"; // Cambiar a 2D si está desactivado
            }

            // Guardar el nuevo estado del Switch en SharedPreferences
            guardarModo(isChecked);

            // Mostrar un mensaje con el modo actual
            Toast.makeText(P4.this, "Modo cambiado a " + s2, Toast.LENGTH_SHORT).show();
        });
        //    --Botones--
    }

    //Funcion para extraer el primer paquete de señas
    private void PaquetePorDefecto() {
        try {
            // Accedemos al archivo en res/raw con su ID
            InputStream inputStream = getResources().openRawResource(R.raw.paq1);

            // Definimos la ruta base del almacenamiento externo
            File externalDir = getExternalFilesDir(null);

            // Creamos las carpetas Paquetes, 2D y Activados si no existen
            File carpetaPaquetes = new File(externalDir, "Paquetes");
            if (!carpetaPaquetes.exists()) carpetaPaquetes.mkdir();

            File carpeta2D = new File(carpetaPaquetes, "2D");
            if (!carpeta2D.exists()) carpeta2D.mkdir();

            File carpetaActivados = new File(carpeta2D, "Activados");
            if (!carpetaActivados.exists()) carpetaActivados.mkdir();

            // Definimos el archivo de destino como paq1.txt
            File archivoDestino = new File(carpetaActivados, "paq1.txt");

            // Preparamos el FileOutputStream para escribir en el archivo de destino
            FileOutputStream fos = new FileOutputStream(archivoDestino);

            // Buffer para copiar el archivo
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            // Cerramos los streams
            inputStream.close();
            fos.close();

        } catch (IOException e) {
            // Mostramos el error en consola si ocurre algún problema
            e.printStackTrace();
        }
    }

    // Función para guardar el modo en SharedPreferences
    private void guardarModo(boolean is3D) {
        // Guardar en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MODE_KEY, is3D); // Guardar si está en 3D (true) o 2D (false)
        editor.apply(); // Aplicar los cambios
    }

    //  --Teclados--
    //Funcion para teclado dactilogo
    private void TecladoDactilogo() {
        Dialog dialog = new Dialog(this); //Inicamos un dialogo para poder manehjar mejor el codigo y reutilizar el layout en otras clases java
        dialog.setContentView(R.layout.f1); //Vinculamos el dialogo con el layout

        //Iniciamos las casillas
        TextView display = dialog.findViewById(R.id.ID39);
        TextView Enviar = dialog.findViewById(R.id.ID40);

        //Boton para enviar a analizar el texto escrito
        Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1 = display.getText().toString();
                IniciarAnimación(); //Inicamos la animación que lee el texto y lo convierte en señas
                dialog.dismiss(); //Cerramos el dialogo
            }
        });

        // Insertamos la lista de numeros en el teclado
        setNumberButtonListener(dialog, R.id.ID1, display, "1");
        setNumberButtonListener(dialog, R.id.ID2, display, "2");
        setNumberButtonListener(dialog, R.id.ID3, display, "3");
        setNumberButtonListener(dialog, R.id.ID4, display, "4");
        setNumberButtonListener(dialog, R.id.ID5, display, "5");
        setNumberButtonListener(dialog, R.id.ID6, display, "6");
        setNumberButtonListener(dialog, R.id.ID7, display, "7");
        setNumberButtonListener(dialog, R.id.ID8, display, "8");
        setNumberButtonListener(dialog, R.id.ID9, display, "9");
        setNumberButtonListener(dialog, R.id.ID10, display, "0");

        // Insertamos la lista de letras en el teclado según el modelo Qwerty
        setLetterButtonListener(dialog, R.id.ID11, display, "q");
        setLetterButtonListener(dialog, R.id.ID12, display, "w");
        setLetterButtonListener(dialog, R.id.ID13, display, "e");
        setLetterButtonListener(dialog, R.id.ID14, display, "r");
        setLetterButtonListener(dialog, R.id.ID15, display, "t");
        setLetterButtonListener(dialog, R.id.ID16, display, "y");
        setLetterButtonListener(dialog, R.id.ID17, display, "u");
        setLetterButtonListener(dialog, R.id.ID18, display, "i");
        setLetterButtonListener(dialog, R.id.ID19, display, "o");
        setLetterButtonListener(dialog, R.id.ID20, display, "p");
        setLetterButtonListener(dialog, R.id.ID21, display, "a");
        setLetterButtonListener(dialog, R.id.ID22, display, "s");
        setLetterButtonListener(dialog, R.id.ID23, display, "d");
        setLetterButtonListener(dialog, R.id.ID24, display, "f");
        setLetterButtonListener(dialog, R.id.ID25, display, "g");
        setLetterButtonListener(dialog, R.id.ID26, display, "h");
        setLetterButtonListener(dialog, R.id.ID27, display, "j");
        setLetterButtonListener(dialog, R.id.ID28, display, "k");
        setLetterButtonListener(dialog, R.id.ID29, display, "l");
        setLetterButtonListener(dialog, R.id.ID30, display, "z");
        setLetterButtonListener(dialog, R.id.ID31, display, "x");
        setLetterButtonListener(dialog, R.id.ID32, display, "c");
        setLetterButtonListener(dialog, R.id.ID33, display, "v");
        setLetterButtonListener(dialog, R.id.ID34, display, "b");
        setLetterButtonListener(dialog, R.id.ID35, display, "n");
        setLetterButtonListener(dialog, R.id.ID36, display, "m");

        // Funcion de borrar
        dialog.findViewById(R.id.ID37).setOnClickListener(v -> {
            String text = display.getText().toString();
            if (text.length() > 0) {
                display.setText(text.substring(0, text.length() - 1));
            }
        });

        //Función para crear espacios
        dialog.findViewById(R.id.ID38).setOnClickListener(v -> {
            display.append(" ");
        });

        dialog.show(); //Al cargar todo se abre el dialogo
    }

    //Ingresamos los numeros en el teclado Dactilogo
    private void setNumberButtonListener(Dialog dialog, int buttonId, TextView display, String number) {
        dialog.findViewById(buttonId).setOnClickListener(v -> {
            display.append(number);
        });
    }

    //Ingresamos las letras en el teclado Dactilogo
    private void setLetterButtonListener(Dialog dialog, int buttonId, TextView display, String letter) {
        dialog.findViewById(buttonId).setOnClickListener(v -> {
            display.append(letter);
        });
    }

    //Funcion para teclado estandar
    private void TecladoEstandar() {
        // Creamos el cuadro de diálogo con un campo de texto
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escribe algo");

        // Usamos un layout para el cuadro de diálogo con un EditText
        final EditText input = new EditText(this);
        builder.setView(input);

        // Configuramos el botón de "Aceptar" en el cuadro de diálogo
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            // Reemplazamos el texto de s1 con lo que el usuario escribió
            s1 = input.getText().toString();

            // Iniciamos la función de animación
            IniciarAnimación();
        });

        // Mostramos el cuadro de diálogo
        builder.show();
    }
    //  --Teclados--



    //Procesamiento de la voz
    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    // El reconocimiento está listo para comenzar
                }

                @Override
                public void onBeginningOfSpeech() {
                    // Se ha comenzado a hablar
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                    // Cambio en el nivel de entrada de voz
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                    // Se ha recibido un buffer de audio
                }

                @Override
                public void onEndOfSpeech() {
                    // Fin de la entrada de voz
                }

                @Override
                public void onError(int error) {
                    // Manejar errores aquí
                    s1 = " ";
                    cod5.setImageResource(R.drawable.vacio);
                    cod6.setImageResource(R.drawable.r10);
                    Toast.makeText(P4.this, "Error en el reconocimiento de voz", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResults(Bundle results) {
                    // Se han recibido resultados
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        s1 = matches.get(0);
                        cod7.setText(s1);
                        IniciarAnimación();
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    // Resultados parciales (si los necesitas)
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                    // Eventos adicionales (si es necesario)
                }
            });
        }
    }

    //Funcion para empezar a escuchar la voz humana y convertirla a texto
    private void startSpeechRecognition() {
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-MX"); // Cambia a tu idioma preferido
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora");

        cod6.setImageResource(R.drawable.r7); //Creamos un cambio en el boton para que el usuario reconozca que se esta grabando
        speechRecognizer.startListening(recognizerIntent);
    }



    //Animación que convierte todo a lenguaje de señas y lo muestra en el visor
    private void IniciarAnimación() {

        try {
            // Obtener las rutas de los archivos
            File archivoSeñas = new File(this.getExternalFilesDir(null), "Paquetes/2D/Activados/paq1.txt");
            File archivoSeñaPalabras = new File(this.getExternalFilesDir(null), "señapalabras.txt");

            // Crear un handler para manejar la actualización de la UI
            Handler handler = new Handler();

            // Guardar el texto original para el subtítulo sin normalizar
            final String subtituloText = s1;

            // Normalizar el texto hablado para comparaciones (solo se aplica a las letras)
            String spokenTextNormalizado = Normalizer.normalize(s1.toLowerCase(), Normalizer.Form.NFD)
                    .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

            // Dividir el texto hablado en palabras
            String[] palabras = spokenTextNormalizado.split(" ");  // Dividir por espacios
            String[] palabrasOriginales = subtituloText.split(" ");  // Dividir el texto original para subtítulos

            // Variable para llevar el tiempo de retraso acumulado
            int delay = 0;

            // Recorrer cada palabra del texto hablado
            for (int i = 0; i < palabras.length; i++) {
                final String palabraHabladaNormalizada = palabras[i];
                final String palabraHabladaOriginal = palabrasOriginales[i];  // Para mostrar en subtítulos
                boolean palabraEncontrada = false;

                // Mostrar la palabra en el TextView "Subtitulos"
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cod7.setText(palabraHabladaOriginal);  // Mostrar la palabra completa en el TextView
                    }
                }, delay);


                /*
                // Leer el archivo "señapalabras" para verificar si la palabra está en el archivo
                BufferedReader brPalabras = new BufferedReader(new InputStreamReader(new FileInputStream(archivoSeñaPalabras)));
                String lineaPalabras;

                // Buscar la palabra en el archivo de señapalabras
                while ((lineaPalabras = brPalabras.readLine()) != null) {
                    String[] partesPalabras = lineaPalabras.split(";");

                    if (partesPalabras.length == 2) {
                        String palabraArchivo = partesPalabras[0];

                        // Comparar la palabra del archivo con la palabra normalizada hablada
                        if (palabraArchivo.equals(palabraHabladaNormalizada)) {
                            final String base64Image = partesPalabras[1];

                            // Establecer la imagen de la palabra completa con un retraso
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                    cod1.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                                }
                            }, delay);

                            delay += 500;  // Aumentar el retraso por palabra encontrada
                            palabraEncontrada = true;
                            break;  // Salir del ciclo si se encuentra la palabra completa
                        }
                    }
                }
                brPalabras.close();

                Toast.makeText(p2.this, "5", Toast.LENGTH_SHORT).show();
                // Si la palabra no fue encontrada en el archivo "señapalabras", procesarla letra por letra
                if (!palabraEncontrada) {*/


                BufferedReader brSeñas = new BufferedReader(new InputStreamReader(new FileInputStream(archivoSeñas)));

                // Recorrer cada letra de la palabra
                for (int j = 0; j < palabraHabladaNormalizada.length(); j++) {
                    final char letraHablada = palabraHabladaNormalizada.charAt(j);
                    String lineaSeñas;
                    boolean letraEncontrada = false;

                    // Buscar cada letra en el archivo "señas"
                    while ((lineaSeñas = brSeñas.readLine()) != null) {
                        String[] partesSeñas = lineaSeñas.split(";");

                        if (partesSeñas.length == 2) {
                            char letraArchivo = partesSeñas[0].charAt(0);

                            // Comparar la letra del archivo con la letra hablada
                            if (letraArchivo == letraHablada) {

                                final String base64Image = partesSeñas[1];

                                // Mostrar la imagen de la letra con el retraso
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                        cod5.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                                    }
                                }, delay);

                                letraEncontrada = true;
                                delay += 500;  // Aumentar el retraso por cada letra encontrada
                                break;  // Salir del ciclo si se encuentra la letra
                            }
                        }
                    }

                    // Resetear el buffer para la próxima letra
                    brSeñas = new BufferedReader(new InputStreamReader(new FileInputStream(archivoSeñas)));

                    // Si la letra no fue encontrada, mostrar una imagen predeterminada con retraso
                    if (!letraEncontrada) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cod5.setImageResource(R.drawable.vacio);
                            }
                        }, delay);
                        delay += 500;  // Aumentar el retraso por la letra no encontrada
                    }
                }

                brSeñas.close();
                //}

                // Eliminar la palabra del TextView "Subtitulos" después de que se ha procesado
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cod7.setText("");  // Limpiar el subtítulo cuando se termina de procesar la palabra
                        cod5.setImageResource(R.drawable.vacio);  // Limpiar el visor de señas
                    }
                }, delay);
            }

            cod6.setImageResource(R.drawable.r10);  // Establecer la imagen final

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Se procesan los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeSpeechRecognizer();
            } else {
                Toast.makeText(this, "Permiso de grabación de audio denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}