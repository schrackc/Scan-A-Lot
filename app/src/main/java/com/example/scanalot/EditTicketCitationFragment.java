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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.FragmentEditTicketCitationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used for the EditTicketCitationFragment. It creates the fragment and uses the fragment_edit_ticket_citation layout. This will be used for
 * further handling of the data when the edit ticket page is used to search for a valid ID.
 *
 */

public class EditTicketCitationFragment extends Fragment {
    FragmentEditTicketCitationBinding binding;
    NavDirections navAction;
    TextView textView;
    Button btnCancel;
    Button btnSavePrint;

    EditText editText_OfficerID;
    EditText editText_OfficerNotes;
    TicketDataViewModel viewModel;
    Spinner chooseStateSpinner;
    Spinner chooseLotSpinner;
    ArrayList<String> offensesArray;

    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    String strOfficerID = "";
    CollectionReference officerCollection;
    MutableLiveData<Integer> ticketNumber = new MutableLiveData<Integer>();
    ArrayList<String> selectedOffenseArray = new ArrayList<String>();

    //MutableLiveData<Integer> ticketNumber = new MutableLiveData<Integer>();

    /**
     * Method in which executes after the view has been created. There are two event listeners on buttonSave and btnPrint which Navigate to other
     * fragments based on a click. Lastly, there is a click event on a text view event listener. When it is clicked, an Alert Dialogue Box appears
     * in which the user will be able to add one or more violations.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TicketDataViewModel.class);
        textView = binding.editAddCitations;
        btnCancel = binding.editCancelButton;
        btnSavePrint = binding.editSavePrintButton;
        editText_OfficerID = binding.editOfficerID;
        editText_OfficerNotes = binding.editNotes;
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.connectToPrinter();
        //get the instances for firebase
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();


        //query db for username associated with the current User's email.

       /*Get officer id*/
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
                            strOfficerID = document.get("OfficerID").toString();
                            viewModel.setOfficerID(strOfficerID);
                            editText_OfficerID.setText(strOfficerID);
                        }
                    }
                }
            }
        });
        Log.i("Current User ID", currentUser.getEmail());
        Log.i("Current User ID", currentUser.getUid());

        //show the value in OfficerId editText


        chooseStateSpinner = binding.editChooseTheStateSpinner;
        chooseLotSpinner = binding.editChooseLotSpinner;
        autoEditTicketCitationData();

        viewModel.getParkingLot().getValue();

        btnSavePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    //set the notes live data variable,print,and send ticket to Firestore
                    viewModel.setOfficerNotes(editText_OfficerNotes.getText().toString());
                    mainActivity.printText();
                    createTicket();

                }

               // navAction = EditTicketFragmentDirections.actionEditTicketFragmentToEditTicketCitationFragment();
                //get the nav controller and tell it to navigate
              //  Navigation.findNavController(view).navigate(navAction);

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navAction = EditTicketCitationFragmentDirections.actionEditTicketCitationFragmentToScanFragment();
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
                offensesArray = viewModel.getArrOffenses().getValue();
                // use size() to get array size.
                String[] violations = offensesArray.toArray(new String[offensesArray.size()]);
                boolean[] checkBoxes = new boolean[violations.length];

                //The array to add choices to
                ArrayList<Integer> langList = new ArrayList<>();

                //sets the items in the box and allows the user to check and umcheck the violations
                builder.setMultiChoiceItems(violations, checkBoxes, new DialogInterface.OnMultiChoiceClickListener() {
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
                            stringBuilder.append(violations[langList.get(j)]);

                            // check condition
                            if (j != langList.size() - 1) {

                                // When j value  not equal to lang list size - 1, add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        textView.setText(stringBuilder.toString());


                        for (int k = 0; k < checkBoxes.length; k++) {
                            if (checkBoxes[k]) {
                                if (!selectedOffenseArray.contains(violations[k])) {
                                    selectedOffenseArray.add(violations[k]);
                                }
                            }
                        }
                        viewModel.setArrSelectedOffenses(selectedOffenseArray);
                        Log.d("SELECTED ARRAY", selectedOffenseArray.toString());
                        //selectedOffenseArray.clear();
                        ArrayList<String> fineAmountArray = new ArrayList<>();
                        // Query to get corresponding fine amounts from selected offenses.
                        // Will be used to get total fine on Print Preview page.
                        for (String offense : viewModel.getArrSelectedOffenses().getValue()) {
                            CollectionReference offensesCollection = db.collection("Offenses");
                            Query query = offensesCollection.whereEqualTo("OffenseType", offense);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // Retrieve the corresponding FineAmount field value and store it in fineAmountArray
                                            String fineAmount = document.get("FineAmount").toString();
                                            fineAmountArray.add(fineAmount);
                                            Log.d("FineAmount", fineAmountArray.toString());
                                        }
                                    } else {
                                        Log.d("FineAmount", "Error getting documents: ", task.getException());
                                    }
                                    viewModel.setArrFineAmount(fineAmountArray);
                                }
                            });
                        }// end of for loop for fines query.
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


    private void createTicket()
    {
        String officerID = editText_OfficerID.getText().toString();
        String carModel = binding.editVehicleModel.getText().toString();
        String carColor = binding.editVehicleColor.getText().toString();

        String[] citationValues = binding.editAddCitations.getText().toString().split(",");

        List<String> citations = Arrays.asList(citationValues);
        String carLicenseNumber = binding.editTextPlateNumber.getText().toString();
        String citationTime = LocalDateTime.now().toString();
        String carParkingLot = binding.editChooseLotSpinner.getSelectedItem().toString();
        String carState = binding.editChooseTheStateSpinner.getSelectedItem().toString();
        String carMake = binding.editVehicleMake.getText().toString();
        String officerNotes = binding.editNotes.getText().toString();



        String offense ="";
        String totalFine = "";

        if(citations.size()>0)
        {
            offense = citations.toString();

            totalFine =  calculateTotalFine();
        }
        viewModel.setOfficerID(officerID);
        viewModel.setVehicleModel(carModel);
        viewModel.setVehicleColor(carColor);
        viewModel.setArrSelectedOffenses(new ArrayList<String>(citations));
        viewModel.setLicenseNumber(carLicenseNumber);
        viewModel.setEditTicketParkingLot(carParkingLot);
        viewModel.setLicenseState(carState);
        viewModel.setVehicleMake(carMake);
        viewModel.setOfficerNotes(officerNotes);

        Map<String, Object> data = new HashMap<>();
        data.put("CarModel", carModel);
        data.put("CarColor", carColor);
        data.put("FineAmount", "$" + totalFine);
        data.put("LicenseNum", carLicenseNumber);
        data.put("Offense", offense);
        data.put("Officer", officerID);
        data.put("ParkingLot", carParkingLot);
        data.put("Time", citationTime);
        data.put("LicenseState", carState);
        data.put("CarMake", carMake);
        data.put("OfficerNotes", officerNotes);
        data.put("ArrayOffenses", citations);
        data.put("TicketNum", viewModel.getTicketID().getValue());
        //Update document with certain ID
        db.collection("Tickets").document(viewModel.getEditTicketDocumentID().getValue()).update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            navAction = EditTicketCitationFragmentDirections.actionEditTicketCitationFragmentToEditPrintPreviewFragment();
                            //get the nav controller and tell it to navigate
                            Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_main).navigate(navAction);
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Ticket Failed To Update", Toast.LENGTH_LONG);
                        }
                    }
                });
    }



    private void DBConnectionFailed() {
        Toast.makeText(getContext(), "Failed To Send Ticket To Database. Please Try Again", Toast.LENGTH_LONG).show();

    }

    /**
     * set Live Data values to view values within Fragment
     */
    private void autoEditTicketCitationData() {
        Log.i("LIVE DATA edit CITATION FRAG", "LICENSE NUM: " + viewModel.getLicenseNumber().getValue());
        Log.i("LIVE DATA edit CITATION FRAG", "LICENSE STATE: " + viewModel.getLicenseState().getValue());

        //set the value editTextPlateNumber box
        binding.editTextPlateNumber.setText(viewModel.getLicenseNumber().getValue());
        //set the value of the chooseStateSpinner
        ArrayAdapter chooseStateAdapter = (ArrayAdapter) chooseStateSpinner.getAdapter();
        chooseStateSpinner.setSelection(chooseStateAdapter.getPosition(viewModel.getLicenseState().getValue()));
        //set the value of the chooseLotSpinner
        ArrayAdapter chooseLotAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, viewModel.getArrParkingLotList().getValue().toArray());
        //ArrayAdapter chooseLotAdapter = (ArrayAdapter) chooseLotSpinner.getAdapter();
        chooseLotSpinner.setAdapter(chooseLotAdapter);
        chooseLotSpinner.setSelection(chooseLotAdapter.getPosition(viewModel.getParkingLot().getValue()));
        //set the value editVehicleModel box
        binding.editVehicleModel.setText(viewModel.getVehicleModel().getValue());
        //set the value editVehicleModel box
        binding.editVehicleColor.setText(viewModel.getVehicleColor().getValue());
        //set balue for editVehicleMake box
        binding.editVehicleMake.setText(viewModel.getVehicleMake().getValue());
        //set citation text
        String strViolationText = "";
        if (viewModel.getArrSelectedOffenses().getValue() != null) {
            for (String strViolation : viewModel.getArrSelectedOffenses().getValue()) {
                strViolationText += (strViolation + ",");
            }
        }
        binding.editAddCitations.setText(strViolationText);
        //set officer notes
        editText_OfficerNotes.setText(viewModel.getOfficerNotes().getValue());

    }

    public String calculateTotalFine(){
        ArrayList<String> fineAmountTotalArray = new ArrayList<>();
        ArrayList<String> emptyArray = new ArrayList<>();
        emptyArray.add("");
        if (viewModel.getArrFineAmount().getValue() == null){fineAmountTotalArray.add("$0");}
        else {fineAmountTotalArray = viewModel.getArrFineAmount().getValue();}
        int totalFineAmount = 0;
        for (String fineTotal : fineAmountTotalArray) {
            if (fineTotal.length() == 0) {
                totalFineAmount = 0;
            }
            else {
                int totalAmount = Integer.parseInt(fineTotal.substring(1));
                totalFineAmount += totalAmount;
            }
        }
        String totalFineAmountString = Integer.toString(totalFineAmount);
//        viewModel.setArrFineAmount(emptyArray);
        return totalFineAmountString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditTicketCitationBinding.inflate(inflater, container, false);
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