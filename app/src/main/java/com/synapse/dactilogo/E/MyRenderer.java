package com.synapse.dactilogo.E;

import android.content.Context;
import android.view.MotionEvent;

import com.synapse.dactilogo.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;

public class MyRenderer extends Renderer {

    private Object3D humano;

    public MyRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        // Configurar el fondo blanco
        int fondoColor = mContext.getResources().getColor(R.color.Fondo3); // Obtener el color desde los recursos
        getCurrentScene().setBackgroundColor(fondoColor); // Establecer el color de fondo

        // Cargar el modelo humano.obj
        LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.humano);
        try {
            objParser.parse();
            humano = objParser.getParsedObject();

            // Crear un material para la textura
            Material material = new Material();
            // Cargar la textura piel.png desde res/raw
            Texture texture = new Texture("pielTexture", R.raw.piel);
            material.addTexture(texture);
            material.setColorInfluence(0);  // Asegurarse de que solo se use la textura

            // Asignar el material al modelo
            humano.setMaterial(material);
            getCurrentScene().addChild(humano);

        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();  // Manejar cualquier otra excepción
        }

        // Configurar la cámara
        getCurrentCamera().setPosition(0, 1.5, 2); // Ajusta la posición vertical y distancia de la cámara
        getCurrentCamera().setLookAt(0, 1.5, 0); // Apunta hacia el centro del modelo
    }

    @Override
    public void onRender(long elapsedTime, double deltaTime) {
        super.onRender(elapsedTime, deltaTime);

    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}