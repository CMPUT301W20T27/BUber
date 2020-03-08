package com.example.buber.Views.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.R;
import com.example.buber.Views.Components.CustomTripList;
import com.example.buber.Views.Components.TripSearchRecord;
import com.example.buber.Views.Fragments.AcceptTripRequestFragment;
import com.example.buber.Views.UIErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TripSearchActivity extends AppCompatActivity implements UIErrorHandler, Observer,
        AcceptTripRequestFragment.OnFragmentInteractionListener {

    ListView tripSearchList;
    ArrayAdapter<TripSearchRecord> tripSearchRecordArrayAdapter;
    ArrayList<TripSearchRecord> tripDataList;

    private static final String TAG = "TripSearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_search_activity);
        tripSearchList = findViewById(R.id.trip_search_list);
        App.getModel().addObserver(this);

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

        tripSearchList.setOnItemClickListener((parent, view, position, id) ->
                new AcceptTripRequestFragment(
                        tripDataList.get(position),
                        position).show(getSupportFragmentManager(),
                        "VIEW_RECORD"));

        updateTripList();
    }

    public void updateTripList() {
        ApplicationController.getTripsForUser(this);
    }

    @Override
    public void onError(Error e) {
        // TODO MIKE: Handle Incoming UI Errors
    }

    @Override
    public void update(Observable o, Object arg) {
        ApplicationModel m = (ApplicationModel) o;
        // Update tripDataList
        List<Trip> tripList = m.getSessionTripList();
        tripDataList.clear();
        for (Trip t: tripList) {
            tripDataList.add(new TripSearchRecord(t));
        }
        tripSearchRecordArrayAdapter.notifyDataSetChanged();
    }

    public void onAcceptPressed(TripSearchRecord tripSearchRecord, int position){
        // TODO: Backend code for selecting trip

        // Navigate back to main activity
        this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }
}
