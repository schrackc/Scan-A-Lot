//package com.example.scanalot;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static androidx.test.espresso.action.ViewActions.typeText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(AndroidJUnit4.class)
//public class SelectLotFragmentUITest {
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
//
//    @Test
//    public void verifySelectLotButtonDisplayed() {
//        // Launch the SelectLotFragment by clicking on its menu item
//        onView(withId(R.id.select_lot_fragment)).perform(click());
//        // Check that the select lot button is displayed
//        onView(withId(R.id.selectLotButton)).check(matches(isDisplayed()));
//        // Launch the SelectLotFragment by clicking on its menu item
//        activityRule.getScenario();
//        // select from spinner
//        onView(withId(R.id.editTicketSearchBox)).perform(typeText("1111"), closeSoftKeyboard());
//        // Click button,
//        onView(withId(R.id.editTicketSearchButton)).perform(click());
//    }
//
//}

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
public class SelectLotFragmentUITest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void verifySelectLotControls() {
        // Launch the SelectLotFragment by clicking on its menu item
        onView(withId(R.id.select_lot_fragment)).perform(click());
        // Click spinner to focus
        onView(withId(R.id.selectLotSpinner)).perform(click());
        // select lot from spinner
        onData(allOf(is(instanceOf(String.class)), is("Lot A")))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.selectLotSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Lot B")))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.selectLotSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Lot C")))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.selectLotSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Lot D")))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.selectLotSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Lot E")))
                .inRoot(isPlatformPopup())
                .perform(click());
        // Click confirm button.
        onView(withId(R.id.selectLotSpinner)).perform(click());
    }
}