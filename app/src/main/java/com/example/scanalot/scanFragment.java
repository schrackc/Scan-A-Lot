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



public class scanFragment extends Fragment {
FragmentScanBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        Button outlinedManualButton = binding.outlinedButton;
        Button resultScanButton = binding.ResultsScanButton;
       Log.i("onCreate","scan fragment created");
       outlinedManualButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.i("Button Click", "manual button clicked !!!!");
               NavDirections navAction = scanFragmentDirections.actionScanFragmentToManualEntryFragment();
               Navigation.findNavController(view).navigate(navAction);
           }
       });



        resultScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navAction = scanFragmentDirections.actionScanFragmentToResultsFragment();
                Navigation.findNavController(view).navigate(navAction);
            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
