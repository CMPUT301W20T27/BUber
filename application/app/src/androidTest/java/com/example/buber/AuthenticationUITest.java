package com.example.buber;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Views.Activities.CreateAccountActivity;
import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class AuthenticationUITest {
    private Solo solo;

    public AuthenticationUITest() {
        App.getAuthDBManager().signOut(); // Ensure any user is already signed out
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
    public void testLoginFailures() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), "");
        solo.clickOnButton("Login as Rider");
        assertTrue(solo.waitForText("Please provide your email", 1, 200));

        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), "tester@tester.tester");
        solo.clickOnButton("Login as Rider");
        assertTrue(solo.waitForText("Password must be at least 6 digits", 1, 200));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }


    @Test
    public void testCreateAccountFailures() {
        solo.clickOnButton("Create an Account");
        solo.assertCurrentActivity("Wrong Activity", CreateAccountActivity.class);


        solo.clickOnButton("Create Account");
        assertTrue(solo.waitForText("Please provide a username", 1, 200));
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "tester");

        solo.clickOnButton("Create Account");
        assertTrue(solo.waitForText("Password must be at least 6 digits", 1, 200));

        solo.enterText((EditText) solo.getView(R.id.createAccountPassword), "12345");
        solo.clickOnButton("Create Account");
        assertTrue(solo.waitForText("Password must be at least 6 digits", 1, 200));


        solo.enterText((EditText) solo.getView(R.id.createAccountPassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "tester");
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), "tester");

        solo.clearEditText((EditText) solo.getView(R.id.createAccountEmail));
        solo.enterText((EditText) solo.getView(R.id.createAccountEmail), "tester");
        solo.clickOnButton("Create Account");
        assertTrue(solo.waitForText("Please provide a valid email", 1, 200));

        solo.clearEditText((EditText) solo.getView(R.id.createAccountEmail));
        solo.enterText((EditText) solo.getView(R.id.createAccountEmail), "tester@");
        solo.clickOnButton("Create Account");
        assertTrue(solo.waitForText("Please provide a valid email", 1, 200));

        solo.clearEditText((EditText) solo.getView(R.id.createAccountEmail));
        solo.enterText((EditText) solo.getView(R.id.createAccountEmail), "tester@tester.");
        solo.clickOnButton("Create Account");
        assertTrue(solo.waitForText("Please provide a valid email", 1, 200));

        solo.clearEditText((EditText) solo.getView(R.id.createAccountEmail));
        solo.enterText((EditText) solo.getView(R.id.createAccountEmail), "tester@tester.asdf.");
        solo.clickOnButton("Create Account");
        assertTrue(solo.waitForText("Please provide a valid email", 1, 200));
    }
    

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
