package com.example.scanalot;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.FragmentScanBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

/**
 * This class is used for the ScanFragment. It creates the fragment and uses the fragment_scan layout. This is used as the main page when the user
 * logs into the app. This fragment will be responsible for hosting the camera and getting the scan from the license plate.
 *
 * @author Andrew Hoffer
 * @Created 1/21/23
 * @Contributors Andrew Hoffer - 1/21/23 - Created the fragment
 */

//// ------------------------------------------------------------------------------------------------------------------------------//
//// This fragment is responsible for initializing a provider. binding to that provider, and analyzing images with MLKit in the future.
//// Should users want to rotate the camera horizontally to take scans, that function is intended to be supported here as well.
//// Using the CameraX library and Google's MlKit - for OCR - in the future we will analyze images and extract UTF-8 data from them.
//// CameraX mainly is used to interact with the camera's functions. It is currently set to build 1.2.1, the latest stable release.
//// ------------------------------------------------------------------------------------------------------------------------------//
public class ScanFragment extends Fragment {
    // Defining instance variables.
    private TextView overlayText;
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    FragmentScanBinding binding;
    NavDirections navAction;
    Button btnManualEntry;
    Button btnResultScan;


    TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

    ProcessCameraProvider cameraProvider;
    CameraSelector cameraSelector;


    /**
     * Method in which executes during the creation of the view. It is creating an instance of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // super.onViewCreated(view, savedInstanceState);
        binding = FragmentScanBinding.inflate(inflater, container, false);
        previewView = binding.previewView;
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        overlayText = binding.overlayTextView;
        try {
            cameraProvider = cameraProviderFuture.get();
        }catch(Exception ex)
        {
            Log.i("ERROR", ex.toString());
        }
        //set the camera view which is the front camera
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        //use image capture use case since we want an image


        //create an image analysis instance which is responsible for capturing images
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        // enable the following line if RGBA output is needed.
                        //  .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        //set the display of camera view using Preview
        Preview preview = new Preview.Builder().build();

        //set the surface for the camera through the Preview Layout
        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());


        //bind all of these together on the lifecycle
        // cameraProvider.bindToLifecycle(getViewLifecycleOwner(),cameraSelector,imageCapture,preview);
        cameraProvider.bindToLifecycle(getViewLifecycleOwner(),cameraSelector,imageAnalysis,preview);

        //process the images coming in and get text using TextRecognition Object
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            imageAnalysis.setAnalyzer(getContext().getMainExecutor(), new ImageAnalysis.Analyzer() {
                @SuppressLint("UnsafeOptInUsageError")
                @Override
                public void analyze(@NonNull ImageProxy imageProxy) {
                    int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();

                    Image cameraImage = null;

                    // attempt to get the image
                    try {
                        cameraImage =  imageProxy.getImage();
                    } catch(Exception ex)
                    {
                        Log.e("IMAGE ANALYSIS","FAILED TO GET THE IMAGE: " +  ex.getMessage().toString());
                    }

                    //check if we got the image
                    if(cameraImage !=null)
                    {
                        //create an image of type InputImage to pass into a vision api
                        InputImage image = InputImage.fromMediaImage(cameraImage, imageProxy.getImageInfo().getRotationDegrees());
                        //pass into a vision api such as tesseract
                        //  Log.i("VISION API","PASSING IMAGE INTO THE VISION API");

                        InputImage img =  InputImage.fromMediaImage(cameraImage, rotationDegrees);

                        Task<Text> result = textRecognizer.process(img).addOnCompleteListener(new OnCompleteListener<Text>() {
                            @Override
                            public void onComplete(@NonNull Task<Text> task) {
                                String resultText = task.getResult().getText();
                                // after done, release the ImageProxy object
                                imageProxy.close();
                                Log.i("RESULT TEXT", resultText);
                                overlayText.setText(resultText);
                                overlayText.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                }
            });
        }

        return binding.getRoot();
    }


    /**
     * Method used when the view is created. This is the destination fragment when the user logs in. There are two buttons which are given click
     * event listeners. The manual button takes you to the manual entry fragment to manually enter a license plate to see if it belong in the
     * right lot. The Results scan button is a temporary button until the camera scan is scanning, but this will take you to the result of your
     * scan.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnManualEntry = binding.outlinedButton;
        btnResultScan = binding.ResultsScanButton;
        Log.i("onCreate", "scan fragment created");

        //event listener on the manual entry button. Navigate to manual entry fragment
        btnManualEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Button Click", "manual button clicked !!!!");
                navAction = ScanFragmentDirections.actionScanFragmentToManualEntryFragment();
                Navigation.findNavController(view).navigate(navAction);
            }
        });
        //event listener on the result scan button. Navigate to result fragment.
        btnResultScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navAction = ScanFragmentDirections.actionScanFragmentToResultsFragment();
                Navigation.findNavController(view).navigate(navAction);
            }
        });

    }



    /**
     * Cleans up resources when view is destroyed
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}