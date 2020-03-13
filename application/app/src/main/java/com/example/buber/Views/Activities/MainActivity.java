package com.example.buber.Views.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Driver;
import com.example.buber.Model.User;
import com.example.buber.Views.UIErrorHandler;

import java.util.Observable;
import java.util.Observer;

import static com.example.buber.Model.User.TYPE.DRIVER;
import static com.example.buber.Model.User.TYPE.RIDER;

/**
 * MainActivity. Used mostly as a router to check the current users login state and redirect
 * to either the Login or Main map activity (in case the user is already logged in)
 * TODO: MVC Updating and Error Handling.
 */
public class MainActivity extends AppCompatActivity implements Observer, UIErrorHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationModel m = App.getModel();
        m.addObserver(this);

        determineLoginStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        ApplicationModel m = (ApplicationModel) o;
    }

    /**Determines login status of user*/
    private void determineLoginStatus() {
        if(App.getAuthDBManager().isLoggedIn()) {
            App.getAuthDBManager().getCurrentSessionUser((resultData, err) -> {
                if (resultData != null) {
                    User tmpUser = (User) resultData.get("user");
                    App.getModel().setSessionUser(tmpUser);

                    //this fetched driver is from the db
                    Driver driver = (Driver) resultData.get("user");
                    tmpUser.setType(driver.getDriverLoggedOn() ? DRIVER : RIDER);

                    determineTripStatus();  // now determine trip status
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                    this.finish();
                }
                else {
                    Toast.makeText(this, err.getMessage(), Toast.LENGTH_LONG);
                }
            });
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            this.finish();
        }
    }

    /**Determines status of current trip if available*/
    private void determineTripStatus() {
        if (App.getModel().getSessionUser().getType() == RIDER) {
            ApplicationController.getRiderCurrentTrip(this);
        }
    }

    @Override
    public void onError(Error e) {

    }
}