package com.example.scanalot;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.rule.GrantPermissionRule.grant;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
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
    public void setUp() {
        activity = activityRule.getActivity();
        bottomNav = activity.findViewById(R.id.bottom_nav);
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testCameraPreviewIsDisplayed(){
        // Grants permissions
        grant(android.Manifest.permission.CAMERA);
        // checks for preview to be displayed
        onView(withId(R.id.previewView)).check(matches(isDisplayed()));
    }

    @Test
    public void testBottomNavbar(){
        BottomNavigationView bottomNavBar = activity.findViewById(R.id.bottom_nav);
        onView(withId(R.id.scan_fragment)).perform(click()); // click on the scan_fragment menu item
        onView(withId(R.id.scan_fragment)).check(matches(isDisplayed())); // check that scan_fragment is displayed

        onView(withId(R.id.edit_ticket_fragment)).perform(click()); // click on the edit_ticket_fragment menu item
        onView(withId(R.id.edit_ticket_fragment)).check(matches(isDisplayed())); // check that edit_ticket_fragment is displayed

        onView(withId(R.id.select_lot_fragment)).perform(click()); // click on the select_lot_fragment menu item
        onView(withId(R.id.select_lot_fragment)).check(matches(isDisplayed())); // check that select_lot_fragment is displayed

        onView(withId(R.id.log_out_fragment)).perform(click()); // click on the log_out_fragment menu item
        onView(withId(R.id.log_out_fragment)).check(matches(isDisplayed())); // check that log_out_fragment is displayed
    }

}
