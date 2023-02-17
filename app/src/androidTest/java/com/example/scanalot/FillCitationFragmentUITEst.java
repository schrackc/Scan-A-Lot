package com.example.scanalot;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;

import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.AdapterView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FillCitationFragmentUITEst {

    private String strTicketNumberCorrect = "12348484";
    private String strTicketNumberFail = ".";

    private String strOfficerIDCorrect = "12348484";
    private String strOfficerIDFail = ".";


    private String strPlateNumberCorrect = "123456";
    private String strPlateNumberFail = ".";


    private String strChooseStateSpinnerCorrect = "Alabama";

    private String strChooseLotSpinner = "Lot A";

    private String strVehicleModelCorrect = "Jeep Cherokee";
    private String strVehicleModelFail = "1234";


    private String strVehicleColorCorrect = "Green";
    private String strVehicleColorFail = "1234";

    private String strFillAddCitationCorrect = "Citation A";
    private String strFillAddCitationCancel = "Cancel";
    private String strDialogTitle = "Select Violations";
    private String strDialogCancel = "CANCEL";
    private String strDialogOk = "OK";
    private String strFillNotesText = "Some Note Text";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);


    public void navigateToFillCitationPage() {
        //see if we are on the scan fragment in order to click the result button
        onView(withId(R.id.scan_fragment)).check(matches(isDisplayed()));
        //click button on main activity scan page
        onView(withId(R.id.ResultsScanButton)).perform(click());
        //check to see if fragment has changed to the result fragment
        onView(withText("Pass Expired - Wrong Lot")).check(matches(isDisplayed()));
        //Click the button tha will take you to fill Citation page
        onView(withId(R.id.fillSavePrintButton)).perform(click());
    }

    @Test
    public void testEnterCorrectFillCitationInfo() {
        navigateToFillCitationPage();
        testEditTicketNumberCorrect();
        testEditOfficerIDNumberCorrect();
        testChooseLotSpinnerCorrect();
        testEditPlateNumberCorrect();
        testChooseStateSpinnerCorrect();

        testEditVehicleModelCorrect();
        testEditVehicleColorCorrect();
        testFillAddCitationsCorrect();
        testEditFillNotes();
        testFillSavePrintButton();
    }

    @Test
    public void testEnterIncorrectFillCitationInfo() {
        navigateToFillCitationPage();
        testEditTicketNumberFail();
        testEditEditOfficerIDNumberFail();
        testChooseLotSpinnerNoSelect();
        testEditPlateNumberFail();
        testChooseStateSpinnerNoSelect();

        testEditVehicleModelFail();
        testEditVehicleColorFail();
        testFillAddCitationsFail();
        testEditFillNotes();
        testFillCancelButton();
    }
    public void testEditTicketNumberCorrect() {

        //add number  ticket id
        onView(withId(R.id.fillTicketNumber)).perform(typeText(strTicketNumberCorrect), closeSoftKeyboard());
        onView(withId(R.id.fillTicketNumber)).check(matches(withText(containsString(strTicketNumberCorrect))));
    }

    public void testEditTicketNumberFail() {

        //add number  ticket id
        onView(withId(R.id.fillTicketNumber)).perform(typeText(strTicketNumberFail), closeSoftKeyboard());
        onView(withId(R.id.fillTicketNumber)).check(matches(withText(not(containsString(strTicketNumberFail)))));

    }


    public void testEditOfficerIDNumberCorrect() {

        //add number  ticket id
        onView(withId(R.id.fillOfficerID)).perform(typeText(strOfficerIDCorrect), closeSoftKeyboard());
        onView(withId(R.id.fillOfficerID)).check(matches(withText(containsString(strOfficerIDCorrect))));

    }

    public void testEditEditOfficerIDNumberFail() {

        //add number  ticket id
        onView(withId(R.id.fillOfficerID)).perform(typeText(strOfficerIDFail), closeSoftKeyboard());
        onView(withId(R.id.fillOfficerID)).check(matches(withText(not(containsString(strOfficerIDFail)))));

    }


    public void testChooseLotSpinnerCorrect() {

        //click spinner
        onView(withId(R.id.fillChooseLotSpinner)).perform(click());
        //loads the dropdown into the view and bring it to focus
        onData(allOf(is(instanceOf(String.class)), is(containsString(strChooseLotSpinner)))).perform(click());
        //Check if text was selected
        // onView(withId(R.id.fillChooseLotSpinner)).check(matches(withText(containsString(strChooseLotSpinner))));
    }

    public void testChooseLotSpinnerNoSelect() {

        //click spinner
        onView(withId(R.id.fillChooseLotSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(containsString(strChooseLotSpinner)))).perform(pressBack());

    }


    public void testEditPlateNumberCorrect() {

        //add number  ticket id
        onView(withId(R.id.fillTextPlateNumber)).perform(typeText(strPlateNumberCorrect), closeSoftKeyboard());
        onView(withId(R.id.fillTextPlateNumber)).check(matches(withText(containsString(strPlateNumberCorrect))));

    }

    public void testEditPlateNumberFail() {

        //add number  ticket id
        onView(withId(R.id.fillTextPlateNumber)).perform(typeText(strPlateNumberFail), closeSoftKeyboard());
        onView(withId(R.id.fillTextPlateNumber)).check(matches(withText(not(containsString(strPlateNumberFail)))));

    }


    public void testChooseStateSpinnerCorrect() {
        onView(withId(R.id.fillChooseTheStateSpinner)).check(matches(isDisplayed()));
        //click spinner
        onView(withId(R.id.fillChooseTheStateSpinner)).perform(click());

        onData(allOf(is(instanceOf(String.class)), is(containsString(strChooseStateSpinnerCorrect)))).perform(click());

    }

    public void testChooseStateSpinnerNoSelect() {

        //click spinner
        onView(withId(R.id.fillChooseTheStateSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(containsString(strChooseStateSpinnerCorrect)))).perform(pressBack());
    }

    public void testEditVehicleModelCorrect() {


        onView(withId(R.id.fillVehicleModel)).perform(typeText(strVehicleModelCorrect), closeSoftKeyboard());
        onView(withId(R.id.fillVehicleModel)).check(matches(withText(containsString(strVehicleModelCorrect))));

    }

    public void testEditVehicleModelFail() {


        onView(withId(R.id.fillVehicleModel)).perform(typeText(strVehicleModelFail), closeSoftKeyboard());
        onView(withId(R.id.fillVehicleModel)).check(matches(withText(containsString(strVehicleModelFail))));

    }


    public void testEditVehicleColorCorrect() {


        onView(withId(R.id.fillVehicleColor)).perform(typeText(strVehicleColorCorrect), closeSoftKeyboard());
        onView(withId(R.id.fillVehicleColor)).check(matches(withText(containsString(strVehicleColorCorrect))));

    }

    public void testEditVehicleColorFail() {


        onView(withId(R.id.fillVehicleColor)).perform(typeText(strVehicleColorFail), closeSoftKeyboard());
        onView(withId(R.id.fillVehicleColor)).check(matches(withText(containsString(strVehicleColorFail))));

    }

    public void testFillAddCitationsCorrect() {

        //click spinner
        onView(withId(R.id.fillAddCitations)).perform(click());
        //loads the dropdown into the view and bring it to focus

        onView(withText(strDialogTitle)).inRoot(isDialog()).check(matches(isDisplayed()));

        onData(anything()).atPosition(0).perform(click());

        onView(withText(strDialogOk)).inRoot(isDialog()).perform(click());
    }

    public void testFillAddCitationsFail() {
        onView(withId(R.id.fillAddCitations)).perform(click());
        //loads the dropdown into the view and bring it to focus

        onView(withText(strDialogTitle)).inRoot(isDialog()).check(matches(isDisplayed()));

        onView(withText(strDialogCancel)).inRoot(isDialog()).perform(click());
    }

    public void testEditFillNotes() {

        onView(withId(R.id.fillNotes)).perform(typeText(strFillNotesText), closeSoftKeyboard());
        onView(withId(R.id.fillNotes)).check(matches(withText(containsString(strFillNotesText))));

    }


    public void testFillCancelButton() {

        //add number  ticket id
        onView(withId(R.id.fillCancelButton)).perform(click());

    }

    public void testFillSavePrintButton() {

        //add number  ticket id
        onView(withId(R.id.fillSavePrintButton)).perform(click());

    }


}
