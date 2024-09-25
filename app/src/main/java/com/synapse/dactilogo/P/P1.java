package com.synapse.dactilogo.P;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.synapse.dactilogo.M.P2;
import com.synapse.dactilogo.M.P3;
import com.synapse.dactilogo.M.P4;
import com.synapse.dactilogo.R;

public class P1 extends AppCompatActivity {
    ConstraintLayout cod1, cod2, cod3; //Los contenedores que funcionará como boton para cada modo
    ImageView cod4, cod5, cod6, cod10, cod11, cod12, cod13; // Las imagenes que representan cada modo - Boton Recordar Modo - Botones de información de cada modo
    LinearLayout cod7, cod8, cod9; // Los contenedores que organizan el nombre del modo y el boton de ayuda
    int I1 = 0;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1);

        /*

        Se presentan los 3 modos

        Básico: Opciones simples sin tantos botones
        Perfil: opciones más avanzadas, con acceso a tienda y personalización
        Premium: Acceso a modelado 3D y modelos unicos

         */

        cod1 = findViewById(R.id.ID1); //ConstraintLayout 1 o modo Básico
        cod2 = findViewById(R.id.ID4); //ConstraintLayout 2 o modo Perfil
        cod3 = findViewById(R.id.ID7); //ConstraintLayout 3 o modo Premium
        cod4 = findViewById(R.id.ID2); //Imagen que representa el modo Básico
        cod5 = findViewById(R.id.ID5); //Imagen que representa el modo Perfil
        cod6 = findViewById(R.id.ID8); //Imagen que representa el modo Premium
        cod7 = findViewById(R.id.ID3); //Linear layout del modo Básico
        cod8 = findViewById(R.id.ID6); //Linear layout del modo Perfil
        cod9 = findViewById(R.id.ID9); //Linear layout del modo Premium
        cod10 = findViewById(R.id.ID10); //Opción recordar modo
        cod11 = findViewById(R.id.ID11); //Opción Información modo Básico
        cod12 = findViewById(R.id.ID12); //Opción Información modo Perfil
        cod13 = findViewById(R.id.ID13); //Opción Información modo Premium

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // Recuperar el valor guardado
        I1 = sharedPreferences.getInt("mi_int_clave", 0);  // Por defecto 0 si no se ha guardado

        if (I1 == 1){
            Intent intent = new Intent(P1.this, P2.class);
            startActivity(intent); // Lanzar al modo básico si el usuario activo la opción para recordar por el sistema
            finish();
        } else if (I1 == 2){
            Intent intent = new Intent(P1.this, P3.class);
            startActivity(intent); // Lanzar al modo perfil si el usuario activo la opción para recordar por el sistema
            finish();
        } else if (I1 == 3){
            Intent intent = new Intent(P1.this, P4.class);
            startActivity(intent); // Lanzar al modo premium si el usuario activo la opción para recordar por el sistema
            finish();
        }

        //Al clickear un modo el constraintlatout se tornara de color negativo y la imagen se deslizará a la derecha, el LineartLayout se esconderá y se vanzará a la siguiente Activity

        cod1.setOnClickListener(v -> Animación(cod1, cod4, cod7, P2.class, 1)); //Entrando al modo Básico
        cod2.setOnClickListener(v -> Animación(cod2, cod5, cod8, P3.class, 2)); //Entrando al modo Perfil
        cod3.setOnClickListener(v -> Animación(cod3, cod6, cod9, P4.class, 3)); //Entrando al modo Premium

        //Recordamos que modo eligió el usuario
        cod10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (I1 == 0){
                    I1 = 1; //0 significa apagado y 1 significa encendido
                    cod10.setImageResource(R.color.Color1);
                }else{
                    I1 = 0; //0 significa apagado y 1 significa encendido
                    cod10.setImageResource(R.color.Color0);
                }
            }
        });

        // Acción para el botón "Modo Básico"
        cod11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogo("Modo Básico", getString(R.string.t12));
            }
        });

        // Acción para el botón "Modo Perfil"
        cod12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogo("Modo Perfil", getString(R.string.t13));
            }
        });

        // Acción para el botón "Modo Premium"
        cod13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogo("Modo Premium", getString(R.string.t14));
            }
        });
    }

    private void Animación(ConstraintLayout layout, ImageView image, LinearLayout linearLayout, Class<?> nextActivity, int I) {
        // Cambiar el fondo del ConstraintLayout a blanco
        layout.setBackgroundColor(getResources().getColor(android.R.color.white));

        // Crear una nueva ConstraintSet para cambiar las restricciones
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout); // Clonamos las restricciones actuales del ConstraintLayout correspondiente

        // Modificar la propiedad layout_constraintHorizontal_bias de la imagen
        constraintSet.setHorizontalBias(image.getId(), 0.99f); // Mover a la derecha

        // Aplicar la animación de transición
        TransitionManager.beginDelayedTransition(layout);
        constraintSet.applyTo(layout); // Aplicar las nuevas restricciones al ConstraintLayout correspondiente

        // Ocultar el LinearLayout
        linearLayout.setVisibility(View.GONE);

        if(I1 == 1 | I1 == 2 | I1 == 3 ){
            // Guardar el nuevo valor
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("mi_int_clave", I);
            editor.apply();  // Guardar de forma asincrónica
        }




        // Usar un Handler para retrasar el lanzamiento de la nueva actividad
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(P1.this, nextActivity);
            intent.putExtra("modo", String.valueOf(I));
            startActivity(intent); // Lanzar la nueva actividad después de la animación
            finish(); // Finalizar la actividad actual para no volver atrás
        }, 800); // El retraso es de 1 segundo (1000 milisegundos) para que la animación se aprecie
    }

    // Método para mostrar un cuadro de diálogo con información
    private void mostrarDialogo(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(P1.this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();  // Cierra el diálogo al presionar OK
            }
        });
        builder.show();
    }
}