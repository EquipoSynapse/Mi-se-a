package com.synapse.dactilogo;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    private MySurfaceView mySurfaceView;
    private MyRenderer myRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySurfaceView = findViewById(R.id.surface_view);
        myRenderer = new MyRenderer(this);
        mySurfaceView.setSurfaceRenderer(myRenderer);
    }
}