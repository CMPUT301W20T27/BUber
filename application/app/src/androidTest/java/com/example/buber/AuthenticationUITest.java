package com.example.buber;

import android.app.Activity;
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
    public void testCorrectLogin() {
        String email = "testaccount@buber.ca";
        String password = "testpassword";
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
        solo.clickOnButton("Login as Rider");
        assertTrue(solo.waitForText("You are NOW logged in.", 1, 2000));
        assertTrue(solo.waitForActivity(MapActivity.class));
    }

    @Test
    public void testIncorrectLogin() {
        String email = "testaccount@buber.ca";
        String password = "incorrectpassword";
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
        solo.clickOnButton("Login as Rider");
        assertTrue(solo.waitForText("The password is invalid or the user does not have a password.", 1, 200));
        assertFalse(solo.waitForActivity(MapActivity.class));
    }


    @Test
    public void testFailureCreateAccount() {
        String username = "tester";
        String password = "123456";
        String firstName = "tester";
        String lastName = "tester";
        String email = "tester@buber.ca";
        String phoneNumber = "1234567890";

        solo.clickOnButton("Create an Account");
        solo.assertCurrentActivity("Wrong Activity", CreateAccountActivity.class);
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), username);
        solo.enterText((EditText) solo.getView(R.id.createAccountPassword), password);
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), firstName);
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), lastName);
        solo.enterText((EditText) solo.getView(R.id.createAccountEmail), email);
        solo.enterText((EditText) solo.getView(R.id.createAccountPhoneNumber), phoneNumber);
        solo.clickOnButton("Create Account");
    }
//
//    @Test
//    public void testLoginPersistence() {
//
//    }
//
//    @Test
//    public void testLogout() {
//
//    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
