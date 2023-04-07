package com.example.scanalot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.FragmentResultsBinding;

import java.util.ArrayList;

/**
 * This class is used for the resultsFragment. It creates the fragment and uses the fragment_results layout. This will be used right after
 * the user scans the license plate with their camera. This fragment will appear giving them the results from their scan and an option of writing
 * out a ticket
 *
 * @author Andrew Hoffer
 * @Created 2/4/23
 * @Contributors Andrew Hoffer - 2/4/23 - Created the fragment
 * @Contributors Curtis Schrack - 3/10/23 - Check License plate with database vehicles
 */

public class ResultsFragment extends Fragment {

    FragmentResultsBinding binding;
    NavDirections navAction;
    Button btnFillCitation;
    MainActivity getActivity = (MainActivity)getActivity();
   // String strLicenseNumber = getActivity.strLicenseNumber;
    //String strLicenseState = getActivity.strLicenseState;
  //  ArrayList<ArrayList<Object>> arrVehicles = getActivity.arrVehicles;
    //view model
    TicketDataViewModel viewModel;
    String strLicenseNumber;
    String strLicenseState;
    ArrayList<VehicleCategories> arrVehicles;
    TextView DetectedTextBanner;

    //Test values
    Boolean isLicenseFound = false;
    Boolean isInRightLot = false;
    /**
     * Method in which executes after the view has been created. The fill citation button is given a click event listener to switch to the
     * fill citation fragment.
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get the view model which contains the live,changeable data that was made in main activity
        viewModel = new ViewModelProvider(requireActivity()).get(TicketDataViewModel.class);
        /*get the variables that were set in the view model and assign them to the variables*/
        strLicenseNumber = viewModel.getLicenseNumber().getValue();
        strLicenseState = viewModel.getLicenseState().getValue();
        arrVehicles = viewModel.getVehicleList().getValue();


        //Logging the results which were passed
        Log.i("LIVE DATA RESULTS FRAG", "LICENSE NUM: " + strLicenseNumber);
        Log.i("LIVE DATA RESULTS FRAG", "LICENSE STATE: " + strLicenseState);
        Log.i("LIVE DATA RESULTS FRAG", "LICENSE VEHICLES: " + arrVehicles.toString());
        //Check for if license info is in the database
        int iRowCheck = 0;
        for (; iRowCheck < arrVehicles.size(); iRowCheck++) {
            if (arrVehicles.get(iRowCheck).getLicNum().equals(strLicenseNumber) && arrVehicles.get(iRowCheck).getLicState().equals(strLicenseState)){
                //set values for autofill
                viewModel.setVehicleModel(arrVehicles.get(iRowCheck).getModel());
                viewModel.setVehicleMake(arrVehicles.get(iRowCheck).getMake());
                viewModel.setVehicleColor(arrVehicles.get(iRowCheck).getColor());
                isLicenseFound = true;
                break;
            }
        }

        //getting the button
        btnFillCitation = binding.fillSavePrintButton;

        //setting the event listener
        btnFillCitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 String resultsLotSpinner = binding.MultipleStateLicensePlateSpinner.getSelectedItem().toString();
//                 if (resultsLotSpinner!=null){
//                     viewModel.setParkingLot(resultsLotSpinner);
//                     Log.i("LOT SELECTED: ", viewModel.getParkingLot().getValue());
//                }
                navAction = ResultsFragmentDirections.actionResultsFragmentToFillCitationFragment2();
                Navigation.findNavController(view).navigate(navAction);

            }
        });

        //Different banner results depending on if car is found in the database
        if (isLicenseFound){
            //Check if in the right parking lot
            ArrayList<String> lstAuthParkingLots = arrVehicles.get(iRowCheck).getAuthParkingLot();
            for (int iParkLotIndex = 0; iParkLotIndex < arrVehicles.get(iRowCheck).getAuthParkingLot().size() && !isInRightLot; iParkLotIndex++){
                if (arrVehicles.get(iRowCheck).getAuthParkingLot().get(iParkLotIndex).equals(viewModel.getParkingLot().getValue()))
                    isInRightLot = true;
            }

            //Banner if vehicle is in proper lot
            if (isInRightLot){
                //sets the text of the result text view. Will need to add more to this functionality when camera is able to scan.
                binding.ResultTextView.setText("Vehicle in Correct Lot");
                binding.ResultTextView.setBackgroundColor(getResources().getColor(R.color.success));
            //Banner if vehicle is in the wrong lot
            } else {
                //sets the text of the result text view. Will need to add more to this functionality when camera is able to scan.
                binding.ResultTextView.setText("Vehicle in Wrong Lot");
                binding.ResultTextView.setBackgroundColor(getResources().getColor(R.color.fail));
            }
        //No vehicle found banner
        } else{
            //Reset reference and vehicle values
            viewModel.setVehicleModel(null);
            viewModel.setVehicleColor(null);
            //No vehicle was found in database print out response
            binding.ResultTextView.setText("No Record of Vehicle");
            binding.ResultTextView.setBackgroundColor(getResources().getColor(R.color.fail));
        }
        //No Parking Lot Selected Response
        if(viewModel.getParkingLot().getValue() == null){
            //No vehicle was found in database print out response
            binding.ResultTextView.setText("No Parking Lot Selected");
            binding.ResultTextView.setBackgroundColor(getResources().getColor(R.color.fail));
        }

        // Set banner text to detected text.
        DetectedTextBanner = binding.DetectedPlateTextView;
        DetectedTextBanner.setText(viewModel.getLicenseNumber().getValue());
    }


//   DocumentReference reference = db.collection("Tickets").document();
//   //set live data ticket id variable for creating a ticket on save and print in fill citation frag
//    viewModel.setTicketID(reference.getId());

    /**
     * Method in which executes during the creation of the view. It is creating an instance of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResultsBinding.inflate(inflater, container, false);
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