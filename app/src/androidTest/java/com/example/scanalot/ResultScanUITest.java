package com.example.scanalot;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * This class is used for UI Testing on the Result Scan Fragment.
 * @author Andrew Hoffer
 * @Created 2/17/23
 * @Contributors Andrew Hoffer - 2/17/23 - Made Test
 */
@RunWith(AndroidJUnit4.class)
public class ResultScanUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testResultFragmentButton() {
        //see if we are on the scan fragment in order to click the result button
        onView(withId(R.id.scan_fragment)).check(matches(isDisplayed()));
        //click button on main activity scan page
        onView(withId(R.id.ResultsScanButton)).perform(click());
        //check to see if fragment has changed to the result fragment
        onView(withText("Pass Expired - Wrong Lot")).check(matches(isDisplayed()));
        //Click the button tha will take you to fill Citation page
        onView(withId(R.id.fillSavePrintButton)).perform(click());

    }
}
