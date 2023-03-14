package com.example.scanalot;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scanalot.databinding.FragmentPrintPreviewBinding;
import com.example.scanalot.databinding.FragmentSelectLotBinding;
import com.google.type.DateTime;

import java.time.LocalDateTime;

/**
 * This class is used for the printPreviewFragment. It creates the fragment and uses the fragment_print_preview layout. This will be used for
 * when the user taps the "save and print" button which will direct them to this to see a preview of the ticket they are printing out.
 *
 * @author Andrew Hoffer
 * @Created 2/4/23
 * @Contributors Andrew Hoffer - 2/4/23 - Created the fragment
 */

public class PrintPreviewFragment extends Fragment {

    FragmentPrintPreviewBinding binding;
    MainActivity mainActivity;
    TicketDataViewModel viewModel;
    /**
     * Method in which executes after the view has been created. It is saving the state of the view
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            binding.textViewViolationTime.setText(binding.textViewViolationTime.getText() + LocalDateTime.now().toString());
            binding.textView2.setText(binding.textView2.getText() + viewModel.getParkingLot().getValue());
            binding.textViewFineAmount.setText(binding.textViewFineAmount.getText() + " $100.00");
            binding.textViewLicensePlate.setText(binding.textViewLicensePlate.getText() + viewModel.getLicenseNumber().getValue());
            binding.textViewVehicleColor.setText(binding.textViewVehicleColor.getText() + viewModel.getVehicleList().getValue().get(viewModel.getReferenceNum()).getModel().toString());
            binding.textViewVehicleModel.setText(binding.textViewVehicleModel.getText() + viewModel.getVehicleList().getValue().get(viewModel.getReferenceNum()).getColor().toString());
        }

    }

    /**
     * Method in which executes during the creation of the view. It is creating an instance of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPrintPreviewBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(TicketDataViewModel.class);
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