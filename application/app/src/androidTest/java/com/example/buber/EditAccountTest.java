package com.example.buber;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Model.Rider;
import com.example.buber.Model.User;
import com.example.buber.Views.Activities.EditAccountActivity;
import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class EditAccountTest {
    private Solo edit_solo;
    private Solo login_solo;
    private Solo solo;

    public EditAccountTest(){
        App.getAuthDBManager().signOut(); // Ensure any user is already signed out
    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);


    //Creates one rule to login to app with test account(used in setUp)
    @Rule
    public ActivityTestRule<MainActivity> login_rule = new ActivityTestRule<>(MainActivity.class, true, true);
    //Creates one rule for testing edit account
    @Rule
    public ActivityTestRule<EditAccountActivity> edit_account_rule = new ActivityTestRule<>(EditAccountActivity.class, true, true);

    //login with tester@tester.tester account before running tests for EditAccount
    //Note: app requires an account to be logged in to interact with map screen activities
    @Before
    public void setUp() throws Exception{
        User mockUser = new Rider();
        App.getModel().setSessionUser(mockUser);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        //edit_solo = new Solo(InstrumentationRegistry.getInstrumentation(),login_rule.getActivity());
        //edit_solo = new Solo(InstrumentationRegistry.getInstrumentation(),edit_account_rule.getActivity());

        //logging into tester@tester.tester account before testing begins
        //String email = "tester@tester.tester";
        //String password = "123456";
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        //solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), email);
        //solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), password);
        //solo.clickOnButton("Login as Rider");
        //assertTrue(solo.waitForActivity(MapActivity.class));
        //edit_solo = new Solo(InstrumentationRegistry.getInstrumentation(),edit_account_rule.getActivity());

    }

    //Tests a correct change to the User's EditAccount
    @Test
    public void testCorrectEditAccount(){
        solo.assertCurrentActivity("Wrong Activity", EditAccountActivity.class);
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "newTesterChange");
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "newTesterChangeFN");
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), "newTesterChangeLN");
        solo.enterText((EditText) solo.getView(R.id.createAccountPhoneNumber), "0001111235");
        solo.clickOnButton("Save Changes");
        assertTrue(solo.waitForActivity(MapActivity.class));
    }

    //Tests a incorrect input in EditAccount Activity
    @Test
    public void testEditAccountFailures(){
        solo.assertCurrentActivity("Wrong Activity", EditAccountActivity.class);

        //clear all fields
        solo.clearEditText((EditText) solo.getView(R.id.createAccountUsername));
        solo.clearEditText((EditText) solo.getView(R.id.createAccountFirstName));
        solo.clearEditText((EditText) solo.getView(R.id.createAccountLastName));
        solo.clearEditText((EditText) solo.getView(R.id.createAccountPhoneNumber));
        solo.clickOnButton("Save Changes");
        assertTrue(solo.waitForText("Please provide a username", 1, 200));

        //entered username; all other fields empty
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "newTesterChange");
        solo.clickOnButton("Save Changes");
        assertTrue(solo.waitForText("Please provide your first name", 1, 200));

        //entered username, firstname; all other fields empty
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "newTesterChange");
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "newTesterChangeFN");
        solo.clickOnButton("Save Changes");
        assertTrue(solo.waitForText("Please provide your last name", 1, 200));

        //entered username, firstname, lastname; all other fields empty
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "newTesterChange");
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "newTesterChangeFN");
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), "newTesterChangeLN");
        solo.clickOnButton("Save Changes");
        assertTrue(solo.waitForText("Please provide a phone number", 1, 200));

        //entered username, firstname, lastname, phone number
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "newTesterChange");
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "newTesterChangeFN");
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), "newTesterChangeLN");
        solo.enterText((EditText) solo.getView(R.id.createAccountPhoneNumber), "0001111235");
        solo.clickOnButton("Save Changes");
        assertTrue(solo.waitForActivity(MapActivity.class));
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        //login_solo.finishOpenedActivities();
        //edit_solo.finishOpenedActivities();
        solo.finishOpenedActivities();
    }

}
