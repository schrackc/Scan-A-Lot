package com.example.scanalot;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Preview;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.example.scanalot.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * This class is used for the Main Activity. It creates the Main Activity and uses the activity_main layout. This will be used for
 * hosting all the fragments after the user logs in.
 *
 * @author Andrew Hoffer
 * @author Nick Downey
 * @Created 1/21/23
 * @Contributors Andrew Hoffer - 1/21/23 - Created the Activity and handlers
 * @Contributors Nick Downey - 1/30/23 - Added CameraX code for permissions and added a button
 * @Contributors Nick Downey - 2/23/23 - Added updating of location banner from SelectLotFragment spinner.
 * @Contributors Curtis Schrack - 3/8/23 - Add dynamic variables for license number and license plate and connect firestore
 * @Contributors Nick Downey - 3/13/2023 - Formatted Printer printed text with values from citation screen.
 * @Contributors Nick Downey - 3/18/2023 - Refactor Printer printed text with more values from citation screen.
 * @Contributors Nick Downey - 3/13/2023 - Formatted Printer printed text with values from citation screen.
 * @Contributors Nick Downey - 3/19/2023 - Added data pulling from firebase for parking lots.
 */
public class MainActivity extends AppCompatActivity implements SelectLotFragment.OnSpinnerSelectedListener {
    // CameraX code
    private static final String[] CAMERA_PERMISSION = new String[]{android.Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;


    //the bottom nav menu
    public BottomNavigationView bottomNavigationView;

    //Dynamic variables to show license information between screens
    public String strLicenseNumber;
    public String strLicenseState;

    //for every activity or fragment, there is a BindingClass that allows you to access the views in a easy fashion
    private ActivityMainBinding binding;

    //gets the action from the NavDirectionsObject
    private NavDirections navAction;

    // Preview shows a live image of what the camera feed sees.
    private Preview preview;

    // TextView that will be updated by a spinner on SelectLotFragment.
    private TextView locationBanner;

    /*Printer Variables*/
    //the permission list in which permissions are added/removed
    ArrayList<String> permissionsList;
    AlertDialog alertDialog;
    //permissions passed to launcher
    String[] permissionsStr = {
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.CAMERA
    };
   //number of permissions that still need approved by user
    int permissionsCount = 0;
    //declaration of printer
    EscPosPrinter printer = null;
    //Address of printer
    String strPrinterAddress = "57:4C:54:03:26:32";


    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //A list of all the vehicles in the database
    private ArrayList <VehicleCategories> arrVehicles = new ArrayList<>();

    // List of Parking lots in firebase
    private ArrayList<ParkingLots> arrParkingLots = new ArrayList<>();

    //View Model for passing data between fragments/parent Activities
    private TicketDataViewModel viewModel;
    BluetoothConnection bluetoothConnection = null;


    /**
     * Creates the Main Activity and sets the bottom navigation bar to the navigation controller. The navigation controller is the
     * nav_host_fragment located in content_main.xml. The nav controller is responsible for controlling/replacing fragments within
     * this activity.The nav controller(nav_host_fragment) is included in activity_main.xml in order to do this. The nav controller needs a
     * nav map in order to be told what fragment to replace the current fragment inside of it with next. That is why we
     * connect the nav map to the nav_host_fragment in nav_graph.xml.
     * NavigationUI is a object built just for setting up menus. We can easily link the bottomNavigationBar to the nav controller
     * using this. The only thing we must do is make sure that the nav menu ids in bottom_nav.xml match up with the ids in the nav graph. This
     * will link the menu items to the appropriate destination without us having to deal with popping the fragments off the stack and creating
     * a whole bunch of nav actions (the linkage arrows between the fragments) in nav_graph.xml.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Instantiate View Model for passing data between fragment and this activity
        viewModel = new ViewModelProvider(this).get(TicketDataViewModel.class);
        //creates an instance of Main Activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //sets the Content we want to see by getting the root view  (the parent to all views)
        setContentView(binding.getRoot());
        // gets the location banner.
        locationBanner = findViewById(R.id.geolocationBanner);
        //gets the bottom menu
        bottomNavigationView = binding.bottomNav;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        //gets the nav controller within the nav host fragment
        NavController navController = navHostFragment.getNavController();
        //binds the menu to the nav controller
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        // CameraX Code ------------------------------------------------------ //
        // 3 Methods required are: requestPermission, enableCamera, and hasCameraPermission
        // I the following requests permission when the activity that the camera is within is created.
        // Creating CameraPreview Permission Dialogue. Asks on create.



        /*Printer appending permissions to list*/
        permissionsList = new ArrayList<>();
        permissionsList.addAll(Arrays.asList(permissionsStr));
        //Ask for camera and printer permissions
        askForPermissions();
        ArrayList<String> parkingLots = new ArrayList<String>();
        ArrayList<String> offenses = new ArrayList<String>();

        // Documentation for the following Document pulling.
        // https://firebase.google.com/docs/firestore/query-data/get-data?hl=en&authuser=2#java
        db.collection("Offenses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //add to the array list
                                offenses.add(document.get("OffenseType").toString());
                                Log.d("Offenses", document.getId() + " => " + document.getData());
                                Log.d("Offenses", offenses.toString());
                            }
                            //set the offenses arraylist
                            viewModel.setArrOffenses(offenses);
                        } else {
                            Log.d("Offenses", "Error getting offense documents: ", task.getException());
                        }
                    }
                });


        // Documentation for the following Document pulling.
        // https://firebase.google.com/docs/firestore/query-data/get-data?hl=en&authuser=2#java
        db.collection("ParkingLots")
                .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.i("PARKING LOT IN MAIN", document.get("LotName").toString());
                                        //add to the array list
                                        parkingLots.add(document.get("LotName").toString());
                                        Log.d("ParkingLots", document.getId() + " => " + document.getData());
                                    }
                                    //set the parking lot arraylist
                                    viewModel.setArrParkingLots(parkingLots);
                                } else {
                                    Log.d("ParkingLots", "Error getting parking documents: ", task.getException());
                                }
                            }
                        });

        //set parking lot array viewModel to the array of data retrieved from the firebase
      //  viewModel.setArrParkingLots((arrParkingLots));

        // Gets firebase Vehicles collection and adds all the records to the dbVehicles variable
        db.collection("Vehicles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int iRowValue = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Add values to 2d Array List
                                arrVehicles.add(new VehicleCategories());
                                arrVehicles.get(iRowValue).setOwnerName(document.getString("OwnerFirstName") + " " + document.getString("OwnerLastName"));
                                arrVehicles.get(iRowValue).setMake(document.getString("Make"));
                                arrVehicles.get(iRowValue).setModel(document.getString("Model"));
                                arrVehicles.get(iRowValue).setColor(document.getString("Color"));
                                arrVehicles.get(iRowValue).setLicNum(document.getString("LicenseNum"));
                                arrVehicles.get(iRowValue).setLicState(document.getString("LicenseState"));
                                arrVehicles.get(iRowValue).setAuthParkingLot((ArrayList<String>) document.get("ParkingLot"));
                                Log.d("GotDoc", document.getId() + " => " + document.getData());
                                iRowValue++;
                            }
                        } else {
                            Log.d("NoDoc", "Error getting documents: ", task.getException());
                        }
                    }
                });

        //set the vehicle array in view model to the array of data retrieved from firebase
        viewModel.setVehicleList(arrVehicles);
    }// end of onCreate()

    // End of CameraX -------------------------------------------------- //

    /**
     * Method that handles updating the textView geolocationBanner. It pulls from the selectLot spinner
     * on the SelectLotFragment.
     *
     * @param item
     */
    @Override
    public void onSpinnerSelected(String item) {
        // setting text with whatever is selected in the select lot fragment.
        String location = "Location: " + item;
        locationBanner.setText(location);
    }

    /**
     * Inflates the menu; this adds items to the action bar if it is present.
     *
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*Printer Code------------------------------------------------*/

    /*Receives permission request results for printer*/
    ActivityResultLauncher<String[]> permissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {
                            //get the result values from all the permissions being requested
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            permissionsList = new ArrayList<>();
                            permissionsCount = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (shouldShowRequestPermissionRationale(permissionsStr[i])) {
                                    permissionsList.add(permissionsStr[i]);
                                } else if (!hasPermission(getApplicationContext(), permissionsStr[i])) {
                                    permissionsCount++;
                                }
                            }
                            if (permissionsList.size() > 0) {
                                //Some permissions are denied and can be asked again.
                                askForPermissions();
                            } else if (permissionsCount > 0) {
                                //Show alert dialog
                                showPermissionDialog();
                            } else {
                                //on successfully accepting all permissions
                            }
                        }
                    });


    /**
     * Creates a printer instance from the Bluetooth paired Device List that is a Thermal Printer
     */
    public void connectToPrinter() {

            bluetoothConnection = getBluetoothConnection(strPrinterAddress);

                if(printer==null) {
                    try {
                        printer = new EscPosPrinter(bluetoothConnection, 203, 48f, 32);
                    } catch (EscPosConnectionException e) {
                        printerNotFound();
                    }
                }
    }


    /**
     * Used to output message when printer has failed to connect.
     */
    public void printerConnectionFailed() {
        Toast.makeText(this, "Printer Failed To Connect.", Toast.LENGTH_LONG).show();
    }

    /**
     * Used to output a message when the thermal printer is not found in the Bluetooth paired list
     */
    private void printerNotFound() {
        Toast.makeText(this, "Printer Not Found. Please Pair Printer.", Toast.LENGTH_LONG).show();
    }

    /**
     * Looks through the paired devices list and searches for a specific device address and returns the
     * Bluetooth connection object with that address
     * @param{String} printerAddress
     * @return null or BluetoothConnection
     */

    private BluetoothConnection getBluetoothConnection(String printerAddress) {
        BluetoothConnection bluetoothConnection = null;
        BluetoothConnection[] connections = new BluetoothPrintersConnections().getList();
        if (connections != null) {
            for (int connectionCount = 0; connectionCount < connections.length; connectionCount++) {
                //output the addresses
                Log.i("BLUETOOTH DEVICE", connections[connectionCount].getDevice().getAddress());
                //get the correct address if in list of paired
                if (connections[connectionCount].getDevice().getAddress().contains(printerAddress)) {
                    bluetoothConnection = connections[connectionCount];
                }
            }
        }
        return bluetoothConnection;
    }

    /**
     * Checks to see if the permission was granted or not
     */
    private boolean hasPermission(Context context, String permissionStr) {
        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests Bluetooth permissions in order to connect to the printer
     */
    public void askForPermissions() {

        String[] newPermissionStr = new String[permissionsList.size()];
        for (int i = 0; i < newPermissionStr.length; i++) {
            //creates the array of permissions to be passed to launcher
            newPermissionStr[i] = permissionsList.get(i);
        }
        //request permissions if the string is not empty
        if (newPermissionStr.length > 0) {
            permissionsLauncher.launch(newPermissionStr);
        } else {
        /* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
        which will lead them to app details page to enable permissions from there. */
            showPermissionDialog();
        }
    }

    /**
     * If user denies permission, this will be shown to the user as the rationale for why the permissions
     * are needed.
     */
    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission required")
                .setMessage("Some permissions are need to be allowed to use this app without any problems.")
                .setPositiveButton("Settings", (dialog, which) -> {
                    dialog.dismiss();
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

    /**
     * Used to properly disconnect the printer
     */
    public void printDisconnect() {
        if (printer != null) {
            printer.disconnectPrinter();
        }
    }

    /**
     *Command the thermal printer to print the given text for the ticket.
     */
    LocalDate currentDate = LocalDate.now();
    String date = String.format("%02d/%02d/%04d", currentDate.getMonthValue(), currentDate.getDayOfMonth(), currentDate.getYear());
    public void printText()  {
        if(printer!=null) {
            try {
                printer.printFormattedText(
                        // Change the fine amount and violations to accept correct values.
                        // Currently cannot get violations because there is no live data.
                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.printed_logo, DisplayMetrics.DENSITY_XXXHIGH )) + "</img>\n" +
                        "\n\n\n" +
                        "[C]<font color='bg-black'>---TICKET INFORMATION---</font>" +"\n" +
                        "[L]TicketID:\n" + "[R]" + ticketIDPrinting() +"\n" +
                        "[L]Violation Date:\n" +"[R]" + date + "\n" +
                        "[L]Officer:\n" + "[R]" + reportingOfficerPrinting() +"\n" +
                        "[L]Parking Lot:\n" + "[R]" + citationLocationPrinting() +"\n" +
                        "[L]Violation:\n" + "[R]" + violationTypePrinting() +"\n" +
                        "[L]Fine Amount:\n" + "[R]" + " $" + calculateTotalFine() +"\n" +
                        "[C]<font color='bg-black'>---VEHICLE INFORMATION---</font>" +"\n" +
                        "[L]License:\n" + "[R]" + licensePlatePrinting() +"\n" +
                        "[L]State:\n" + "[R]" + vehicleStatePrinting() +"\n" +
                        "[L]Car Model:\n" + "[R]" + vehicleModelPrinting() +"\n" +
                        "[L]Car Color:\n" + "[R]" + vehicleColorPrinting()
                );
            } catch (Exception e) {
                printerConnectionFailed();
            }

        }else
        {
            connectToPrinter();
        }
    }

    /**
     * Function to calculate the total fine amount for a ticket.
     * It takes in an array from the arrFineAmount view model that contains a set of string fine amounts, corresponding to the selected fines on the FillCitationFragment.
     * The function then iterates through the String array, while removing the first character ($,€,£).
     * It then parses integers from the remaining string and adds them up in one variable that is returned.
     * Note: The printed statement on the ticket will only have USD signs.
     */
    public String calculateTotalFine(){
        ArrayList<String> fineAmountTotalArray = new ArrayList<>();
        if (viewModel.getArrFineAmount().getValue() == null){fineAmountTotalArray.add("$0");}
        else {fineAmountTotalArray = viewModel.getArrFineAmount().getValue();}
        int totalFineAmount = 0;
        for (String fineTotal : fineAmountTotalArray) {
            int totalAmount = Integer.parseInt(fineTotal.substring(1));
            totalFineAmount += totalAmount;
        }
        String totalFineAmountString = Integer.toString(totalFineAmount);
        return totalFineAmountString;
    }

    /**********************
     The following are a series of functions that read their respective view models and print
     a meaningful message to the user if they are detected as null or not.
     **********************/
    public String ticketIDPrinting()
    {
        if (viewModel.getTicketID().getValue() == null)
        {
            return "ID Not Found";
        }
        else {
            return viewModel.getTicketID().getValue().toString();
        }
    }

    public String reportingOfficerPrinting()
    {
        if (viewModel.getOfficerID().getValue() == null)
        {
            return "ID Not Found";
        }
        else {
            return viewModel.getOfficerID().getValue();
        }
    }

    public String citationLocationPrinting()
    {
        if (viewModel.getParkingLot().getValue() == null)
        {
            return "Lot Not Found";
        }
        else {
            return viewModel.getParkingLot().getValue();
        }
    }

    public String violationTypePrinting()
    {
        if (viewModel.getArrSelectedOffenses().getValue() == null)
        {
            return "Offenses Not Found";
        }
        else {
            return viewModel.getArrSelectedOffenses().getValue().toString();
        }
    }

    public String licensePlatePrinting()
    {
        if (viewModel.getLicenseNumber().getValue() == null)
        {
            return "Plate Number Not Found";
        }
        else {
            return viewModel.getLicenseNumber().getValue();
        }
    }

    public String vehicleColorPrinting()
    {
        if (viewModel.getVehicleColor().getValue() == null)
        {
            return "Color Not Found";
        }
        else {
            return viewModel.getVehicleColor().getValue();
        }
    }

    public String vehicleModelPrinting()
    {
        if (viewModel.getVehicleModel().getValue() == null)
        {
            return "Model Not Found";
        }
        else {
            return viewModel.getVehicleModel().getValue();
        }
    }

    public String vehicleMakePrinting()
    {
        if (viewModel.getVehicleMake().getValue() == null)
        {
            return "Make Not Found";
        }
        else {
            return viewModel.getVehicleMake().getValue();
        }
    }

    public String vehicleStatePrinting()
    {
        if (viewModel.getLicenseState().getValue() == null)
        {
            return "State Not Found";
        }
        else {
            return viewModel.getLicenseState().getValue();
        }
    }
}


