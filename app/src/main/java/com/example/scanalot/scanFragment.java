package com.example.scanalot;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.FragmentScanBinding;

/**
 * This class is used for the scanFragment. It creates the fragment and uses the fragment_scan layout. This is used as the main page when the user
 * logs into the app. This fragment will be responsible for hosting the camera and getting the scan from the license plate.
 *
 * @author Andrew Hoffer
 * @Created 1/21/23
 * @Contributors Andrew Hoffer - 1/21/23 - Created the fragment
 */

public class scanFragment extends Fragment {
    FragmentScanBinding binding;
    NavDirections navAction;
    Button btnManualEntry;
    Button btnResultScan;

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
                navAction = scanFragmentDirections.actionScanFragmentToManualEntryFragment();
                Navigation.findNavController(view).navigate(navAction);
            }
        });
        //event listener on the result scan button. Navigate to result fragment.
        btnResultScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navAction = scanFragmentDirections.actionScanFragmentToResultsFragment();
                Navigation.findNavController(view).navigate(navAction);
            }
        });
    }

    /**
     * Method in which executes during the creation of the view. It is creating an instance of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScanBinding.inflate(inflater, container, false);
        return binding.getRoot();
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
