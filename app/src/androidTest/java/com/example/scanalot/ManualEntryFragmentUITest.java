package com.example.scanalot;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ManualEntryFragmentUITest {

    @Before
    public void setUp() {

    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testManualSearchButton() {
        // Check if button is on screen.
        onView(withId(R.id.outlinedButton)).check(matches(isDisplayed()));
        // Click the manual search button on the scan fragment
        onView(withId(R.id.outlinedButton)).perform(click());
        // Check that the manual entry plateSearch field is displayed
        onView(withId(R.id.plateSearch)).check(matches(isDisplayed()));
        // Type text into the manual entry plateSearch field
        onView(withId(R.id.plateSearch)).perform(typeText("ABC123"));
        // Select PA on the spinner.
        activityRule.getScenario();
        // Click spinner to focus
        onView(withId(R.id.stateSpinner)).perform(click());
        // select lot from spinner
        onData(allOf(is(instanceOf(String.class)), is("Pennsylvania")))
                .inRoot(isPlatformPopup())
                .perform(click());
        // Click the search button
        onView(withId(R.id.manualSearchButton)).perform(click());
        // Check that the search results are displayed
        //onView(withId(R.id.searchResultsTextView)).check(matches(isDisplayed()));
    }
}
