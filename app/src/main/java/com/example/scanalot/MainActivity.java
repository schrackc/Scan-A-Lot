package com.example.scanalot;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;


// Bottom Nav Imports
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
// End of Bottom Nav Imports

import com.google.android.material.navigation.NavigationBarView;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scanalot.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements  ReplacementFragment {
    // CameraX code ------------------------------------------------ //
    private static final String[] CAMERA_PERMISSION = new String[]{android.Manifest.permission.CAMERA};
                private static final int CAMERA_REQUEST_CODE = 10;

    // End of CameraX code ----------------------------------------- //
   public BottomNavigationView bottomNavigationView;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = binding.bottomNav;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);





        // CameraX Code ------------------------------------------------------ //
        // 3 Methods required are: requestPermission, enableCamera, and hasCameraPermission
        // I the following makes it so that the camera comes up with the nav bar. It can also use a button to appear.
        // Still working in getting it automatically when the activity starts.
        Button enableCamera = findViewById(R.id.enableCamera);
        enableCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCameraPermission()) {
                    enableCamera();
                } else {
                    requestPermission();
                }
            }
        });

        // End of CameraX -------------------------------------------------- //

    }// end of onCreate()

    // Method for cameraX launching. Checks permissions and returns bool if perms given or not.
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    // Method for requesting permission of camera.
    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }
    // Method for creating new Intent class to start activity.
    private void enableCamera(){
        NavDirections navAction = scan_fragmentDirections.actionScanFragmentToCameraActivity();
        Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigate(navAction);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



   @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void replaceParentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentmainId, fragment).commit();

    }
}


