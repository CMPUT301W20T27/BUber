package com.example.buber;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.example.buber.Views.Activities.RequestStatusActivity;
import com.example.buber.Views.Activities.TripBuilderActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class RiderUITests {
    private Solo solo;

    public RiderUITests() {
        App.getAuthDBManager().signOut();
    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        runLogin();
    }

    @Test
    public void testRideCreation() {
        solo.clickOnButton("Request a Ride");
        assertTrue(solo.waitForActivity(TripBuilderActivity.class, 1000));
        TripBuilderActivity current = (TripBuilderActivity) solo.getCurrentActivity();

        solo.clickOnView(solo.getView(R.id.to_autocomplete_fragment));
        solo.sleep(1000);
        solo.clickOnText("Search");
        solo.enterText(2, "Cupertino");
        solo.sleep(1000);
        solo.clickOnText("Cupertino Library");
        assertTrue(solo.waitForText("Submit Trip Request"));
        solo.clickOnText("Submit Trip Request");
        assertTrue(solo.waitForActivity(MapActivity.class));
    }

    @Test
    public void testRideStatus(){
        solo.assertCurrentActivity("Wrong Activity",MapActivity.class);
        solo.clickOnButton("TESTSTATUS");
        solo.assertCurrentActivity("Wrong Activity", RequestStatusActivity.class);
        assertTrue(solo.waitForText("Ride Status:     "));
        assertTrue(solo.waitForText(""));
    }

    @Test
    public void testDisallowNewRideCreation() {
        solo.clickOnButton("Request a Ride");
        assertTrue(solo.waitForActivity(TripBuilderActivity.class, 1000));
        assertTrue(solo.waitForText("Cancel Your Current Ride Request"));
    }

    @Test
    public void testRiderNotifyAcceptRequest() {

    }

    @Test
    public void testInvalidFareEntry() {

    }

    @Test
    public void testTripCancel() {

    }

    @Test
    public void testInvalidTripEntry() {
        solo.clickOnButton("REQUEST A RIDE");
        assertTrue(solo.waitForActivity(TripBuilderActivity.class, 1000));
        assertFalse(solo.searchButton("SUBMIT TRIP REQUEST"));
    }

    public void runLogin() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        if (!solo.waitForActivity(MapActivity.class, 1000)) {
            String email = "evan@buber.ca";
            String password = "123456";
            solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
            solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
            solo.clickOnButton("Login as Rider");
        }
    }

}
