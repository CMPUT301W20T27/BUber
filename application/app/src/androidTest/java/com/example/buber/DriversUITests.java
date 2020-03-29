package com.example.buber;

import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertTrue;

/**
 * Testing Driver logging on, rendering the correct views, and accepting a ride
 * NOTE: make sure there is the following ride before running this test:
 * Use Start location : current location (default)
 * Use End location : Cupertino Library
 * Press run all to run tests
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DriversUITests {
    private static final String TAG = "DriverUITests";
    private Solo solo;
    private boolean onlyVisible = true;
    private UiDevice mDevice;

    public DriversUITests() {

        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        allowPermissionsIfNeeded();
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
    public void viewContactAndSelectTripDRIVER_ACCEPT() throws InterruptedException {
        //select trip with username = tester

        if(solo.searchText("Show Active Ride Requests Near You", onlyVisible)) {
            solo.clickOnText("Show Active Ride Requests Near You");
            solo.clickOnText("tester");
            //view contact information of the rider before accepting
            solo.clickOnText("View Contact Details");
            solo.waitForText("wait", 0, 500);

            //click the back button
            solo.goBack();
            solo.clickOnText("tester");
            solo.clickOnButton(2);
        }
    }

    @Test
    public void viewDriverContactAndRatingTestPhone() {
        //test calling

        if (solo.searchText("Ride Status", onlyVisible)) {
            solo.clickOnButton("Ride Status");
            solo.waitForText("wait", 0, 500);

            if (solo.searchText("View Contact Details", onlyVisible)) {
                solo.clickOnButton("View Contact Details");
                // Test phone
                solo.clickOnText("Phone");
                solo.waitForDialogToOpen(100);
                assertTrue(solo.searchText("Do you want to phone this driver", 1));
                solo.searchText("Do you want to phone this driver", 1);
                solo.clickOnButton("OK");
            }
        }

    }

    @Test
    public void viewDriverContactAndRatingTestEmail() {
        //Test email
        if (solo.searchText("Ride Status", onlyVisible)) {
            solo.clickOnButton("Ride Status");
            solo.waitForText("wait", 0, 500);
            if (solo.searchText("View Contact Details", onlyVisible)) {
                solo.clickOnButton("View Contact Details");
                solo.clickOnText("Email");
                solo.waitForDialogToOpen(100);
                assertTrue(solo.searchText("Do you want to email this driver", 1));
                solo.searchText("Do you want to email this driver", 1);
                solo.clickOnButton("OK");
                solo.waitForText("wait", 0, 500);
            }
        }

    }


    @Test
    public void tripStatusCheckforDRIVER_ARRIVED() {
        //TODO: When luke finishes his EN_ROUTE so that it follows MVC so that the
        // location in the controller changes when the driver Location changes
        // So that the rider is notified ONLY based on the location in the MODEl

        //check for message: Notifying rider you have arrived
    }


    @Test
    public void tripStatusCheckforCOMPLETED() {
        //TODO: When luke finishes his EN_ROUTE so that it follows MVC so that the
        // location in the controller changes when the driver Location changes
        // So that the rider is notified ONLY based on the location in the MODEl

        //check for message: Notifying rider you have arrived
    }


    private void allowPermissionsIfNeeded()  {
        if (Build.VERSION.SDK_INT >= 23) {
            UiObject allowPermissions = mDevice.findObject(new UiSelector().text("Allow"));
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException e) {
                    Log.d("DriverUITests", "permissions error" + e);
                }
            }
        }
    }



}
