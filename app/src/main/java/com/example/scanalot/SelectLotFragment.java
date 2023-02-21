package com.example.scanalot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.scanalot.databinding.FragmentSelectLotBinding;

/**
 * This class is used for the SelectLotFragment. It creates the fragment and uses the fragment_select_layout layout. This will be used for
 * when the user wants to select the parking lot they want to scan. This allows for the app to know which cars belong and dont belong in the
 * selected lot.
 *
 * @author Andrew Hoffer
 * @Created 1/21/23
 * @Contributors Andrew Hoffer - 1/21/23 - Created the fragment
 */
public class SelectLotFragment extends Fragment {

    FragmentSelectLotBinding binding;


    /**
     * Method in which executes after the view has been created. It is saving the state of the view.
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
        binding = FragmentSelectLotBinding.inflate(inflater, container, false);
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