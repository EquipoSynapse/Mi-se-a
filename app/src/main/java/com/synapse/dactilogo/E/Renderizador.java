package com.synapse.dactilogo.E;

import android.content.Context;
import android.view.MotionEvent;

import com.synapse.dactilogo.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.renderer.Renderer;

public class Renderizador extends Renderer {

    private DirectionalLight directionalLight;
    private Object3D object3D;

    public Renderizador(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        getCurrentScene().setBackgroundColor(getContext().getResources().getColor(R.color.white));

        getCurrentCamera().setPosition(0, 1.5, 1.2);
        getCurrentCamera().setLookAt(0, 1.5, 0);

        try {
            LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.humano);
            objParser.parse();
            object3D = objParser.getParsedObject();
            object3D.setPosition(0, 0, 0);
            /*object3D.rotate(Vector3.Axis.X, 90.0);
            object3D.rotate(Vector3.Axis.Z, 180.0);*/
            //object3D.setRotY(90);

            Material material = new Material();
            material.enableLighting(true);
            material.setDiffuseMethod(new DiffuseMethod.Lambert());

            try {
                // Cargar la textura desde los recursos
                Texture texture = new Texture("skinTexture", R.raw.piel);
                material.addTexture(texture);
            } catch (Texture.TextureException e) {
                e.printStackTrace();
            }

            object3D.setMaterial(material);

            getCurrentScene().addChild(object3D);
        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRender(long elapsedRealtime, double deltaTime) {
        super.onRender(elapsedRealtime, deltaTime);
        //object3D.rotate(Vector3.Axis.Y, 1.0);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        // Implementa la lógica de desplazamiento si es necesario
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        // Implementa la lógica de eventos táctiles si es necesario
    }
}
