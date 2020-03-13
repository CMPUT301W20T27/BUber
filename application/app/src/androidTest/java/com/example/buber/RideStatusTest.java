package com.example.buber;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Views.Activities.CreateAccountActivity;
import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.example.buber.Views.Activities.RequestStatusActivity;
import com.example.buber.Views.Activities.TripBuilderActivity;
<<<<<<< HEAD
=======
import com.example.buber.Views.Activities.TripSearchActivity;
>>>>>>> b31765f5062865baa28cda75187f02e611b01a38
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class RideStatusTest {

    private Solo solo;

    public RideStatusTest() {
        //App.getAuthDBManager().signOut(); // Ensure any user is already signed out
    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /*TESTS*/

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testCorrectLogin() {
        App.getAuthDBManager().signOut(); // Ensure any user is already signed out
        String email = "nickagain2@gmail.com";
        String password = "password";
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
        solo.clickOnButton("Login as Rider");
        assertTrue(solo.waitForText("You are NOW logged in.", 1, 5000));
        assertTrue(solo.waitForActivity(MapActivity.class));
    }

<<<<<<< HEAD
    public void driverLogin(){

        App.getAuthDBManager().signOut(); // Ensure any user is already signed out
        String email = "nickagain2@gmail.com";
        String password = "password";
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
        solo.clickOnButton("Login as Rider");
        assertTrue(solo.waitForText("You are NOW logged in.", 1, 5000));
        assertTrue(solo.waitForActivity(MapActivity.class));
    }


=======
>>>>>>> b31765f5062865baa28cda75187f02e611b01a38
    @Test
    public void testCreateRide() {
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
    public void testStatusRideRider(){
        //testCreateRide
        solo.assertCurrentActivity("Wrong Activity",MapActivity.class);
        solo.clickOnButton("testStatus");
        solo.assertCurrentActivity("Wrong Activity",RequestStatusActivity.class);
        assertTrue(solo.waitForText("Ride Status:"));
        assertTrue(solo.waitForText("PENDING"));  //expected output
        assertTrue(solo.waitForText("Amphitheatre Pkwy, Mountain"));
        assertTrue(solo.waitForText("10800 Torre Ave, Cupertino, CA 95014, USA"));
    }

<<<<<<< HEAD
    @Test
    public void testDriverFindRide(){

    }

    @Test
    public void testStatusRideDriver(){
        //sign in as driver first
        solo.assertCurrentActivity("Wrong Activity",MapActivity.class);
        solo.clickOnButton("testStatus");
        solo.assertCurrentActivity("Wrong Activity",RequestStatusActivity.class);
        assertTrue(solo.waitForText("Ride Status:"));
        assertTrue(solo.waitForText("PENDING"));  //expected output
        assertTrue(solo.waitForText("Amphitheatre Pkwy, Mountain"));
        assertTrue(solo.waitForText("10800 Torre Ave, Cupertino, CA 95014, USA"));
    }

=======
>>>>>>> b31765f5062865baa28cda75187f02e611b01a38

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}

