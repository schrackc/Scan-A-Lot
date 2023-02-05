package com.example.scanalot;

// ------------------------------------------------------------------------------------------------------------------------------//
// This class is responsible for initializing a provider. binding to that provider, and analyzing images with MLKit in the future.
// Should users want to rotate the camera horizontally to take scans, that function is intended to be supported here as well.
// Using the CameraX library and Google's MlKit - for OCR - in the future we will analyze images and extract UTF-8 data from them.
// CameraX mainly is used to interact with the camera's functions. It is currently set to build 1.2.1, the latest stable release.
// ------------------------------------------------------------------------------------------------------------------------------//

import android.os.Bundle;
import android.util.Size;
import android.view.OrientationEventListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class cameraActivity extends AppCompatActivity {
// Defining instance variables.
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private TextView textView;

    //onCreate() method to initialize above variables and bind image analysis case to the camera provider.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        previewView = findViewById(R.id.previewView);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

       // The following is for handling screen orientation. We may decide to delete it.
       textView = findViewById(R.id.orientation);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                    bindImageAnalysis(cameraProvider);
                } // try
                catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }// catch
            }// run()
        }, ContextCompat.getMainExecutor(this));//listener
    }// end onCreate()


    //bindImageAnalysis() method used above. Listens for changes in camera rotation.
    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider) {
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                image.close();
            }
        });
        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                textView.setText(Integer.toString(orientation));
            }
        };
        orientationEventListener.enable();
        Preview preview = new Preview.Builder().build();
        //preview.setSurfaceProvider(previewView.getSurfaceProvider());
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,
                imageAnalysis, preview);
    }
    
    
} // end of cameraActivity class
