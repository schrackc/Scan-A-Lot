package com.example.scanalot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class TicketDataViewModel extends ViewModel {
    /*private*/
    private final MutableLiveData<Integer> iTicketID = new MutableLiveData<Integer>();
    private final MutableLiveData<String> strOfficerNotes = new MutableLiveData<String>();
    private final MutableLiveData<String> strOfficerID = new MutableLiveData<String>();
    private final MutableLiveData<String> strParkingLot = new MutableLiveData<String>();
    private final MutableLiveData<String> strLicenseNumber = new MutableLiveData<String>();
    private final MutableLiveData<String> strLicenseState = new MutableLiveData<String>();
    private final MutableLiveData<String> strVehicleModel = new MutableLiveData<String>();
    private final MutableLiveData<String> strVehicleMake = new MutableLiveData<String>();
    private final MutableLiveData<String> strVehicleColor = new MutableLiveData<String>();
    private final MutableLiveData<ArrayList<VehicleCategories>> arrVehicles = new MutableLiveData<ArrayList<VehicleCategories>>();
    private final MutableLiveData<ArrayList<String>> arrParkingLots = new MutableLiveData<ArrayList<String>>();
    private final MutableLiveData<ArrayList<String>> arrOffenses = new MutableLiveData<ArrayList<String>>();
    private final MutableLiveData<ArrayList<String>> arrSelectedOffenses = new MutableLiveData<ArrayList<String>>();
    private final MutableLiveData<ArrayList<String>> arrFineAmount = new MutableLiveData<ArrayList<String>>();

    private final MutableLiveData<String> strEditTicketDocumentID= new MutableLiveData<String>();
    private final MutableLiveData<String> strEditTicketParkingLot= new MutableLiveData<String>();

    /*Public */

    /* License Plate Number Getters/Setters */
    public void setLicenseNumber(String p_LicenseNum)
    {
        strLicenseNumber.setValue(p_LicenseNum);
    }

    public LiveData<String> getLicenseNumber()
        {
            return strLicenseNumber;
        }


     /* License Plate State Getters/Setters */
    public void setLicenseState(String p_LicenseState)
    {
        strLicenseState.setValue(p_LicenseState);
    }

    public LiveData<String> getLicenseState()
    {
        return strLicenseState;
    }

    /*Array License Plated Vehicles ArrayList Getters/Setters*/
    public void setVehicleList( ArrayList<VehicleCategories> arr)
    {
        arrVehicles.setValue(arr);
    }
    public LiveData<ArrayList<VehicleCategories>> getVehicleList()
    {
        return  arrVehicles;
    }


    /*Ticket ID Setter/Getter*/
    public void setTicketID(Integer p_TicketID)
    {
        iTicketID.setValue(p_TicketID);
    }

    public LiveData<Integer> getTicketID()
    {
        return iTicketID;
    }


    /*Officer ID Setter/Getter*/
    public void setOfficerID(String p_OfficerID)
    {
        strOfficerID.setValue(p_OfficerID);
    }

    public LiveData<String> getOfficerID()
    {
        return strOfficerID;
    }

    /*Officer Notes Setter/Getter*/
    public void setOfficerNotes(String p_Notes)
    {
        strOfficerNotes.setValue(p_Notes);
    }

    public LiveData<String> getOfficerNotes()
    {
        return strOfficerNotes;
    }

    /*Parking Lot Setter/Getter*/
    public void setParkingLot(String p_ParkingLot)
    {
        strParkingLot.setValue(p_ParkingLot);
    }

    public LiveData<String> getParkingLot()
    {
        return strParkingLot;
    }

    /*Vehicle Model Setter/Getter*/
    public void setVehicleModel(String p_VehicleModel)
    {
        strVehicleModel.setValue(p_VehicleModel);
    }
    public LiveData<String> getVehicleModel()
    {
        return strVehicleModel;
    }

    /*Vehicle Make Setter/Getter*/
    public void setVehicleMake(String p_VehicleMake)
    {
        strVehicleMake.setValue(p_VehicleMake);
    }
    public LiveData<String> getVehicleMake()
    {
        return strVehicleMake;
    }

    /*Vehicle Color Setter/Getter*/
    public void setVehicleColor(String p_VehicleColor)
    {
        strVehicleColor.setValue(p_VehicleColor);
    }
    public LiveData<String> getVehicleColor()
    {
        return strVehicleColor;
    }

    /*Array Parking Lots Getters/Setters*/
    public void setArrParkingLots(ArrayList<String> arr) {arrParkingLots.setValue(arr);}
    public LiveData<ArrayList<String>> getArrParkingLotList()
    {
        return arrParkingLots;
    }

    /*Array Offenses Getters/Setters*/
    public void setArrOffenses(ArrayList<String> arr) {arrOffenses.setValue(arr);}
    public LiveData<ArrayList<String>> getArrOffenses(){return arrOffenses;}

    /*Array Selected Offenses Getters/Setters*/
    public void setArrSelectedOffenses(ArrayList<String> arr) {arrSelectedOffenses.setValue(arr);}
    public LiveData<ArrayList<String>> getArrSelectedOffenses(){return arrSelectedOffenses;}

    /*Array Fine Amount Getters/Setters*/
    public void setArrFineAmount(ArrayList<String> arr){arrFineAmount.setValue(arr);}
    public LiveData<ArrayList<String>> getArrFineAmount(){return arrFineAmount;}

    /*Edit Ticket Document ID Getters/Setters*/
    public void setEditTicketDocumentID(String p_DocumentID)
    {
        strEditTicketDocumentID.setValue(p_DocumentID);
    }
    public LiveData<String> getEditTicketDocumentID()
    {
        return strEditTicketDocumentID;
    }

    public void setEditTicketParkingLot(String p_ParkingLot)
    {
        strEditTicketParkingLot.setValue(p_ParkingLot);
    }
    public LiveData<String> getEditTicketParkingLot()
    {
        return strEditTicketParkingLot;
    }

}