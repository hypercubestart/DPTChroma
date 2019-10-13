package com.andrewliu.gpuvideo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.ImageReader;
import android.opengl.GLSurfaceView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.daasuu.gpuv.camerarecorder.GPUCameraRecorder;
import com.daasuu.gpuv.camerarecorder.GPUCameraRecorderBuilder;
import com.daasuu.gpuv.camerarecorder.LensFacing;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.egl.filter.GlInvertFilter;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity implements RecyclerViewListener{
    private GLSurfaceView sampleGLView;
    private GPUCameraRecorder gpuCameraRecorder;
    private SeekBar seekBar;
    private View seekBarView;
    private TextView seekBarTextView;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private View filterView;
    private ColorBlindTypes currentType;
    private float intensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        seekBarView = findViewById(R.id.seekbar_view);
        seekBarTextView = findViewById(R.id.seekBarTextView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        filterView = findViewById(R.id.filter_view);

        seekBarTextView.setText("0.0");

        currentType = ColorBlindTypes.Deuteranope;
        intensity = 0;

        // set a LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        final RecyclerViewAdapter customAdapter = new RecyclerViewAdapter(MainActivity.this, ColorBlind.getAll(), this);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAdapter.toggleVisiblity();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                intensity = progress / 33.33f;
                seekBarTextView.setText(String.valueOf(Math.round(intensity * 100.0) / 100.0));
                setFilter();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    34);
        }
    }

    @Override
    protected void onResume() {
        sampleGLView = new GLSurfaceView(getApplicationContext());
        FrameLayout frameLayout = findViewById(R.id.wrap_view);
        frameLayout.addView(sampleGLView);

        gpuCameraRecorder = new GPUCameraRecorderBuilder(MainActivity.this, sampleGLView)
                .lensFacing(LensFacing.BACK)
                .build();


        // record start.
        gpuCameraRecorder.start(this.getFilesDir().getAbsolutePath());
        // record stop.
        gpuCameraRecorder.stop();

        setFilter();

        seekBarView.bringToFront();
        filterView.bringToFront();

        super.onResume();
    }

    @Override
    protected void onPause() {
        sampleGLView.onPause();

        gpuCameraRecorder.stop();
        gpuCameraRecorder.release();
        gpuCameraRecorder = null;

        ((FrameLayout) findViewById(R.id.wrap_view)).removeView(sampleGLView);
        sampleGLView = null;

        super.onPause();
    }

    @Override
    public void onItemClick(ColorBlindTypes type) {
        if (type == ColorBlindTypes.Azure) {
            Intent i = new Intent(getApplicationContext(), ClassifyColorActivity.class);
            startActivity(i);
        } else {
            currentType = type;
            setFilter();
        }
    }

    public void setFilter() {
        gpuCameraRecorder.setFilter(ColorBlind.getFilter(currentType, intensity));
    }

}
