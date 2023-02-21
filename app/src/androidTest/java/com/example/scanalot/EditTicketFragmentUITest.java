package com.example.scanalot;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EditTicketFragmentUITest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testEditTicketControls() {
        // Launch the SelectLotFragment by clicking on its menu item
        onView(withId(R.id.edit_ticket_fragment)).perform(click());
        // Check that the select lot button is displayed
        onView(withId(R.id.editTicketSearchButton)).check(matches(isDisplayed()));
        activityRule.getScenario();
        // type text in box
        onView(withId(R.id.editTicketSearchBox)).perform(typeText("1111"), closeSoftKeyboard());
        // Click button,
        onView(withId(R.id.editTicketSearchButton)).perform(click());
    }
}