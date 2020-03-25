package com.example.buber;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Model.Trip;
import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.example.buber.Views.Activities.RequestStatusActivity;
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
        boolean onlyVisible = true;
            if(solo.searchText("Cancel Your Current Ride Request", onlyVisible)){
            solo.clickOnButton("Cancel Your Current Ride Request");
            solo.clickOnText("Yes");
        }
     }

    @Test
    public void cancelTripBeforDriverPicksUp() {
        // Remove trip from firebase
        boolean onlyVisible = true;
        if (solo.searchText("Trip accepted! Cancel driver pick-up?", onlyVisible)) {
            solo.clickOnButton("Trip accepted! Cancel driver pick-up?");
        }
    }

    @Test
    public void createTrip(){
        boolean onlyVisible = true;
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
    public void tripStatusCheckforDRIVER_ARRIVED() {
        //when luke finishes his  EN_ROUTE
    }


    @Test
    public void tripStatusCheckforDRIVER_PICKING_UP() {
        boolean onlyVisible = true;
        if(solo.searchText("A driver has accepted! Proceed?", onlyVisible)){
            solo.waitForDialogToOpen(5000);
            assertTrue(solo.searchText("A driver has accepted! Proceed?", 1));
            solo.searchText("A driver has accepted! Proceed?", 1);
            solo.clickOnButton(1);
            solo.waitForActivity(MapActivity.class, 5000);
        }

    }

    @Test
    public void viewDriverContactAndRating() {
        //test calling

    }


    @Test
    public void testInvalidTripEntry() {
        boolean onlyVisible = true;
        if(solo.searchText("Request a Ride", onlyVisible)){
            solo.assertCurrentActivity("Wrong activity", MapActivity.class);
            solo.clickOnButton("Request a Ride");
            assertTrue(solo.waitForActivity(TripBuilderActivity.class, 1000));
            assertFalse(solo.getView(R.id.submitTripBtn).getVisibility() == View.INVISIBLE);
        }
    }

}
