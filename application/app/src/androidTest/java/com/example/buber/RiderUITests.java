package com.example.buber;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.example.buber.Views.Activities.TripBuilderActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Rider tests for testing login, ride creation, correct views, ride cancellation, and
 * notification popups. Also veryfying correctness of ride status page.
 * Press run all to run tests
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RiderUITests {
    private Solo solo;
    private boolean onlyVisible = true;
    private UiDevice mDevice;
    private static final String TAG = "RiderUITests";
    public RiderUITests() {

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
        runLogin();
    }

    public void runLogin() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        if (!solo.waitForActivity(MapActivity.class, 1000)) {
            String email = "tester@tester.tester";
            String password = "123456";
            solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
            solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
            solo.clickOnButton("Login as Rider");
        }
    }
    @Test
    public void cleanUp() {
        // Remove trip from firebase

            if(solo.searchText("Cancel Your Current Ride Request", onlyVisible)){
            solo.clickOnButton("Cancel Your Current Ride Request");
            solo.clickOnText("Yes");
        }

        if (solo.searchText("Trip accepted! Cancel driver pick-up?", onlyVisible)) {
            solo.clickOnButton("Trip accepted! Cancel driver pick-up?");
        }

     }

    @Test
    public void cancelTripBeforDriverPicksUp() {
        //check for popup
        if(solo.searchText("Your ride is here", onlyVisible)){
            solo.waitForDialogToOpen(5000);
            assertTrue(solo.searchText("Your ride is here", 1));
            //click "NO, IAM CANCELLING"
            solo.clickOnButton(0);
            solo.waitForActivity(MapActivity.class, 5000);
        }

    }


    @Test
    public void createTrip(){

        if(solo.searchText("Request a Ride", onlyVisible)) {
            solo.clickOnButton("Request a Ride");

            //select start point
            solo.clickOnButton("Select Start Point");
            solo.clickOnText("Search");
            solo.typeText(0, "Megan Johnson High-Walkability Path");
            solo.clickOnText("Megan Johnson High-Walkability Path", 2);
            solo.clickOnText("OK");

            //Select end point
            solo.clickOnButton("Select End Point");
            solo.clickOnText("Search");
            solo.typeText(0, "1842 N Shoreline Blvd");
            solo.clickOnText("1842 N Shoreline Blvd", 2);
            solo.clickOnText("OK");

            //Submit Trip Request
            solo.clickOnButton("Submit Trip Request");


            solo.waitForActivity(MapActivity.class, 1000000000);
        }
    }

    @Test
    public void tripStatusCheckforEN_ROUTE() {
        //check for popup

        if(solo.searchText("Your ride is here", onlyVisible)){
            solo.waitForDialogToOpen(5000);
            assertTrue(solo.searchText("Your ride is here", 1));
            //click YES
            solo.clickOnButton(1);
            solo.waitForActivity(MapActivity.class, 5000);
        }

    }


    @Test
    public void tripStatusCheckforDRIVER_PICKING_UP() {

        if(solo.searchText("A driver has accepted! Proceed?", onlyVisible)){
            solo.waitForDialogToOpen(5000);
            assertTrue(solo.searchText("A driver has accepted! Proceed?", 1));
            solo.searchText("A driver has accepted! Proceed?", 1);
            solo.clickOnButton(1);
            solo.waitForActivity(MapActivity.class, 5000);
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
    public void testInvalidTripEntry() {

        if(solo.searchText("Request a Ride", onlyVisible)){
            solo.assertCurrentActivity("Wrong activity", MapActivity.class);
            solo.clickOnButton("Request a Ride");
            assertTrue(solo.waitForActivity(TripBuilderActivity.class, 1000));
            assertFalse(solo.getView(R.id.submitTripBtn).getVisibility() == View.INVISIBLE);
        }
    }

    private void allowPermissionsIfNeeded()  {
        if (Build.VERSION.SDK_INT >= 23) {
            UiObject allowPermissions = mDevice.findObject(new UiSelector().text("Allow"));
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException e) {
                    Log.d("RiderUITests", "permissions error" + e);
                }
            }
        }
    }

}
