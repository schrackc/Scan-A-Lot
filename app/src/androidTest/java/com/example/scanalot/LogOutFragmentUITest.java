package com.example.scanalot;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class LogOutFragmentUITest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void verifyLogout() {
        // Launch the log_out_fragment by clicking on its menu item
        onView(withId(R.id.log_out_fragment)).perform(click());
        // Check that the Log Out button is displayed
        onView(withId(R.id.logOutButton)).check(matches(isDisplayed()));
        // Click LogoutButton
        onView(withId(R.id.logOutButton)).perform(click());
        // Verify Logout by checking for elements on login screen.
        activityRule.getScenario();
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.login)).check(matches(isDisplayed()));
    }
}
