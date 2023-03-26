package com.example.scanalot;


/**
 * This is a class of the variables that will be used to store values in 2D array from firebase.
 * It contains all offenses contained in firebase.
 *
 * @author Nick Downey
 * @Created 3/22/2023
 * @Contributor Nick Downey - Create Class Variables
 */
public class OffensesFirebase {
    //Offenses Values in Firebase.
    private String strFine;
    private String strOffenseType;

    //strFine getter and setter
    String getStrFine(){return strFine;}
    void setStrFine(String strFineParam){
        strFine = strFineParam;
    }

    //strOffenseType getter and setter
    String getStrOffenseType(){return strOffenseType;}
    void setStrOffenseType(String strOffenseTypeParam){
        strOffenseType = strOffenseTypeParam;
    }
}
