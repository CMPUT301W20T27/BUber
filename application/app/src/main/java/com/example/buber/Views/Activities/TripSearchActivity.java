package com.example.buber.Views.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.Model.UserLocation;
import com.example.buber.R;
import com.example.buber.Views.UIErrorHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TripSearchActivity extends AppCompatActivity implements UIErrorHandler, Observer {

    private static final String TAG = "TripSearchActivity";
    
    ArrayList<Trip> allTrips;
    ArrayList<Trip> filteredTrips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_search_activity);

        // TODO MIKE: Get UI elements and initialize here

        App.getModel().addObserver(this);
        Log.d(TAG, "onCreate: We here bois");

        /**
         * This returns an arraylist of all the trips in the database
         * This arraylist is updated whenever the database is updated
         */
        allTrips = App.getDbManager().getTrips((hashMap, error) -> { // This listener is called whenever the trips in the database are updated

            updateTrips();

        });

        /**
         * This returns an arraylist of filtered trips in the database
         * This arraylist is updated whenever the database is updated
         */
        filteredTrips = App.getDbManager().getFilteredTrips((hashMap, error) -> { // This listener is called whenever the filtered trips in the database are updated
            updateFilteredTrips();
        });

    }

    public void populateTripList() {
        UserLocation driverLoc = App.getModel().getSessionUser().getCurrentUserLocation();

        App.getController().getDriverTrips(driverLoc, this);
    }

    /**
     * This method is called whenever the "trips" arraylist is updated
     */
    public void updateTrips() {
        // TODO MIKE: This is called every time db updates so u probably want to do stuff here or call things here like:
        Log.d(TAG, "Updated All trips");
        for (Trip trip : filteredTrips) {
            Log.d(TAG, "ALL TRIPS item: " + trip.getDocID());
        }



    }

    /**
     * This method is called whenever the "filtered" arraylist is updated
     */
    public void updateFilteredTrips() {
        // TODO MIKE: This is called every time db updates so u probably want to do stuff here or call things here like:
        Log.d(TAG, "Updated Fileted trips");
        for (Trip trip : filteredTrips) {
            Log.d(TAG, "Filtered TRIPS item: " + trip.getDocID());
        }




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
