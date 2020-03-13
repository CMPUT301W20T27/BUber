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
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertTrue;

/**
 * Testing Driver logging on, rendering the correct views, and accepting a ride
 * NOTE: make sure there is the following ride before running this test:
 * Use Start location google plex, 1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA
 * Use End location google new york, 111 8th Ave, New York, NY 10011, USA
 * You can run all these tests at once
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DriverUITests {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        //driverLogin();
    }

    @Test
    public void driverLogin(){

        App.getAuthDBManager().signOut(); // Ensure any user is already signed out
        String email = "nickagain2@gmail.com";
        String password = "password";
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
        solo.clickOnButton("Login As Driver");
        assertTrue(solo.waitForText("You are NOW logged in."));
        solo.sleep(1000);  //firebase is asyncronous. allow time
        solo.assertCurrentActivity("Wrong Activity",MapActivity.class);
    }

//    @Test
//    public void testDriverFindRide(){
//        solo.clickOnButton("Show Active Ride Requests Near You");
//        solo.assertCurrentActivity("Wrong activity", TripSearchActivity.class);
//        solo.clickOnText("1600 Amphitheatre Pkwy");
//        solo.clickOnText("Accept");
//        solo.sleep(1000);
//        solo.assertCurrentActivity("Wrong activity", MapActivity.class);
//        assertTrue(solo.waitForText("Trip status changed to: DRIVER_ACCEPT"));
//    }
//
//    @Test
//    public void testStatusRideDriver(){
//        //sign in as driver first
//        solo.clickOnButton("Ride Status");
//        solo.assertCurrentActivity("Wrong Activity", RequestStatusActivity.class);
//        assertTrue(solo.waitForText("Ride Status:"));
//        assertTrue(solo.waitForText("DRIVER_ACCEPT"));  //expected output
//        assertTrue(solo.waitForText("Amphitheatre"));
//        assertTrue(solo.waitForText("New York"));
//    }
}
