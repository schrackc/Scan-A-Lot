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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;

import android.widget.AdapterView;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class is used for UI Testing on the Fill Citation Fragment.
 * @author Andrew Hoffer
 * @Created 2/17/23
 * @Contributors Andrew Hoffer - 2/17/23 - Made Test
 */
@RunWith(AndroidJUnit4.class)
public class FillCitationFragmentUITest {
    private String strTicketNumberCorrect = " ABC 1234";
    private String strTicketNumberFail = ".";
    private String strVehicleMakeCorrect = "Chevrolet";
    private String strVehicleMakeFail = ".";

    private String strPlateNumberCorrect = "123456";
    private String strPlateNumberFail = ".";

    private String strChooseStateSpinnerCorrect = "Alabama";
    private String strChooseLotSpinner = "Lot A";

    private String strVehicleModelCorrect = "Jeep Cherokee";
    private String strVehicleModelFail = "1234";

    private String strVehicleColorCorrect = "Green";
    private String strVehicleColorFail = "1234";

    private String strFillAddCitationCorrect = "Wrong Lot";

    private String strFillAddCitationCancel = "Cancel";
    private String strDialogTitle = "Select Violations";
    private String strDialogCancel = "CANCEL";
    private String strDialogOk = "OK";
    private String strFillNotesText = "Some Note Text";


    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testEnterCorrectFillCitationInfo() {
        navigateToFillCitationPage();
        testEditVehicleMakeCorrect();
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
    public void testAutoFillCitationInfoSuccess()
    {
        /*select lot*/
        onView(withId(R.id.select_lot_fragment)).perform(click());
        // Click spinner to focus
        onView(withId(R.id.selectLotSpinner)).perform(click());
        // select lot from spinner
        onData(Matchers.allOf(Matchers.is(instanceOf(String.class)), Matchers.is("Lot A")))
                .inRoot(isPlatformPopup())
                .perform(click());


        onView(withId(R.id.scan_fragment)).perform(click());
        onView(withId(R.id.scan_fragment)).check(matches(isDisplayed()));
        /*Go to Manual Entry*/
        onView(withId(R.id.outlinedButton)).check(matches(isDisplayed()));
        // Click the manual search button on the scan fragment
        onView(withId(R.id.outlinedButton)).perform(click());
        // Check that the manual entry plateSearch field is displayed
        onView(withId(R.id.plateSearch)).check(matches(isDisplayed()));
        // Type text into the manual entry plateSearch field
        onView(withId(R.id.plateSearch)).perform(typeText("ABC 1234"),closeSoftKeyboard());

        // Click the search button
        onView(withId(R.id.manualSearchButton)).perform(click());

        //click button on main activity scan page
        onView(withId(R.id.fillSavePrintButton)).perform(click());
        onView(withId(R.id.fillSavePrintButton)).perform(click());

    }


    public void testAutoFillCitationInfoFail()
    {
        /*select lot*/
        onView(withId(R.id.select_lot_fragment)).perform(click());
        // Click spinner to focus
        onView(withId(R.id.selectLotSpinner)).perform(click());
        // select lot from spinner
        onData(Matchers.allOf(Matchers.is(instanceOf(String.class)), Matchers.is("Lot A")))
                .inRoot(isPlatformPopup())
                .perform(click());
        
        onView(withId(R.id.scan_fragment)).perform(click());
        onView(withId(R.id.scan_fragment)).check(matches(isDisplayed()));
        /*Go to Manual Entry*/
        onView(withId(R.id.outlinedButton)).check(matches(isDisplayed()));
        // Click the manual search button on the scan fragment
        onView(withId(R.id.outlinedButton)).perform(click());
        // Check that the manual entry plateSearch field is displayed
        onView(withId(R.id.plateSearch)).check(matches(isDisplayed()));
        // Type text into the manual entry plateSearch field
        onView(withId(R.id.plateSearch)).perform(typeText("ABC 123"),closeSoftKeyboard());

        // Click the search button
        onView(withId(R.id.manualSearchButton)).perform(click());



        //click button on main activity scan page
        onView(withId(R.id.fillSavePrintButton)).perform(click());
        onView(withId(R.id.fillSavePrintButton)).perform(click());

    }

    /**
     * Responsible For navigating to the Fill Citation Page.
     */
    public void navigateToFillCitationPage() {

        onView(withId(R.id.scan_fragment)).check(matches(isDisplayed()));
        //click button on main activity scan page
        onView(withId(R.id.ResultsScanButton)).perform(click());
        //check to see if fragment has changed to the result fragment
        onView(withText("No Parking Lot Selected")).check(matches(isDisplayed()));
        //Click the button tha will take you to fill Citation page
        onView(withId(R.id.fillSavePrintButton)).perform(click());
    }

    @Test
    public void testEnterIncorrectFillCitationInfo() {
        navigateToFillCitationPage();
        testEditVehicleMakeFail();
        testChooseLotSpinnerNoSelect();
        testEditPlateNumberFail();
        testChooseStateSpinnerNoSelect();
        testEditVehicleModelFail();
        testEditVehicleColorFail();
        testFillAddCitationsFail();
        testEditFillNotes();
        testFillCancelButton();
    }

    /*The rest of these functions deal with each View on the Page. One will be the correct input test while the other is not.*/
    public void testEditVehicleMakeCorrect() {
        onView(withId(R.id.fillVehicleMake)).perform(typeText(strVehicleMakeCorrect), closeSoftKeyboard());
        onView(withId(R.id.fillVehicleMake)).check(matches(withText(containsString(strVehicleMakeCorrect))));
    }



    public void testEditVehicleMakeFail() {
        onView(withId(R.id.fillVehicleMake)).perform(typeText(strVehicleMakeFail), closeSoftKeyboard());
        onView(withId(R.id.fillVehicleMake)).check(matches(withText(containsString(strVehicleMakeFail))));
    }


    public void testChooseLotSpinnerCorrect() {
        //click spinner
        onView(withId(R.id.fillChooseLotSpinner)).perform(click());
        //loads the dropdown into the view and bring it to focus
        onData(allOf(is(instanceOf(String.class)), is(containsString(strChooseLotSpinner)))).perform(click());
    }

    public void testChooseLotSpinnerNoSelect() {
        //click spinner
        onView(withId(R.id.fillChooseLotSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(containsString(strChooseLotSpinner)))).perform(pressBack());
    }


    public void testEditPlateNumberCorrect() {
        onView(withId(R.id.fillTextPlateNumber)).perform(typeText(strPlateNumberCorrect), closeSoftKeyboard());
        onView(withId(R.id.fillTextPlateNumber)).check(matches(withText(containsString(strPlateNumberCorrect))));
    }

    public void testEditPlateNumberFail() {
        onView(withId(R.id.fillTextPlateNumber)).perform(typeText(strPlateNumberFail), closeSoftKeyboard());
        onView(withId(R.id.fillTextPlateNumber)).check(matches(withText(not(containsString(strPlateNumberFail)))));
    }


    public void testChooseStateSpinnerCorrect() {
        onView(withId(R.id.fillChooseTheStateSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.fillChooseTheStateSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(containsString(strChooseStateSpinnerCorrect)))).perform(click());
    }

    public void testChooseStateSpinnerNoSelect() {
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
        //loads the dropdown into the view and see if it is displayed
        onView(withText(strDialogTitle)).inRoot(isDialog()).check(matches(isDisplayed()));
        //click Violation A
        onData(anything()).atPosition(0).perform(click());
        //Click the OK button
        onView(withText(strDialogOk)).inRoot(isDialog()).perform(click());
    }

    public void testFillAddCitationsFail() {
        onView(withId(R.id.fillAddCitations)).perform(click());
        onView(withText(strDialogTitle)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(strDialogCancel)).inRoot(isDialog()).perform(click());
    }

    public void testEditFillNotes() {
        onView(withId(R.id.fillNotes)).perform(typeText(strFillNotesText), closeSoftKeyboard());
        onView(withId(R.id.fillNotes)).check(matches(withText(containsString(strFillNotesText))));
    }


    public void testFillCancelButton() {
        onView(withId(R.id.fillCancelButton)).perform(click());
    }

    public void testFillSavePrintButton() {
        onView(withId(R.id.fillSavePrintButton)).perform(click());
    }


}
