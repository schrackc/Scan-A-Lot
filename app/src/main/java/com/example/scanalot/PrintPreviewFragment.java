package com.example.scanalot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.scanalot.databinding.FragmentPrintPreviewBinding;
import com.example.scanalot.databinding.FragmentSelectLotBinding;

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
    /**
     * Method in which executes after the view has been created. It is saving the state of the view
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Method in which executes during the creation of the view. It is creating an instance of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPrintPreviewBinding.inflate(inflater, container, false);
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