package com.example.scanalot;

import java.util.ArrayList;

/**
 * This is a class of the variables that will be used to store values in 2D array from firebase
 *
 * @author Curtis Schrack
 * @Created 3/12/2023
 * @Contributor Curtis Schrack - Create Class Variables
 * @Contributors Nick Downey - 03/25/23 - Added Violations from Firebase.
 */
public class VehicleCategories {
    //Vehicle Firebase value Categories
    private String strOwnerName;
    private String strCarMake;
    private String strCarModel;
    private String strCarColor;
    private String strLicenseNumber;
    private String strLicenseState;
    private ArrayList<String> arrParkingLot;

    //OwnerName getter and setter
    String getOwnerName(){ return strOwnerName;}
    void setOwnerName(String owner){
        strOwnerName = owner;
    }

    //Car Make getter and setter
    String getMake(){ return strCarMake;}
    void setMake(String make){
        strCarMake = make;
        return;
    }

    //Car Model getter and setter
    String getModel(){ return strCarModel;}
    void setModel(String model){
        strCarModel = model;
        return;
    }

    //Car Color getter and setter
    String getColor(){ return strCarColor;}
    void setColor(String color){
        strCarColor = color;
        return;
    }

    //Car License Number getter and setter
    String getLicNum(){ return strLicenseNumber;}
    void setLicNum(String LicNum){
        strLicenseNumber = LicNum;
        return;
    }

    //Car State getter and setter
    String getLicState(){ return strLicenseState;}
    void setLicState(String LicState){
        strLicenseState = LicState;
        return;
    }

    //Allowed ParkingLot getter and setter
    ArrayList<String> getAuthParkingLot(){ return arrParkingLot;}
    void setAuthParkingLot(ArrayList<String> AuthParkingLot) {
        arrParkingLot = AuthParkingLot;
        return;
    }
}
