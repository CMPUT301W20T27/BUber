package com.example.buber;

import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Model.Trip;
import com.example.buber.Model.UserLocation;
import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.example.buber.Views.Activities.RequestStatusActivity;
import com.example.buber.Views.Activities.TripBuilderActivity;
import com.example.buber.Views.Activities.TripSearchActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Testing Driver logging on, rendering the correct views, and accepting a ride
 * NOTE: make sure there is the following ride before running this test:
 * Use Start location : current location (default)
 * Use End location : Cupertino Library
 * Press run all to run tests
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DriverUITests {

    private Solo solo;
    private Solo driverSolo;
    public DriverUITests() {
        App.getAuthDBManager().signOut();
    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        //runLoginRider();  //make request first for driver
        //testRideCreation();
        runLoginDriver();
    }

    public void runLoginDriver() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        if (!solo.waitForActivity(MapActivity.class, 1000)) {
            String email = "tester2@tester.tester";
            String password = "123456";
            solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
            solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
            solo.clickOnText("Login As Driver");
        }
    }

    @Test
    public void selectTrip(){
        //select trip with username = tester
        if(solo.waitForText("Show Active Ride Requests Near You", 1, 1000)) {
            solo.clickOnText("Show Active Ride Requests Near You");
            solo.clickOnText("tester");
            solo.clickOnButton(2);
            solo.waitForActivity(MapActivity.class, 1000000000);
        }
    }


    @Test
    public void tripStatusCheck() {


    }


}
