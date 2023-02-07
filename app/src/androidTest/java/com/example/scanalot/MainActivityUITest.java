package com.example.scanalot;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.widget.Button;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityUITest{

    private MainActivity activity;
    private BottomNavigationView bottomNav;
    private Button enableCamera;

    public MainActivityUITest() {
        //super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        //super.setUp();
        activity = activityRule.getActivity();
        bottomNav = activity.findViewById(R.id.bottom_nav);
        enableCamera = activity.findViewById(R.id.enableCamera);
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testCameraButton(){
        // launch Activity
        activityRule.launchActivity(new Intent());
        // Check if button is on screen.
        onView(withId(R.id.enableCamera)).check(matches(isDisplayed()));
        // Click button.
        onView(withId(R.id.enableCamera)).perform(click());
        // Check if cameraActivity is launched.
        onView(withId(R.id.cameraActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void testManualEntryButton(){
        // Check if button is on screen.
        onView(withId(R.id.manualSearchButton)).check(matches(isDisplayed()));
        // Click button.
        onView(withId(R.id.manualSearchButton)).perform(click());
    }

    @Test
    public void testBottomNavbar(){
        BottomNavigationView bottomNavBar = activity.findViewById(R.id.bottom_nav);
    }

}


