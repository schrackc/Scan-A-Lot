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

import com.example.scanalot.databinding.FragmentFillCitationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
 * This class is used for the FillCitationFragment. It creates the fragment and uses the fragment_fill_citation layout. This will be used for
 * further handling of the data when the camera has scanned a license plate and the officer has clicked a fill citation button which brings
 * them to this fragment.
 *
 * @author Andrew Hoffer
 * @Created 1/30/23
 * @Contributors Andrew Hoffer - 1/30/23 - Created the fragment. Added view event listeners.
 * @Contributors Nick Downey - Added violations and changed officerID pulling to be an int.
 */

public class FillCitationFragment extends Fragment {
    FragmentFillCitationBinding binding;
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
        textView = binding.fillAddCitations;
        btnCancel = binding.fillCancelButton;
        btnSavePrint = binding.fillSavePrintButton;
        editText_OfficerID = binding.fillOfficerID;
        editText_OfficerNotes = binding.fillNotes;
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.connectToPrinter();
        //get the instances for firebase
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //get the officer collection
        //  officerCollection = db.collection("Officers");
        //get the currently logged in user and update Live variable
        currentUser = fAuth.getCurrentUser();
        //viewModel.setArrSelectedOffenses(selectedOffenseArray);

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


        chooseStateSpinner = binding.fillChooseTheStateSpinner;
        chooseLotSpinner = binding.fillChooseLotSpinner;
        autoFillCitationData();

        viewModel.getParkingLot().getValue();

        btnSavePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    //MainActivity mainActivity = (MainActivity) getActivity();
                    //set the notes live data variable,print,and send ticket to Firestore
                    viewModel.setOfficerNotes(editText_OfficerNotes.getText().toString());
                   // mainActivity.printText();
                    createTicket();

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
                offensesArray = viewModel.getArrOffenses().getValue();
                // use size() to get array size.
                String[] violations = offensesArray.toArray(new String[offensesArray.size()]);
                //String[] violations = new String[]{viewModel.getArrOffenses().getValue().toString()};
                //creates the violation array
                //String[] citations = new String[]{"Violation A", "Violation B", "Violation C"};
                //creates the checkboxes
                //boolean[] checkBoxes = new boolean[citations.length];
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
        String carModel = binding.fillVehicleModel.getText().toString();
        String carColor = binding.fillVehicleColor.getText().toString();

        String[] citationValues = binding.fillAddCitations.getText().toString().split(",");

        List<String> citations = Arrays.asList(citationValues);
        String carLicenseNumber = binding.fillTextPlateNumber.getText().toString();
        String citationTime = LocalDateTime.now().toString();
        String carParkingLot = binding.fillChooseLotSpinner.getSelectedItem().toString();
        String carState = binding.fillChooseTheStateSpinner.getSelectedItem().toString();
        String carMake = binding.fillVehicleMake.getText().toString();
        String officerNotes = binding.fillNotes.getText().toString();



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
        viewModel.setParkingLot(carParkingLot);
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
        //get latest ticketnum to increment it for new ticket from ticket collection
        generateTicketNum(data);
    }

    private void generateTicketNum(Map<String, Object> data) {
        db.collection("Tickets").orderBy("TicketNum", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Integer ticketNum = Integer.parseInt(documentSnapshot.get("TicketNum").toString());
                        Log.i("TicketNum", "Regular ticket num " + ticketNum);
                        Integer newTicketNum = ticketNum + 1;
                        //Log.i("new Ticket num", "new ticket num is" + newTicketNum);
                        // Log.i("TicketID in FillCItaiton", "VIEW MODEL TICKET ID IS " + viewModel.getTicketID().getValue());
                        data.put("TicketNum", newTicketNum);
                        viewModel.setTicketID(newTicketNum);
                        db.collection("Tickets").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Ticket Sent Successfully", Toast.LENGTH_LONG).show();
                                    MainActivity mainActivity = (MainActivity) getActivity();
                                    mainActivity.printText();
                                } else {
                                    Log.i("CONNECTION FAILED", task.getException().getMessage());
                                    DBConnectionFailed();
                                }
                            }
                        });
                    }

                } else {
                    DBConnectionFailed();
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
    private void autoFillCitationData() {
        Log.i("LIVE DATA FILL CITATION FRAG", "LICENSE NUM: " + viewModel.getLicenseNumber().getValue());
        Log.i("LIVE DATA FILL CITATION FRAG", "LICENSE STATE: " + viewModel.getLicenseState().getValue());

        //set the value fillTextPlateNumber box
        binding.fillTextPlateNumber.setText(viewModel.getLicenseNumber().getValue());
        //set the value of the chooseStateSpinner
        ArrayAdapter chooseStateAdapter = (ArrayAdapter) chooseStateSpinner.getAdapter();
        chooseStateSpinner.setSelection(chooseStateAdapter.getPosition(viewModel.getLicenseState().getValue()));
        //set the value of the chooseLotSpinner
        ArrayAdapter chooseLotAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, viewModel.getArrParkingLotList().getValue().toArray());
        //ArrayAdapter chooseLotAdapter = (ArrayAdapter) chooseLotSpinner.getAdapter();
        chooseLotSpinner.setAdapter(chooseLotAdapter);
        chooseLotSpinner.setSelection(chooseLotAdapter.getPosition(viewModel.getParkingLot().getValue()));
        //set the value fillVehicleModel box
        binding.fillVehicleModel.setText(viewModel.getVehicleModel().getValue());
        //set the value fillVehicleModel box
        binding.fillVehicleColor.setText(viewModel.getVehicleColor().getValue());
        //set balue for fillVehicleMake box
        binding.fillVehicleMake.setText(viewModel.getVehicleMake().getValue());


        //this is where the ERRROROROROROROROROROROROR is
        //binding.fillTicketNumber.setText(viewModel.getTicketID().getValue().toString());
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
        binding = FragmentFillCitationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}