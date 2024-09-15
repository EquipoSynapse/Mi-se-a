package com.synapse.dactilogo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // Duración de la pantalla de carga (3 segundos)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Referencia al logo
        ImageView logoImageView = findViewById(R.id.logoImageView);

        // Cargar la animación de fade-in
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImageView.startAnimation(fadeInAnimation); // Aplicar animación al logo

        // Handler para retrasar la transición a la pantalla de bienvenida
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Iniciar la pantalla de bienvenida
                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish(); // Cerrar la actividad de splash para que no vuelva al retroceder
            }
        }, SPLASH_TIME_OUT);
    }
}
