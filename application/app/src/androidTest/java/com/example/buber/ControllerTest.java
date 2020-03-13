package com.example.buber;

import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.Driver;
import com.example.buber.Model.User;
import com.example.buber.Model.UserLocation;
import com.example.buber.Views.Activities.LoginActivity;
import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ControllerTest {
    ApplicationController c;
    Solo solo;


    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        App.getAuthDBManager().signOut();
        c = App.getController();
        App.getModel().clearModelForLogout();
    }


    @Test
    public void testLogin() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        String email = "evan@buber.ca";
        String password = "123456";
        LoginActivity activity = (LoginActivity) solo.getCurrentActivity();
        Intent i = new Intent(activity, MapActivity.class);
        c.login(email, password, User.TYPE.RIDER, activity, i);
        assertTrue(solo.waitForActivity(MapActivity.class));
    }

    @Test
    public void testLoadSessionTrip() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        LoginActivity activity = (LoginActivity) solo.getCurrentActivity();
        Intent i = new Intent(activity, MapActivity.class);
        c.loadSessionTrip(i, activity);
        // Should be true since no current user
        assertFalse(solo.waitForActivity(MapActivity.class));
    }

    @Test
    public void testLogout() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        String email = "evan@buber.ca";
        String password = "123456";
        LoginActivity activity = (LoginActivity) solo.getCurrentActivity();
        Intent i = new Intent(activity, MapActivity.class);
        c.login(email, password, User.TYPE.RIDER, activity, i);
        solo.waitForActivity(MapActivity.class);
        c.logout();
        assertFalse(App.getAuthDBManager().isLoggedIn());
        assertNull(App.getModel().getSessionUser());
    }

    @Test
    public void testGetTripsForUser() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        String email = "evan@buber.ca";
        String password = "123456";
        LoginActivity activity = (LoginActivity) solo.getCurrentActivity();
        Intent i = new Intent(activity, MapActivity.class);
        c.login(email, password, User.TYPE.RIDER, activity, i);
        solo.waitForActivity(MapActivity.class);

        User mockUser = new Driver();
        mockUser.setCurrentUserLocation(new UserLocation(10, 10));
        App.getModel().setSessionUser(mockUser);
        c.getTripsForUser(null);
        solo.sleep(1000);
        assertNotNull(App.getModel().getSessionTripList());
    }
}
