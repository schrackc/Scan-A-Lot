package com.example.scanalot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.FragmentResultsBinding;

/**
 * This class is used for the resultsFragment. It creates the fragment and uses the fragment_results layout. This will be used right after
 * the user scans the license plate with their camera. This fragment will appear giving them the results from their scan and an option of writing
 * out a ticket
 *
 * @author Andrew Hoffer
 * @Created 2/4/23
 * @Contributors Andrew Hoffer - 2/4/23 - Created the fragment
 */

public class ResultsFragment extends Fragment {

    FragmentResultsBinding binding;
    NavDirections navAction;
    Button btnFillCitation;


    /**
     * Method in which executes after the view has been created. The fill citation button is given a click event listener to switch to the
     * fill citation fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getting the button
        btnFillCitation = binding.fillSavePrintButton;

        //setting the event listener
        btnFillCitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navAction = ResultsFragmentDirections.actionResultsFragmentToFillCitationFragment2();
                Navigation.findNavController(view).navigate(navAction);
            }
        });
        //sets the text of the result text view. Will need to add more to this functionality when camera is able to scan.
        binding.ResultTextView.setText("Pass Expired - Wrong Lot");
    }


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