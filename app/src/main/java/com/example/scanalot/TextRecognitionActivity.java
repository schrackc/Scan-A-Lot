package com.example.scanalot;

import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;


/**
 * This class is used for text recognition with Google's MLKit.
 *
 * @author Nickolas Downey
 * @Created 2/18/23
 * @Contributors
 */
public class TextRecognitionActivity implements ImageAnalysis.Analyzer{
    // Create instance of TextRecognizer
    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    // Pass a media.Image to as the InputImage object because the app takes pictures.
    // We would use a file URI if we wanted to select images from a photo gallery.
    // Analyze takes in an image and gets information from it.
    @Override
    @ExperimentalGetImage
    public void analyze(ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            imageProxy.close();
            // Pass image to an ML Kit Vision API
        }
    }


    // Takes in an image and ...
//.addOnCompleteListener(results -> image.close()); use for closing CameraX listener or it will stall

    public void process(InputImage image){
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                // Task completed successfully



                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

    }





}


