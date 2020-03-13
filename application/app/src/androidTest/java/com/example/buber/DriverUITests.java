package com.example.buber;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.example.buber.Views.Activities.RequestStatusActivity;
import com.example.buber.Views.Activities.TripSearchActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class DriverUITests {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        //driverLogin();
    }

    public void cleanUp() {
        // Remove trip from firebase
        if (solo.waitForText("Cancel Your Current Ride Request")) {
            solo.clickOnText("Cancel Your Current Ride Request");
            solo.clickOnText("Yes");
            assertTrue(solo.waitForText("Request a Ride"));
        }

    }
    @Test
    public void driverLogin(){

        App.getAuthDBManager().signOut(); // Ensure any user is already signed out
        String email = "nickagain3@gmail.com";
        String password = "password";
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
        solo.clickOnButton("Login As Driver");
        assertTrue(solo.waitForText("You are NOW logged in."));
        solo.sleep(1000);  //firebase is asyncronous. allow time
        solo.assertCurrentActivity("Wrong Activity",MapActivity.class);
    }

    @Test
    public void testDriverFindRide(){
        solo.assertCurrentActivity("Wrong Activity", MapActivity.class);
        solo.clickOnButton("Show Active Ride Requests Near You");
        solo.assertCurrentActivity("Wrong activity", TripSearchActivity.class);
        solo.clickLongInList(1);
        solo.clickOnText("Accept");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong activity", MapActivity.class);
        assertTrue(solo.waitForText("Trip status changed to: DRIVER_ACCEPT"));
        solo.clickOnButton("Cancel Your Current Ride Request");  //cancel button
    }

    @Test
    public void testStatusRideDriver(){
        //sign in as driver first
        solo.assertCurrentActivity("Wrong Activity",MapActivity.class);
        solo.clickOnButton("Ride Status");
        solo.assertCurrentActivity("Wrong Activity", RequestStatusActivity.class);
        assertTrue(solo.waitForText("Ride Status:"));
        assertTrue(solo.waitForText("DRIVER_ACCEPT"));  //expected output
        assertTrue(solo.waitForText("Amphitheatre Pkwy, Mountain"));
        assertTrue(solo.waitForText("10800 Torre Ave, Cupertino, CA 95014, USA"));
    }
}
