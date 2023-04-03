package com.example.scanalot;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class PrintPreviewUITest {
        @Rule
        public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

        @Test
        public void testPrintPreviewFragment() throws InterruptedException {
            // Delay lets app load. It may not catch the navbar is the delay is not here.
            TimeUnit.SECONDS.sleep(1);
            // Launch the SelectLotFragment by clicking on its menu item
            onView(withId(R.id.select_lot_fragment)).perform(click());
            // Click spinner to focus
            onView(withId(R.id.selectLotSpinner)).perform(click());
            // select lot from spinner
            onData(allOf(is(instanceOf(String.class)), is("Lot A")))
                    .inRoot(isPlatformPopup())
                    .perform(click());

            onView(withId(R.id.scan_fragment)).perform(click());
            onView(withId(R.id.outlinedButton)).perform(click());
            onView(withId(R.id.plateSearch)).perform(typeText("ABC 1234"),closeSoftKeyboard());
            onView(withId(R.id.manualSearchButton)).perform(click());
            onView(withId(R.id.fillSavePrintButton)).perform(click());
            // select violations in this time. Cannot test selecting the checkbox array.
            TimeUnit.SECONDS.sleep(5);
            onView(withId(R.id.fillSavePrintButton)).perform(click());
            // The following is code that should select from our violations checkbox list.
            // This is not possible as far as I know due to it being a textview and supplied data by firestore.
//            onView(withId(R.id.fillAddCitations)).perform(scrollTo()).perform(click());
//            // Click on the first checkbox option
//            onView(withText("Wrong Lot"))
//                    .perform(scrollTo())
//                    .perform(click());
//            // Click on the first checkbox option
//            onView(withText("No Pass"))
//                    .perform(scrollTo())
//                    .perform(click());
//            onView(withText("OK")).perform(click());

            // Check for the correct data to be pulled from firestore.
            // The data is not static so the test requires human verification.
            onView(withId(R.id.textViewTicketID)).check(matches(isDisplayed()));
            onView(withId(R.id.textViewViolationTime)).check(matches(isDisplayed()));
            onView(withId(R.id.TextViewReportingOfficer)).check(matches(isDisplayed()));
            onView(withId(R.id.textViewCitationLocation)).check(matches(isDisplayed()));
            onView(withId(R.id.textViewViolationType)).check(matches(isDisplayed()));
            onView(withId(R.id.textViewFineAmount)).check(matches(isDisplayed()));
            onView(withId(R.id.textViewLicensePlate)).check(matches(isDisplayed()));
            onView(withId(R.id.textViewVehicleColor)).check(matches(isDisplayed()));
            onView(withId(R.id.textViewVehicleModel)).check(matches(isDisplayed()));
        }



}
