package com.example.scanalot;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Preview;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.scanalot.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class is used for the Main Activity. It creates the Main Activity and uses the activity_main layout. This will be used for
 * hosting all the fragments after the user logs in.
 *
 * @author Andrew Hoffer
 * @author Nick Downey
 * @Created 1/21/23
 * @Contributors Andrew Hoffer - 1/21/23 - Created the Activity and handlers
 * @Contributors Nick Downey - 1/30/23 - Added CameraX code for permissions and added a button
 * @Contributors Nick Downey - 2/23/23 - Added updating of location banner from SelectLotFragment spinner.
 * @Contributors Curtis Schrack - 3/8/23 - Add dynamic variables for license number and license plate and connect firestore
 */
public class MainActivity extends AppCompatActivity implements SelectLotFragment.OnSpinnerSelectedListener {
    // CameraX code
    private static final String[] CAMERA_PERMISSION = new String[]{android.Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;


    //the bottom nav menu
    public BottomNavigationView bottomNavigationView;

    //Dynamic variables to show license information between screens
    public String strLicenseNumber;
    public String strLicenseState;

    //for every activity or fragment, there is a BindingClass that allows you to access the views in a easy fashion
    private ActivityMainBinding binding;

    //gets the action from the NavDirectionsObject
    private NavDirections navAction;

    // Preview shows a live image of what the camera feed sees.
    private Preview preview;

    // TextView that will be updated by a spinner on SelectLotFragment.
    private TextView locationBanner;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //A list of all the vehicles in the database
    private ArrayList <ArrayList<Object>> arrVehicles = new ArrayList<ArrayList<Object>>();

    //View Model for passing data between fragments/parent Activities
    private TicketDataViewModel viewModel;

    /**
     * Creates the Main Activity and sets the bottom navigation bar to the navigation controller. The navigation controller is the
     * nav_host_fragment located in content_main.xml. The nav controller is responsible for controlling/replacing fragments within
     * this activity.The nav controller(nav_host_fragment) is included in activity_main.xml in order to do this. The nav controller needs a
     * nav map in order to be told what fragment to replace the current fragment inside of it with next. That is why we
     * connect the nav map to the nav_host_fragment in nav_graph.xml.
     * NavigationUI is a object built just for setting up menus. We can easily link the bottomNavigationBar to the nav controller
     * using this. The only thing we must do is make sure that the nav menu ids in bottom_nav.xml match up with the ids in the nav graph. This
     * will link the menu items to the appropriate destination without us having to deal with popping the fragments off the stack and creating
     * a whole bunch of nav actions (the linkage arrows between the fragments) in nav_graph.xml.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Instantiate View Model for passing data between fragment and this activity
        viewModel = new ViewModelProvider(this).get(TicketDataViewModel.class);
        //creates an instance of Main Activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //sets the Content we want to see by getting the root view  (the parent to all views)
        setContentView(binding.getRoot());
        // gets the location banner.
        locationBanner = findViewById(R.id.geolocationBanner);
        //gets the bottom menu
        bottomNavigationView = binding.bottomNav;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        //gets the nav controller within the nav host fragment
        NavController navController = navHostFragment.getNavController();
        //binds the menu to the nav controller
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // CameraX Code ------------------------------------------------------ //
        // 3 Methods required are: requestPermission, enableCamera, and hasCameraPermission
        // I the following requests permission when the activity that the camera is within is created.
        // Creating CameraPreview Permission Dialogue. Asks on create.
        if (!hasCameraPermission()) {
            requestPermission();
        } else {
            enableCamera();
        }

        // Gets firebase Vehicles collection and adds all the records to the dbVehicles variable
        db.collection("Vehicles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int iRowValue = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Add values to 2d Array List
                                arrVehicles.add(new ArrayList<>());
                                arrVehicles.get(iRowValue).add(0, document.getString("OwnerFirstName") + " " + document.getString("OwnerLastName"));
                                arrVehicles.get(iRowValue).add(1, document.getString("Make"));
                                arrVehicles.get(iRowValue).add(2, document.getString("Model"));
                                arrVehicles.get(iRowValue).add(3, document.getString("Color"));
                                arrVehicles.get(iRowValue).add(4, document.getString("LicenseNum"));
                                arrVehicles.get(iRowValue).add(5, document.getString("LicenseState"));
                                arrVehicles.get(iRowValue).add(6, document.get("ParkingLot"));
                                Log.d("GotDoc", document.getId() + " => " + document.getData());
                                iRowValue++;
                            }
                        } else {
                            Log.d("NoDoc", "Error getting documents: ", task.getException());
                        }
                    }
                });

        //set the vehicle array in view model to the array of data retrieved from firebase
        viewModel.setLicenseVehicleList(arrVehicles);
    }// end of onCreate()

    /**
     * Method for cameraX launching. Checks permissions and returns bool if perms given or not.
     *
     * @return true or false
     */
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Method for requesting permission of camera.
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, CAMERA_PERMISSION, CAMERA_REQUEST_CODE);
    }


    /**
     * Method for creating new Intent class to start activity.
     */
    private void enableCamera() {
//        navAction = scanFragmentDirections.actionScanFragmentToCameraActivity();
//        Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(navAction);
    }
    // End of CameraX -------------------------------------------------- //

    /**
     * Method that handles updating the textView geolocationBanner. It pulls from the selectLot spinner
     * on the SelectLotFragment.
     *
     * @param item
     */
    @Override
    public void onSpinnerSelected(String item) {
        // setting text with whatever is selected in the select lot fragment.
        String location = "Location: " + item;
        locationBanner.setText(location);
    }

    /**
     * Inflates the menu; this adds items to the action bar if it is present.
     *
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}


