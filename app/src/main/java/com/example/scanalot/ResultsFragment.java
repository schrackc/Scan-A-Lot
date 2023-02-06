package com.example.scanalot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scanalot.databinding.FragmentResultsBinding;
import com.example.scanalot.databinding.FragmentScanFragmentBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsFragment extends Fragment {

    FragmentResultsBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        Button fillCitationButton = binding.resultFillCitationButton;
        fillCitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               NavDirections navAction = ResultsFragmentDirections.actionResultsFragmentToFillCitationFragment2();
                Navigation.findNavController(view).navigate(navAction);

            }
        });


        binding.ResultTextView.setText("Pass Expired - Wrong Lot");


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

}