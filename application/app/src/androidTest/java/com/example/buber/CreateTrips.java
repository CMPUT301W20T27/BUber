package com.example.buber;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Views.Activities.CreateAccountActivity;
import com.example.buber.Views.Activities.LoginActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class  CreateTrips {
    ApplicationController c;
    private Solo solo;
    private boolean onlyVisible = true;

    public CreateTrips() {
        App.getAuthDBManager().signOut();
    }

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }


    @Test
    public void createTrip1() {
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), "mike@mike.mike");
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), "123456");
        solo.clickOnButton("Login as Rider");

        if (solo.searchText("Request a Ride", onlyVisible)) {
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
        }

    }

    @Test
    public void createTrip2() {
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), "nick@nick.nick");
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), "123456");
        solo.clickOnButton("Login as Rider");

        if (solo.searchText("Request a Ride", onlyVisible)) {
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
        }

    }
    @Test
    public void createTrip3() {
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), "madeeha@madeeha.madeeha");
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), "123456");
        solo.clickOnButton("Login as Rider");

        if (solo.searchText("Request a Ride", onlyVisible)) {
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
        }

    }
    @Test
    public void createTrip4() {
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), "evan@evan.evan");
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), "123456");
        solo.clickOnButton("Login as Rider");

        if (solo.searchText("Request a Ride", onlyVisible)) {
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
        }

    }

}