package com.example.scanalot;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import androidx.fragment.app.Fragment;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class ResultBannerUnitTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void vehicleCorrectLot() throws InterruptedException {
        //Go to parkinglot change lot location
        onView(withId(R.id.select_lot_fragment)).perform(click());
        // Click spinner to focus
        onView(withId(R.id.selectLotSpinner)).perform(click());
        // select lot from spinner
        onData(allOf(is(instanceOf(String.class)), is("Lot A")))
                .inRoot(isPlatformPopup())
                .perform(click());
        //Go back to Scan screen
        onView(withId(R.id.scan_fragment)).perform((click()));
        // Launch the ManualSearch by clicking on its menu item
        onView(withId(R.id.outlinedButton)).perform(click());
        // type valid license plate
        onView(withId(R.id.plateSearch)).perform(typeText("ABC 1234"), closeSoftKeyboard());
        TimeUnit.SECONDS.sleep(2);
        // Click search button
        onView(withId(R.id.manualSearchButton)).perform(click());
        TimeUnit.SECONDS.sleep(2);
    }
    @Test
    public void vehicleWrongLot() throws InterruptedException {
        //Go to parkinglot change lot location
        onView(withId(R.id.select_lot_fragment)).perform(click());
        // Click spinner to focus
        onView(withId(R.id.selectLotSpinner)).perform(click());
        // select lot from spinner
        onData(allOf(is(instanceOf(String.class)), is("Lot B")))
                .inRoot(isPlatformPopup())
                .perform(click());
        //Go back to Scan screen
        onView(withId(R.id.scan_fragment)).perform((click()));
        // Launch the ManualSearch by clicking on its menu item
        onView(withId(R.id.outlinedButton)).perform(click());
        // type valid license plate
        onView(withId(R.id.plateSearch)).perform(typeText("ABC 1234"), closeSoftKeyboard());
        TimeUnit.SECONDS.sleep(2);
        // Click search button
        onView(withId(R.id.manualSearchButton)).perform(click());
        TimeUnit.SECONDS.sleep(2);
    }
    @Test
    public void vehicleNoDatabase() throws InterruptedException {
        //Go to parkinglot change lot location
        onView(withId(R.id.select_lot_fragment)).perform(click());
        // Click spinner to focus
        onView(withId(R.id.selectLotSpinner)).perform(click());
        // select lot from spinner
        onData(allOf(is(instanceOf(String.class)), is("Lot A")))
                .inRoot(isPlatformPopup())
                .perform(click());
        //Go back to Scan screen
        onView(withId(R.id.scan_fragment)).perform((click()));
        // Launch the ManualSearch by clicking on its menu item
        onView(withId(R.id.outlinedButton)).perform(click());
        // type valid license plate
        onView(withId(R.id.plateSearch)).perform(typeText("NO PLATE"), closeSoftKeyboard());
        TimeUnit.SECONDS.sleep(2);
        // Click search button
        onView(withId(R.id.manualSearchButton)).perform(click());
        TimeUnit.SECONDS.sleep(2);
    }
    @Test
    public void noParkingLot() throws InterruptedException {
        // Launch the ManualSearch by clicking on its menu item
        onView(withId(R.id.outlinedButton)).perform(click());
        // type valid license plate
        onView(withId(R.id.plateSearch)).perform(typeText("ABC 1234"), closeSoftKeyboard());
        TimeUnit.SECONDS.sleep(2);
        // Click search button
        onView(withId(R.id.manualSearchButton)).perform(click());
        TimeUnit.SECONDS.sleep(2);
    }
}
