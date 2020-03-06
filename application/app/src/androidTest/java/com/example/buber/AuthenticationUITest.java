package com.example.buber;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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
        assertTrue(solo.waitForText("Incorrect Password. Please Try Again.", 1, 2000));
        assertFalse(solo.waitForActivity(MapActivity.class));
    }

    @Test
    public void testOfflineLogin() {
        String email = "testaccount@buber.ca";
        String password = "testpassword";
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
        solo.clickOnButton("Login as Rider");
        assertTrue(solo.waitForText("You are offline. Please try again" +
                "when connected to the internet", 1, 2000));
        assertFalse(solo.waitForActivity(MapActivity.class));
    }

    @Test
    public void testSuccessCreateAccount() {

    }

    @Test
    public void testFailureCreateAccount() {

    }

    @Test
    public void testLoginPersistence() {

    }

    @Test
    public void testLogout() {

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
