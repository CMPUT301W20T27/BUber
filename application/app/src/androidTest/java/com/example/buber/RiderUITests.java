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
        if(solo.waitForText("Cancel Your Current Ride Request", 1, 1000)) {
            solo.clickOnButton("Cancel Your Current Ride Request");
            solo.clickOnText("Yes");
        }
    }

    @Test
    public void createTrip(){
        if(solo.waitForText("Request a Ride", 1, 1000)) {
            solo.clickOnButton("Request a Ride");
            //select start point
            solo.clickOnButton("Select Start Point");
            solo.clickOnText("Search");
            solo.typeText(0, "Megan Johnson High-Walkability Path");
            solo.clickOnText("Megan Johnson High-Walkability Path", 2);
//        solo.waitForActivity(solo.getCurrentActivity().toString());
//        solo.waitForActivity(MapActivity.class, 1000000000);
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
    public void tripStatusCheck() {


    }




    @Test
    public void testInvalidTripEntry() {
        if (solo.waitForText("Request a Ride", 1, 1000)) {
            solo.assertCurrentActivity("Wrong activity", MapActivity.class);
            solo.clickOnButton("Request a Ride");
            assertTrue(solo.waitForActivity(TripBuilderActivity.class, 1000));
            assertFalse(solo.getView(R.id.submitTripBtn).getVisibility() == View.INVISIBLE);
        }
    }

}
