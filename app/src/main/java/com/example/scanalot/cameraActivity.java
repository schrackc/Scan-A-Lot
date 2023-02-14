package com.example.scanalot;

import android.os.Bundle;
import android.util.Size;
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


/**
 * This class is responsible for initializing a provider, binding to that provider, and analyzing images with MLKit in the future.
 * Should users want to rotate the camera horizontally to take scans, that function is intended to be supported here as well.
 * Using the CameraX library and Google's MlKit - for OCR - in the future we will analyze images and extract UTF-8 data from them.
 * CameraX mainly is used to interact with the camera's functions. It is currently set to build 1.2.1, the latest stable release.
 *
 * @author Nick Downey
 * @Created 1/30/23
 * @Contributors Nick Downey - 1/30/23 - Created the initial camera Activity which opens the camera
 */

public class cameraActivity extends AppCompatActivity {

    // Defining instance variables.
    private PreviewView m_previewView;
    private ListenableFuture<ProcessCameraProvider> m_cameraProviderFuture;

    /**
     * onCreate() method to initialize above variables and bind image analysis case to the camera provider.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        m_previewView = findViewById(R.id.previewView);
        m_cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        m_cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = m_cameraProviderFuture.get();
                    bindImageAnalysis(cameraProvider);
                } // try
                catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }// catch
            }// run()
        }, ContextCompat.getMainExecutor(this));//listener
    }// end onCreate()


    /**
     * method used above. Listens for changes in camera rotation.
     */
    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider) {
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720)).setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                image.close();
            }
        });
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(m_previewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
    }

} // end of cameraActivity class
