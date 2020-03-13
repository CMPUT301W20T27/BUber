package com.example.buber;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Account;
import com.example.buber.Model.Rider;
import com.example.buber.Model.User;
import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;

public class BrowseAndSearchTest {
    private Solo solo;


    //Signs out previous user and "signs in" a mockUser for testing
    public BrowseAndSearchTest(){
        App.getAuthDBManager().signOut(); // Ensure any user is already signed out

        //Create mockAccount and mockUser -> setSessionUser(mockUser)
        // Note: this logs a mockUser into the app
        Account mockAccount = new Account("Test", "User", "testUser@test.test", "0001112222");
        //User mockUser = new Rider("mockUser", mockAccount);
        Rider rider = new Rider("mockUser", mockAccount);
        rider.setRiderLoggedOn(true);
        User mockUser = rider;
        App.getModel().setSessionUser(mockUser);
        EventCompletionListener listener = (resultData, err) -> {};
        App.getAuthDBManager().signIn("testUser@test.test","password", listener);

    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    //Note: app requires an account to be logged in to interact with map screen activities
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.enterText((EditText) solo.getView(R.id.loginEmailEditText), "testUser@test.test");
        solo.enterText((EditText) solo.getView(R.id.loginPasswordEditText), "password");
        solo.clickOnButton("Login as Rider");
        solo.waitForText("You are NOW logged in.", 1, 2000);
        solo.waitForActivity(MapActivity.class);

        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", MapActivity.class);

        solo.clickOnButton(1);
    }

}
