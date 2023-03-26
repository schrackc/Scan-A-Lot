package com.example.scanalot;

import java.util.ArrayList;

/**
 * This is a class of the variables that will be used to store values in 2D array from firebase.
 * It contains all parking lots contained in firebase.
 *
 * @author Nick Downey
 * @Created 3/19/2023
 * @Contributor Nick Downey - Create Class Variables
 */

public class ParkingLots {
    // Parking Lot values in Firebase.

    private ArrayList<String> arrParkingLot;

    private String strLotName;

    private String strMaxLatitude;

    private String strMinLatitude;

    private String strMaxLongitude;

    private String strMinLongitude;


    // arrParkingLot getter and setter
   ArrayList<String> getArrParkingLot(){return arrParkingLot;}
   void setArrParkingLot(ArrayList<String> arrParkingLotList){
       arrParkingLot = arrParkingLotList;
   }

   // strLotName field getter and setter
    String getStrLotName(){return strLotName;}
    void setStrLotName(String lotName){
       strLotName = lotName;
    }


}
