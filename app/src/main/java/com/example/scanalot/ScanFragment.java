package com.example.scanalot;


import android.annotation.SuppressLint;
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
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.FragmentScanBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;

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
    TicketDataViewModel viewModel;
    ArrayList<ArrayList<Object>> vehicleList;

    FirebaseFirestore db;

    CollectionReference vehiclesCollection;
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
        viewModel = new ViewModelProvider(requireActivity()).get(TicketDataViewModel.class);
        //get the list of vehicle data which was from firebase
        // vehicleList = viewModel.getLicenseVehicleList().getValue();
        db = FirebaseFirestore.getInstance();
        //get the collection
        vehiclesCollection = db.collection("Vehicles");
        //now set up a query to get the particular vehicles that belong with the lot
        Query lotQuery = vehiclesCollection.whereEqualTo("ParkingLot", viewModel.getParkingLot().getValue());
        //get the Data from firestore
        lotQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    vehicleList = new ArrayList<ArrayList<Object>>();
                    int iRowValue = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Add values to 2d Array List
                        vehicleList.add(new ArrayList<>());
                        vehicleList.get(iRowValue).add(0, document.getString("OwnerFirstName") + " " + document.getString("OwnerLastName"));
                        vehicleList.get(iRowValue).add(1, document.getString("Make"));
                        vehicleList.get(iRowValue).add(2, document.getString("Model"));
                        vehicleList.get(iRowValue).add(3, document.getString("Color"));
                        vehicleList.get(iRowValue).add(4, document.getString("LicenseNum"));
                        vehicleList.get(iRowValue).add(5, document.getString("LicenseState"));
                        vehicleList.get(iRowValue).add(6, document.get("ParkingLot"));
                        Log.d("GotDoc", document.getId() + " => " + document.getData());
                        iRowValue++;
                    }
                }else
                {
                    Log.e("FIRE STORE ERROR: ", "Could not get data");
                }
            }
        });
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
                        // was 1280 720
                        .setTargetResolution(new Size(1080, 2400))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        //set the display of camera view using Preview
        Preview preview = new Preview.Builder().build();

        //set the surface for the camera through the Preview Layout
        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        //bind all of these together on the lifecycle
        // cameraProvider.bindToLifecycle(getViewLifecycleOwner(),cameraSelector,imageCapture,preview);
        cameraProvider.bindToLifecycle(getViewLifecycleOwner(),cameraSelector,imageAnalysis,preview);

        ///////////////////// NEW MLKIT START///////////////////////////////////////////////
        //process the images coming in and get text using TextRecognition Object
        //This if statement checks if the device's API level is equal to or greater than Android P (API level 28).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //This line sets the analyzer for the ImageAnalysis object, which processes the incoming images.
            imageAnalysis.setAnalyzer(getContext().getMainExecutor(), new ImageAnalysis.Analyzer() {
                @SuppressLint("UnsafeOptInUsageError")
                @Override
                public void analyze(@NonNull ImageProxy imageProxy) {
                    //This line gets the rotation of the image in degrees.
                    int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();

                    //This block of code attempts to retrieve the image from the ImageProxy object, logs an error message if it fails, and assigns the image to the cameraImage variable.
                    Image cameraImage = null;
                    try {
                        cameraImage =  imageProxy.getImage();
                    } catch(Exception ex) {
                        Log.e("IMAGE ANALYSIS","FAILED TO GET THE IMAGE: " + ex.getMessage());
                    }

                    //This if statement checks if the cameraImage variable is not null.
                    if(cameraImage != null) {
                        //This line creates an InputImage object from the cameraImage and rotationDegrees variables.
                        InputImage image = InputImage.fromMediaImage(cameraImage, imageProxy.getImageInfo().getRotationDegrees());

                        //This block of code uses the TextRecognition object to process the InputImage and extract text.
                        // It then filters the extracted text using a regular expression pattern and sets it to the overlayText TextView.
                        // Finally, it logs the extracted text- for debug purposes - and closes the ImageProxy object.
                        textRecognizer.process(image)
                                .addOnSuccessListener(new OnSuccessListener<Text>() {
                                    @Override
                                    public void onSuccess(Text visionText) {
                                        StringBuilder sb = new StringBuilder();

                                        for (Text.TextBlock block : visionText.getTextBlocks()) {
                                            for (Text.Line line : block.getLines()) {
                                                String text = line.getText().trim();

                                                if (text.matches("^[A-Za-z]{3}[-\\s]\\d{4}$")) {
                                                    sb.append(text).append("\n");
                                                }
                                            }
                                        }

                                        // set the filtered text to the overlayText TextView
                                        overlayText.post(() -> overlayText.setText(sb.toString()));

                                      boolean isCorrectLot =  compareLicensePlates(overlayText.getText());

                                        if(isCorrectLot)
                                        {
                                            Log.i("IS CORRECT LOT","GOOD VEHICLE YAHHHH!!!!");
                                        }
                                        else
                                        {
                                            Log.i("IS CORRECT LOT","BAD VEHICLE NAHHHH !!!!");
                                        }
                                    }
                                })
                                .addOnCompleteListener(new OnCompleteListener<Text>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Text> task) {
                                        String resultText = task.getResult().getText();
                                        // after done, release the ImageProxy object
                                         imageProxy.close();
                                         // used for checking what MLKit is seeing.
                                         Log.i("RESULT TEXT", resultText);

                                    }
                                });
                    } else {
                        //This else statement closes the ImageProxy object if the cameraImage variable is null.
                        // This is a requirement for CameraX implementations of MLKit in particular.
                        imageProxy.close();
                    }
                }


            });

        }
        return binding.getRoot();
    }


    private boolean compareLicensePlates(CharSequence p_licensePlate) {
       boolean isCorrectLot = false;
        Log.i("VEHICLE PLATE ",p_licensePlate.toString());
        for(int vehicleCount = 0; vehicleCount < vehicleList.size();vehicleCount++ )
        {
            String vehicleListPlate = vehicleList.get(vehicleCount).get(4).toString();
            Log.i("VEHICLE LIST PLATE ", vehicleListPlate);
            if(vehicleListPlate.contains(p_licensePlate.toString()))
            {
                isCorrectLot = true;
            }
        }

        return isCorrectLot;

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