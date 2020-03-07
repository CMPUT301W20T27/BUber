package com.example.buber.Views.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.Model.UserLocation;
import com.example.buber.R;
import com.example.buber.Views.Activities.FormUtilities.CustomTripList;
import com.example.buber.Views.Activities.FormUtilities.TripSearchRecord;
import com.example.buber.Views.UIErrorHandler;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TripSearchActivity extends AppCompatActivity implements UIErrorHandler, Observer {

    ListView tripSearchList;
    ArrayAdapter<TripSearchRecord> tripSearchRecordArrayAdapter;
    ArrayList<TripSearchRecord> tripDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_search_activity);

        // TODO MIKE: Get UI elements and initialize here
        // getting list view for tripSearchList
        tripSearchList = findViewById(R.id.trip_search_list);

        App.getModel().addObserver(this);

        populateTripList();

        tripSearchRecordArrayAdapter = new CustomTripList(this, tripDataList)
    }

    public void populateTripList() {
        UserLocation driverLoc = App.getModel().getSessionUser().getCurrentUserLocation();
        App.getController().getDriverTrips(driverLoc, this);
    }

    @Override
    public void onError(Error e) {
        // TODO MIKE: Handle Incoming UI Errors
    }


    @Override
    public void update(Observable o, Object arg) {
        // TODO MIKE: Check for updated query result from model
        //  If exists, populate the list, its called driverQueryResult
    }

    // TODO MIKE: Lets add some handlers for:
    //  1. Viewing details for a ride when a user clicks on them (add a itemclicklistener to each item
    //  in the list)
    //  2. Have a empty method that is called when a user confirms they want to select a trip

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }
}
