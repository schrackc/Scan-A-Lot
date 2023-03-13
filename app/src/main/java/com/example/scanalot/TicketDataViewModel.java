package com.example.scanalot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class TicketDataViewModel extends ViewModel {
    /*private*/
    private final MutableLiveData<String> strTicketID = new MutableLiveData<String>();
    private final MutableLiveData<String> strOfficerID = new MutableLiveData<String>();
    private final MutableLiveData<String> strParkingLot = new MutableLiveData<String>();
    private final MutableLiveData<String> strLicenseNumber = new MutableLiveData<String>();
    private final MutableLiveData<String> strLicenseState = new MutableLiveData<String>();
    private final MutableLiveData<String> strLicenseVehicleModel = new MutableLiveData<String>();
    private final MutableLiveData<String> strLicenseVehicleColor = new MutableLiveData<String>();
    private final MutableLiveData<ArrayList<VehicleCategories>> arrVehicles = new MutableLiveData<ArrayList<VehicleCategories>>();
    private  final MutableLiveData<Integer> iRowReference = new MutableLiveData<>();

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
    public void setTicketID(String p_TicketID)
    {
        strTicketID.setValue(p_TicketID);
    }

    public LiveData<String> getTicketID()
    {
        return strTicketID;
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
    public void setLicenseVehicleModel(String p_VehicleModel)
    {
        strLicenseVehicleModel.setValue(p_VehicleModel);
    }
    public LiveData<String> getLicenseVehicleModel()
    {
        return strLicenseVehicleModel;
    }

    /*Vehicle Color Setter/Getter*/
    public void setStrLicenseVehicleColor(String p_VehicleColor)
    {
        strLicenseVehicleColor.setValue(p_VehicleColor);
    }
    public LiveData<String> getLicenseVehicleColor()
    {
        return strLicenseVehicleColor;
    }

    /* Reference Number Getters/Setters */
    public void setReferenceNum(Integer ref)
    {
        iRowReference.setValue(ref);
    }

    public int getReferenceNum()
    {
        return iRowReference.getValue().intValue();
    }
}
