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
    }

}


