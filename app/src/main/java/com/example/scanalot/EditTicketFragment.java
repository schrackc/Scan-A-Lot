package com.example.scanalot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.FragmentEditTicketBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

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
    Button btnSearchTicket;
    EditText editTicketID;
    TicketDataViewModel viewModel;
    FirebaseFirestore db;
    NavDirections navAction;


    /**
     * Method in which executes after the view has been created. It is saving the state of the view
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSearchTicket = binding.editTicketSearchButton;
        editTicketID = binding.editTicketSearchBox;
        db = FirebaseFirestore.getInstance();
        viewModel = new ViewModelProvider(requireActivity()).get(TicketDataViewModel.class);
        btnSearchTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTicketID.getText().length()>0)
                {
                    Integer searchedTicketNum = Integer.parseInt(String.valueOf(editTicketID.getText()));
                    db.collection("Tickets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    Integer ticketNum =  Integer.parseInt(String.valueOf(document.get("TicketNum")));
                                    if(ticketNum.equals(searchedTicketNum))
                                    {
                                        String strOffense = document.getString("Offense");
                                        String[] arrCitations = strOffense.substring(1, strOffense.length()-1).split(",");
                                        ArrayList<String> arrListCitations = new ArrayList<String>(Arrays.asList(arrCitations));
                                        Log.i("Ticket", "Found Ticket");
                                        viewModel.setLicenseState(document.getString("LicenseState"));
                                        viewModel.setLicenseNumber(document.getString("LicenseNum"));
                                        viewModel.setVehicleColor(document.getString("CarColor"));
                                        viewModel.setVehicleModel(document.getString("CarModel"));
                                        viewModel.setVehicleMake(document.getString("CarMake"));
                                        viewModel.setArrSelectedOffenses(arrListCitations);
                                        viewModel.setOfficerNotes(document.getString("OfficerNotes"));
                                        viewModel.setTicketID(ticketNum);
                                        viewModel.setEditTicketDocumentID(document.getId());
                                        viewModel.setEditTicketParkingLot(document.getString("ParkingLot"));

                                        Log.i("Edit Ticket Doc ID" ,viewModel.getEditTicketDocumentID().getValue().toString());
                                        Log.i("Edit Ticket License State" ,viewModel.getLicenseState().getValue().toString());
                                        Log.i("Edit Ticket LicenseNum" ,viewModel.getLicenseNumber().getValue().toString());
                                        Log.i("Edit Ticket CarColor" ,viewModel.getVehicleColor().getValue().toString());
                                        Log.i("Edit Ticket Car Make" ,viewModel.getVehicleMake().getValue().toString());
                                        Log.i("Edit Ticket CarModel" ,viewModel.getVehicleModel().getValue().toString());
                                        Log.i("Edit Ticket ArrCitations" ,viewModel.getArrSelectedOffenses().getValue().toString());
                                        Log.i("Edit Ticket OfficerNotes" ,viewModel.getOfficerNotes().getValue().toString());
                                        Log.i("Edit TicketNum" ,viewModel.getTicketID().getValue().toString());
                                        Log.i("Edit Ticket Parking Lot" ,viewModel.getEditTicketParkingLot().getValue().toString());

                                        navAction = EditTicketFragmentDirections.actionEditTicketFragmentToEditTicketCitationFragment();
                                        Navigation.findNavController(view).navigate(navAction);

                                    }
                                }
                            }
                        }
                    });

                }else
                {
                    Toast.makeText(getContext(),"Please Enter Ticket ID", Toast.LENGTH_LONG);
                }
            }
        });

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