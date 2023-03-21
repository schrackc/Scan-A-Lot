package com.example.scanalot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.FragmentFillCitationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class is used for the FillCitationFragment. It creates the fragment and uses the fragment_fill_citation layout. This will be used for
 * further handling of the data when the camera has scanned a license plate and the officer has clicked a fill citation button which brings
 * them to this fragment.
 *
 * @author Andrew Hoffer
 * @Created 1/30/23
 * @Contributors Andrew Hoffer - 1/30/23 - Created the fragment. Added view event listeners.
 */

public class FillCitationFragment extends Fragment {

    FragmentFillCitationBinding binding;
    NavDirections navAction;
    TextView textView;
    Button btnCancel;
    Button btnSavePrint;

    EditText editText_OfficerID;
    TicketDataViewModel viewModel;
    Spinner chooseStateSpinner;
    Spinner chooseLotSpinner;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    String strOfficerID = "";
    CollectionReference officerCollection;

    /**
     * Method in which executes after the view has been created. There are two event listeners on buttonSave and btnPrint which Navigate to other
     * fragments based on a click. Lastly, there is a click event on a text view event listener. When it is clicked, an Alert Dialogue Box appears
     * in which the user will be able to add one or more violations.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TicketDataViewModel.class);
        textView = binding.fillAddCitations;
        btnCancel = binding.fillCancelButton;
        btnSavePrint = binding.fillSavePrintButton;
        editText_OfficerID = binding.fillOfficerID;
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.connectToPrinter();
        //get the instances for firebase
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //get the officer collection
      //  officerCollection = db.collection("Officers");
        //get the currently logged in user and update Live variable
        currentUser = fAuth.getCurrentUser();

        //query db for username associated with the current User's email.
        db.collection("Officers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //get email of document
                        String strSnapEmail = (String) document.get("Email");
                       //if the email matches the current user email we got a match
                        if(strSnapEmail.equals(currentUser.getEmail().toString()))
                        {
                            strOfficerID =(String)document.get("Username");
                            viewModel.setOfficerID(strOfficerID);
                            editText_OfficerID.setText(strOfficerID);
                        }
                    }
                }
            }
        });
        Log.i("Current User ID",currentUser.getEmail());
        Log.i("Current User ID",currentUser.getUid());

        //show the value in OfficerId editText


        chooseStateSpinner = binding.fillChooseTheStateSpinner;
        chooseLotSpinner = binding.fillChooseLotSpinner;
        autoFillCitationData();

        btnSavePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    MainActivity mainActivity =(MainActivity)getActivity();
                        mainActivity.printText();
                }

                navAction = FillCitationFragmentDirections.actionFillCitationFragment2ToPrintPreviewFragment();
                //get the nav controller and tell it to navigate
               Navigation.findNavController(view).navigate(navAction);

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navAction = FillCitationFragmentDirections.actionFillCitationFragment2ToScanFragment();
                Navigation.findNavController(view).navigate(navAction);
            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // set title
                builder.setTitle("Select Violations");
                // set dialog non cancelable
                builder.setCancelable(false);
                //creates the violation array
                String[] citations = new String[]{"Violation A", "Violation B", "Violation C"};
                //creates the checkboxes
                boolean[] checkBoxes = new boolean[citations.length];

                //The array to add choices to
                ArrayList<Integer> langList = new ArrayList<>();

                //sets the items in the box and allows the user to check and umcheck the violations
                builder.setMultiChoiceItems(citations, checkBoxes, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int iIndex, boolean isClicked) {
                        if (isClicked) {
                            langList.add(iIndex);
                        } else {
                            langList.remove(Integer.valueOf(iIndex));
                        }
                    }
                });


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        int iJValue = 0;
                        for (int j = iJValue; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(citations[langList.get(j)]);

                            // check condition
                            if (j != langList.size() - 1) {

                                // When j value  not equal to lang list size - 1, add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        textView.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    /**
     * set Live Data values to view values within Fragment
     * */
    private void autoFillCitationData(){
        Log.i("LIVE DATA FILL CITATION FRAG", "LICENSE NUM: " + viewModel.getLicenseNumber().getValue());
        Log.i("LIVE DATA FILL CITATION FRAG", "LICENSE STATE: " + viewModel.getLicenseState().getValue());

        //set the value fillTextPlateNumber box
        binding.fillTextPlateNumber.setText(viewModel.getLicenseNumber().getValue());
        //set the value of the chooseStateSpinner
        ArrayAdapter chooseStateAdapter = (ArrayAdapter) chooseStateSpinner.getAdapter();
        chooseStateSpinner.setSelection(chooseStateAdapter.getPosition(viewModel.getLicenseState().getValue()));
        //set the value of the chooseLotSpinner
        ArrayAdapter chooseLotAdapter = (ArrayAdapter)chooseLotSpinner.getAdapter();
        chooseLotSpinner.setSelection(chooseLotAdapter.getPosition(viewModel.getParkingLot().getValue()));

        //If there is no reference value found means no license was found was found
        try {
            //set the value fillVehicleModel box
            binding.fillVehicleModel.setText((viewModel.getVehicleList().getValue().get(viewModel.getReferenceNum()).getModel()).toString());
            //set the value fillVehicleModel box
            binding.fillVehicleColor.setText((viewModel.getVehicleList().getValue().get(viewModel.getReferenceNum()).getColor()).toString());
        }catch (Exception e){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFillCitationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}