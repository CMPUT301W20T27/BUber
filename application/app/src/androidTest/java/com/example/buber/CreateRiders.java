package com.example.buber;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Views.Activities.CreateAccountActivity;
import com.example.buber.Views.Activities.LoginActivity;
import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateRiders {
    ApplicationController c;
    private Solo solo;
    private boolean onlyVisible = true;

    public CreateRiders() {
        App.getAuthDBManager().signOut();
    }

    @Rule
    public ActivityTestRule<CreateAccountActivity> rule = new ActivityTestRule<>(CreateAccountActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
//        runLogin();
    }

    @Test
    public void createRider1() {
        //Fill in with what you want
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "Madeeha");
        solo.enterText((EditText) solo.getView(R.id.createAccountPassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "Madeeha");
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), "A");
        solo.enterText((EditText) solo.getView(R.id.createAccountEmail), "madeeha@madeeha.madeeha");
        solo.enterText((EditText) solo.getView(R.id.createAccountPhoneNumber), "7801234567");
        solo.clickOnButton("Create Account");
        solo.waitForText("wait", 0, 5000);
    }

    @Test
    public void createRider2() {
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "Evan");
        solo.enterText((EditText) solo.getView(R.id.createAccountPassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "Evan");
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), "P");
        solo.enterText((EditText) solo.getView(R.id.createAccountEmail), "evan@evan.evan");
        solo.enterText((EditText) solo.getView(R.id.createAccountPhoneNumber), "7801234567");
        solo.clickOnButton("Create Account");
        solo.waitForText("wait", 0, 5000);
    }

    @Test
    public void createRider3() {
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "Nick");
        solo.enterText((EditText) solo.getView(R.id.createAccountPassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "Nick");
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), "S");
        solo.enterText((EditText) solo.getView(R.id.createAccountEmail), "nick@nick.nick");
        solo.enterText((EditText) solo.getView(R.id.createAccountPhoneNumber), "7801234567");
        solo.clickOnButton("Create Account");
        solo.waitForText("wait", 0, 5000);
    }

    @Test
    public void createRider4() {
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "Mike");
        solo.enterText((EditText) solo.getView(R.id.createAccountPassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "Mike");
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), "A");
        solo.enterText((EditText) solo.getView(R.id.createAccountEmail), "mike@mike.mike");
        solo.enterText((EditText) solo.getView(R.id.createAccountPhoneNumber), "7801234567");
        solo.clickOnButton("Create Account");
        solo.waitForText("wait", 0, 5000);
    }

}

