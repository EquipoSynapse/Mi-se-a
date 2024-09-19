package com.synapse.dactilogo.P;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.synapse.dactilogo.M.P2;
import com.synapse.dactilogo.M.P3;
import com.synapse.dactilogo.M.P4;
import com.synapse.dactilogo.MainActivity;
import com.synapse.dactilogo.R;

public class P1 extends AppCompatActivity {
    ConstraintLayout cod1, cod2, cod3; //Los contenedores que funcionará como boton para cada modo
    ImageView cod4, cod5, cod6; // Las imagenes que representan cada modo
    LinearLayout cod7, cod8, cod9; // Los contenedores que organizan el nombre del modo y el boton de ayuda

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


        //Al clickear un modo el constraintlatout se tornara de color negativo y la imagen se deslizará a la derecha, el LineartLayout se esconderá y se vanzará a la siguiente Activity

        cod1.setOnClickListener(v -> Animación(cod1, cod4, cod7, P2.class)); //Entrando al modo Básico
        cod2.setOnClickListener(v -> Animación(cod2, cod5, cod8, P3.class)); //Entrando al modo Perfil
        cod3.setOnClickListener(v -> Animación(cod3, cod6, cod9, P4.class)); //Entrando al modo Premium
    }

    private void Animación(ConstraintLayout layout, ImageView image, LinearLayout linearLayout, Class<?> nextActivity) {
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


        Intent intent = new Intent(P1.this, nextActivity);
        startActivity(intent); // Lanzar la nueva actividad
    }
}