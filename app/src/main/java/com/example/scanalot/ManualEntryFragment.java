package com.example.scanalot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.ActivityManualEntryBinding;

/**
 * This class is used for the ManualEntryFragment. It creates the fragment and uses the activity_manual_entry layout. This will be used for
 * when user taps the manual entry button and comes to this fragment to manually search for the license plate and determine if it belongs
 * in the designated lot
 *
 * @author Andrew Hoffer
 * @Created 1/22/23
 * @Contributors Andrew Hoffer - 1/22/23 - Created the fragment
 * @Contributors Curtis Schrack - 3/8/23 - Search Button Navigation
 * @Contributors Nick Downey - 3/8/23 - Observed Vigilantly
 */
public class ManualEntryFragment extends Fragment {
    //here we are using a binding class that is autogenerated when creating a layout file. This allows us to easily access the views.
    private ActivityManualEntryBinding binding;
    NavDirections navAction;
    Button btn_manualSearch;
    TicketDataViewModel viewModel;
    /**
     * Method in which executes after the view has been created. It is saving the state of the view
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get the view model that the main activity has
        viewModel = new ViewModelProvider(requireActivity()).get(TicketDataViewModel.class);
        // If search button clicked gives license number and license state value field values to dynamic values in MainActivity the navigates to results page
        btn_manualSearch = binding.manualSearchButton;
        btn_manualSearch.setEnabled(true);
        btn_manualSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strLicenseNumTemp = binding.plateSearch.getText().toString().trim();
                String strLicenseStateTemp = binding.stateSpinner.getSelectedItem().toString();
                if ((strLicenseNumTemp.length() > 1 && strLicenseNumTemp.length() < 9) && strLicenseStateTemp != null) {
                setTicketDataViewModelValues(strLicenseNumTemp,strLicenseStateTemp);
                Log.i("MANUAL ENTRY SET VALUES",viewModel.getLicenseState().getValue().toString());
                Log.i("MANUAL ENTRY SET VALUES",viewModel.getLicenseNumber().getValue().toString());

                    navAction = ManualEntryFragmentDirections.actionManualEntryFragmentToResultsFragment();
                    Navigation.findNavController(view).navigate(navAction);
                }
            }
        });
    }

    private void setTicketDataViewModelValues(String p_StrLicenseNumTemp, String p_StrLicenseStateTemp) {

        //set the live data objects in the view model
        viewModel.setLicenseNumber(p_StrLicenseNumTemp);
        viewModel.setLicenseState(p_StrLicenseStateTemp);

    }

    /**
     * Method in which executes during the creation of the view. It is creating an instance of this fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityManualEntryBinding.inflate(inflater, container, false);
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

