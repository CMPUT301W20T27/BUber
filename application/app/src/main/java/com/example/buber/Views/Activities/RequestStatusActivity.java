package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.DB.DBManager;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Services.ApplicationService;
import com.example.buber.Views.UIErrorHandler;

import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import static com.example.buber.Model.User.TYPE.RIDER;

/**
 *
 * If u need too, call ApplicationController.getRiderCurrentTrip(this)
 *
 */
public class RequestStatusActivity extends AppCompatActivity implements Observer, UIErrorHandler {
    private TextView statusTextView;
    private TextView startTextView;
    private TextView endTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_status);
        App.getModel().addObserver(this);

        statusTextView = findViewById(R.id.txtRideStatus);
        startTextView = findViewById(R.id.txtStartLocation);
        endTextView = findViewById(R.id.txtEndLocation);

        //This method grabs ride request info. Once data received, proceed to update
        //ApplicationController.getRiderCurrentTrip(this);
        fillStatusForm();
    }

    public void fillStatusForm(){
        User curUser = App.getModel().getSessionUser();
        Trip trip = App.getModel().getSessionTrip();
        if(trip==null){
            Toast.makeText(this,
                    "no request made",Toast.LENGTH_SHORT).show();
            this.finish();
        }

        if(curUser.getType()==RIDER){
            statusTextView.setText(trip.getStatus().toString());

            startTextView.setText(trip.getStartUserLocation().getAddress());
            endTextView.setText(trip.getEndUserLocation().getAddress());
        }

        else{
            statusTextView.setText(trip.getStatus().toString());

            startTextView.setText(trip.getStartUserLocation().getAddress());
            endTextView.setText(trip.getEndUserLocation().getAddress());

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