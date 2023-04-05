package com.example.scanalot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scanalot.databinding.FragmentEditTicketBinding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * This class is used for the EditTicketFragment. It creates the fragment and uses the fragment_edit_ticket layout. This will be used for
 * further handling of the data that will be submitted on this fragment
 *
 * @author Andrew Hoffer
 * @Created 1/30/23
 * @Contributors Andrew Hoffer - 1/30/23 - Created the fragment
 */


public class EditTicketFragment extends Fragment {
    FragmentEditTicketBinding binding;
   // Button btnSearchTicket = binding.editTicketSearchButton;
   // EditText editTicketID = binding.editTicketSearchBox;


    /**
     * Method in which executes after the view has been created. It is saving the state of the view
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


/*        btnSearchTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTicketID.getText().length()>0)
                {


                }else
                {
                    Toast.makeText(getContext(),"Please Enter Ticket ID", Toast.LENGTH_LONG);
                }
            }
        });*/

    }

    /**
     * Method in which executes during the creation of the view. It is creating an instance of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditTicketBinding.inflate(inflater, container, false);
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