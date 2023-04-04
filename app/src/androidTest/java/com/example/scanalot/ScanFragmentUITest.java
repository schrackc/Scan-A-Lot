package com.example.scanalot;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.widget.Button;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ScanFragmentUITest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

@Test
public void scanTicketSuccess() throws InterruptedException {
/*Scan a ticket*/
//see if we are on the scan fragment
onView(withId(R.id.scan_fragment)).check(matches(isDisplayed()));
//wait for a scan of a license plate
Thread.sleep(6000);
//check to see if we are still on the scan page by seeing if there is a view. If we cant find the view we have done a success
onView(withId(R.id.outlinedButton)).check(doesNotExist());
}



    @Test
    public void scanTicketFail() throws InterruptedException {
        /*Scan a ticket*/
//see if we are on the scan fragment
        onView(withId(R.id.scan_fragment)).check(matches(isDisplayed()));
//wait for a scan of a license plate
        Thread.sleep(6000);
//check to see if we are still on the scan page by seeing if there is a view. If its there its a fail
        onView(withId(R.id.outlinedButton)).check(matches(isDisplayed()));
    }









}
