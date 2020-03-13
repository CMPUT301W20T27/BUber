package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.DB.DBManager;
import com.example.buber.Model.Account;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Services.ApplicationService;
import com.example.buber.Views.UIErrorHandler;

import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import static com.example.buber.Model.User.TYPE.DRIVER;
import static com.example.buber.Model.User.TYPE.RIDER;

/**
 * Note: If You need too, call ApplicationController.getRiderCurrentTrip(this)
 */

 /** ReequestStatusActivity is used to show status of trip requests, as well as driver's info
 */
public class RequestStatusActivity extends AppCompatActivity implements Observer, UIErrorHandler {
    private TextView statusTextView;
    private TextView startTextView;
    private TextView endTextView;
    private TextView usernameTextTextView;
    private TextView usernameTextView;
    private TextView driverPhoneTextTextView;
    private TextView driverPhoneTextView;
    private TextView driverEmailTextTextView;
    private TextView driverEmailTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_status);
        App.getModel().addObserver(this);

        statusTextView = findViewById(R.id.txtRideStatus);
        startTextView = findViewById(R.id.txtStartLocation);
        endTextView = findViewById(R.id.txtEndLocation);

        usernameTextTextView = findViewById(R.id.txtRideStatusCurDriverTextTextView);
        usernameTextView = findViewById(R.id.txtRideStatusCurDriverUserName);
        driverPhoneTextTextView = findViewById(R.id.txtRideStatusPhoneTextTextView);
        driverPhoneTextView = findViewById(R.id.txtRideStatusDriverPhoneNumber);
        driverEmailTextTextView = findViewById(R.id.txtRideStatusEmailTextTextView);
        driverEmailTextView = findViewById(R.id.txtRideStatusDriverEmail);


        //initial form fill
        fillStatusForm();
    }

    /**Gets drivers info to be displayed*/
    public void getDriverInfo(String docID){
        App.getDbManager().getDriver(docID, (resultData, err) -> {
            if (resultData != null) {

                //this fetched driver is from the db
                Driver tmpDriver = (Driver) resultData.get("user");
                Account tmpAccount = tmpDriver.getAccount();
                usernameTextView.setText(tmpDriver.getUsername());
                driverPhoneTextView.setText(tmpAccount.getPhoneNumber());
                driverEmailTextView.setText(tmpAccount.getEmail());
            }
            else {
                Toast.makeText(this, err.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }

    /**Fills ride status form with correct user and trip data depending on which user is signed in*/
    public void fillStatusForm(){
        User curUser = App.getModel().getSessionUser();
        Trip trip = App.getModel().getSessionTrip();
        if(trip==null){
            Toast.makeText(this,
                    "no request made",Toast.LENGTH_SHORT).show();
            this.finish();
        }

        if(curUser.getType()==RIDER){  //rider opening the view
            statusTextView.setText(trip.getStatus().toString());
            startTextView.setText(trip.getStartUserLocation().getAddress());
            endTextView.setText(trip.getEndUserLocation().getAddress());
            if(trip.getDriverID()!= null){
                //TODO:: Error handling
                getDriverInfo(trip.getDriverID());
                usernameTextTextView.setVisibility(View.VISIBLE);
                driverEmailTextTextView.setVisibility(View.VISIBLE);
                driverPhoneTextTextView.setVisibility(View.VISIBLE);
                usernameTextView.setVisibility(View.VISIBLE);
                driverEmailTextView.setVisibility(View.VISIBLE);
                driverPhoneTextView.setVisibility(View.VISIBLE);
            }
            else{  //no driver accepted yet
                usernameTextTextView.setVisibility(View.INVISIBLE);
                driverEmailTextTextView.setVisibility(View.INVISIBLE);
                driverPhoneTextTextView.setVisibility(View.INVISIBLE);
                usernameTextView.setVisibility(View.INVISIBLE);
                driverEmailTextView.setVisibility(View.INVISIBLE);
                driverPhoneTextView.setVisibility(View.INVISIBLE);
            }
        }

        else{  //driver opening the view
            statusTextView.setText(trip.getStatus().toString());
            startTextView.setText(trip.getStartUserLocation().getAddress());
            endTextView.setText(trip.getEndUserLocation().getAddress());
            usernameTextTextView.setVisibility(View.INVISIBLE);
            driverEmailTextTextView.setVisibility(View.INVISIBLE);
            driverPhoneTextTextView.setVisibility(View.INVISIBLE);
            usernameTextView.setVisibility(View.INVISIBLE);
            driverEmailTextView.setVisibility(View.INVISIBLE);
            driverPhoneTextView.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void update(Observable o, Object arg) {
        fillStatusForm();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    @Override
    public void onError(Error e) {
        //TODO:: hand ui errors
    }
}