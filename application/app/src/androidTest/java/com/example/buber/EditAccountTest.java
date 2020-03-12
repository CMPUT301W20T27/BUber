package com.example.buber;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Account;
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

import java.util.HashMap;

import static java.lang.System.err;
import static junit.framework.TestCase.assertTrue;

public class EditAccountTest {
    private Solo solo;


    //Signs out previous user and "signs in" a mockUser for testing
    public EditAccountTest(){
        App.getAuthDBManager().signOut(); // Ensure any user is already signed out

        //Create mockAccount and mockUser -> setSessionUser(mockUser)
        // Note: this logs a mockUser into the app
        Account mockAccount = new Account("Test", "User", "testUser@test.test", "0001112222");
        Rider rider = new Rider("mockUser", mockAccount);
        rider.setRiderLoggedOn(true);
        User mockUser = rider;
        App.getModel().setSessionUser(mockUser);
        EventCompletionListener listener = (resultData, err) -> {};
        App.getAuthDBManager().signIn("testUser@test.test","password", listener);
    }
    
    @Rule
    public ActivityTestRule<EditAccountActivity> rule = new ActivityTestRule<>(EditAccountActivity.class, true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    //Tests a correct change to the User's EditAccount
    @Test
    public void testCorrectEditAccount(){
        //Assert that test in EditAccountActivity
        solo.assertCurrentActivity("Wrong Activity", EditAccountActivity.class);

        //clear all fields
        solo.clearEditText((EditText) solo.getView(R.id.createAccountUsername));
        solo.clearEditText((EditText) solo.getView(R.id.createAccountFirstName));
        solo.clearEditText((EditText) solo.getView(R.id.createAccountLastName));
        solo.clearEditText((EditText) solo.getView(R.id.createAccountPhoneNumber));

        //Change EditAccountActivity info
        solo.enterText((EditText) solo.getView(R.id.createAccountUsername), "newTesterChange");
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "newTesterChangeFN");
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), "newTesterChangeLN");
        solo.enterText((EditText) solo.getView(R.id.createAccountPhoneNumber), "0001111235");

        solo.clickOnButton("Save Changes");

    }

    //Tests a incorrect input in EditAccount Activity
    @Test
    public void testEditAccountFailures(){
        //Assert that test in EditAccountActivity
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
        solo.enterText((EditText) solo.getView(R.id.createAccountFirstName), "newTesterChangeFN");
        solo.clickOnButton("Save Changes");
        assertTrue(solo.waitForText("Please provide your last name", 1, 200));

        //entered username, firstname, lastname; all other fields empty
        solo.enterText((EditText) solo.getView(R.id.createAccountLastName), "newTesterChangeLN");
        solo.clickOnButton("Save Changes");
        assertTrue(solo.waitForText("Please provide a phone number", 1, 200));

        //entered username, firstname, lastname, phone number
        solo.enterText((EditText) solo.getView(R.id.createAccountPhoneNumber), "0001111235");
        solo.clickOnButton("Save Changes");

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
