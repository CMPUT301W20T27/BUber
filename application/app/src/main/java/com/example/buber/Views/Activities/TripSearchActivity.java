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

    //Variable declarations
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

        //Below are dummy variables for the list (for testing)
        String[] rider = {"RiderX", "RiderY", "RiderZ"};
        String[] startLat = {"03", "302", "230"};
        String[] startLong = {"21", "143", "212"};
        String[] endLat = {"43", "543", "002"};
        String[] endLong = {"234", "65", "900"};
        String[] estCost = {"$34.99", "$12.60", "$9.21"};
        String[] distDriver = {"3 KM", "12 KM", "0.05 KM"};

        tripDataList = new ArrayList<>();
        for(int i = 0; i< rider.length; i++){
            tripDataList.add((new TripSearchRecord(rider[i], startLat[i], startLong[i], endLat[i],
                    endLong[i], estCost[i], distDriver[i])));
        }

        //Activate the custom array adapter (CustomTripList)
        tripSearchRecordArrayAdapter = new CustomTripList(this, tripDataList);
        tripSearchList.setAdapter(tripSearchRecordArrayAdapter);
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
