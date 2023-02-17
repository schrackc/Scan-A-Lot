package com.example.scanalot;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityUITest {

    // ActivityTestRule is deprecated. We should use ActivityScenario instead.
    // https://developer.android.com/reference/androidx/test/core/app/ActivityScenario
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginCorrectCredentials() {
        // UI testing the login page.
        // Launch Activity.

// Enter valid username.
        onView(withId(R.id.username)).perform(typeText("2@2.edu"), closeSoftKeyboard());
        // Enter valid password.
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        // CLick login button.
        onView(withId(R.id.login)).perform(click());
        // Check if success leads to MainActivity.
        onView(withId(R.id.container)).check(matches(isDisplayed()));

    }

    // Not working.
    @Test
    public void testLoginIncorrectCredentials() {
        // UI testing the Login Page.
        // Launch Activity.
        activityRule.getScenario();
        // Entering invalid uname.
        onView(withId(R.id.username)).perform(typeText("2@2.edu"), closeSoftKeyboard());
        // Entering invalid password.
        onView(withId(R.id.password)).perform(typeText("loremIpsum123"), closeSoftKeyboard());
        // Click login button.
        onView(withId(R.id.login)).perform(click());
        //check to see if login is still displayed by checking username is displayed due to fail
        onView(withId(R.id.username)).check(matches(isDisplayed()));
    }


}


