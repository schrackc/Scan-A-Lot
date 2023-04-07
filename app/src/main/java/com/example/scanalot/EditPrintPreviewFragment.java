package com.example.scanalot;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.scanalot.databinding.FragmentEditPrintPreviewBinding;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class is used for the EditPrintPreviewFragment. It creates the fragment and uses the fragment_edit_print_preview layout. This will be used for
 * when the user taps the "save and print" button which will direct them to this to see a preview of the edited ticket they are printing out.
 *
 */
public class EditPrintPreviewFragment extends Fragment {

    FragmentEditPrintPreviewBinding binding;
    MainActivity mainActivity;
    TicketDataViewModel viewModel;
    /**
     * Method in which executes after the view has been created. It is saving the state of the view
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String strMonth = LocalDateTime.now().getMonth().toString() + "/";
            int iDay = LocalDateTime.now().getDayOfMonth();
            int iYear = LocalDateTime.now().getYear();
            //binding.textViewTicketID.setText(binding.textViewTicketID.getText() + " "+viewModel.getTicketID().getValue());

            viewModel.getTicketID().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.editTextViewTicketID.setText("Ticket ID: "+ticketIDPrinting());
                }
            });

            binding.editTextViewViolationTime.setText(binding.editTextViewViolationTime.getText() +" "+ strMonth + iDay + "/" + iYear);

            binding.editTextViewReportingOfficer.setText(binding.editTextViewReportingOfficer.getText() +" "+ reportingOfficerPrinting());

            binding.editTextViewCitationLocation.setText(binding.editTextViewCitationLocation.getText() +" "+ citationLocationPrinting());

            binding.editTextViewViolationType.setText(binding.editTextViewViolationType.getText() + " " + violationTypePrinting());

            binding.editTextViewFineAmount.setText(binding.editTextViewFineAmount.getText() + " $" + calculateTotalFine());

            binding.editTextViewLicensePlate.setText(binding.editTextViewLicensePlate.getText() +" " +  licensePlatePrinting());

            binding.editTextViewVehicleColor.setText(binding.editTextViewVehicleColor.getText() + " " + vehicleColorPrinting());

            binding.editTextViewVehicleModel.setText(binding.editTextViewVehicleModel.getText() + " " + vehicleModelPrinting());

            binding.editTextViewVehicleMake.setText(binding.editTextViewVehicleMake.getText() + " " + vehicleMakePrinting());

        }
    }

    /**
     * Method in which executes during the creation of the view. It is creating an instance of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(TicketDataViewModel.class);
        binding = FragmentEditPrintPreviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Function to calculate the total fine amount for a ticket.
     * It takes in an array from the arrFineAmount view model that contains a set of string fine amounts, corresponding to the selected fines on the FillCitationFragment.
     * The function then iterates through the String array, while removing the first character ($,€,£).
     * It then parses integers from the remaining string and adds them up in one variable that is returned.
     * Note: The printed statement on the ticket will only have USD signs.
     */
    public String calculateTotalFine(){
        ArrayList<String> fineAmountTotalArray = new ArrayList<>();
        ArrayList<String> emptyArray = new ArrayList<>();
        emptyArray.add("");
        if (viewModel.getArrFineAmount().getValue() == null){fineAmountTotalArray.add("$0");}
        else {fineAmountTotalArray = viewModel.getArrFineAmount().getValue();}
        int totalFineAmount = 0;
        for (String fineTotal : fineAmountTotalArray) {
            if (fineTotal.length() == 0) {
                totalFineAmount = 0;
            }
            else {
                int totalAmount = Integer.parseInt(fineTotal.substring(1));
                totalFineAmount += totalAmount;
            }
        }
        String totalFineAmountString = Integer.toString(totalFineAmount);
//        viewModel.setArrFineAmount(emptyArray);
        return totalFineAmountString;
    }

    /**********************
     The following are a series of functions that read their respective view models and print
     a meaningful message to the user if they are detected as null or not.
     **********************/
    public String ticketIDPrinting()
    {
        if (viewModel.getTicketID().getValue() == null)
        {
            return "ID Not Found";
        }
        else {
            return viewModel.getTicketID().getValue().toString();
        }
    }

    public String reportingOfficerPrinting()
    {
        if (viewModel.getOfficerID().getValue() == null)
        {
            return "ID Not Found";
        }
        else {
            return viewModel.getOfficerID().getValue();
        }
    }

    public String citationLocationPrinting()
    {
        if (viewModel.getParkingLot().getValue() == null)
        {
            return "Lot Not Found";
        }
        else {
            return viewModel.getParkingLot().getValue();
        }
    }

    public String violationTypePrinting()
    {
        if (viewModel.getArrSelectedOffenses().getValue() == null)
        {
            return "Offenses Not Found";
        }
        else {
            return viewModel.getArrSelectedOffenses().getValue().toString();
        }
    }

    public String licensePlatePrinting()
    {
        if (viewModel.getLicenseNumber().getValue() == null)
        {
            return "Plate Number Not Found";
        }
        else {
            return viewModel.getLicenseNumber().getValue();
        }
    }

    public String vehicleColorPrinting()
    {
        if (viewModel.getVehicleColor().getValue() == null)
        {
            return "Color Not Found";
        }
        else {
            return viewModel.getVehicleColor().getValue();
        }
    }

    public String vehicleModelPrinting()
    {
        if (viewModel.getVehicleModel().getValue() == null)
        {
            return "Model Not Found";
        }
        else {
            return viewModel.getVehicleModel().getValue();
        }
    }

    public String vehicleMakePrinting()
    {
        if (viewModel.getVehicleMake().getValue() == null)
        {
            return "Make Not Found";
        }
        else {
            return viewModel.getVehicleMake().getValue();
        }
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