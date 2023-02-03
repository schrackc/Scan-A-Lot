package com.example.scanalot;


import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;



import com.example.scanalot.databinding.FragmentScanFragmentBinding;



public class scan_fragment extends Fragment {
FragmentScanFragmentBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        Button outlinedManualButton = binding.outlinedButton;
       Log.i("onCreate","scan fragment created");
       outlinedManualButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.i("Button Click", "manual button clicked !!!!");
               NavDirections navAction = scan_fragmentDirections.actionScanFragmentToManualEntryFragment();
               Navigation.findNavController(view).navigate(navAction);
           }
       });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScanFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
