package com.example.buber.Views.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.buber.App;
import com.example.buber.Model.Trip;
import com.example.buber.R;
import com.example.buber.Views.Activities.ContactViewerActivity;
import com.example.buber.Views.Activities.TripBuilderActivity;
import com.example.buber.Views.Activities.TripSearchActivity;
import com.example.buber.Views.Components.GetPathFromLocation;
import com.example.buber.Views.Components.TripSearchRecord;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import java.util.List;

/**
 * Fragment used to accept a trip. Generates a modal that shows trip details and allows a user to
 * accept a trip.
 */
public class AcceptTripRequestFragment extends DialogFragment {
    private TextView estimatedCost;
    private TextView startAdd;
    private TextView endAdd;
    private TextView rider;
    private TextView driverDistance;
    private Button viewContactButton;
    private TripSearchActivity parentActivity;

    private OnFragmentInteractionListener listener;
    private TripSearchRecord tripSearchRecord;
    private int position;
    private List<LatLng> routePointList;

    /**
     * Constructor for AcceptTripRequestFragment
     * @param tripSearchRecord,position the tripSearchRecord and its position in the tripSearch list*/
    public AcceptTripRequestFragment(TripSearchRecord tripSearchRecord, int position, TripSearchActivity parentActivity){
        this.tripSearchRecord = tripSearchRecord;
        this.position = position;
        this.parentActivity = parentActivity;
    }

    /**
     * OnAcceptPressed allows the driver to accept trips by pressing the accept button
     * */
    public interface OnFragmentInteractionListener{
        void onAcceptPressed(TripSearchRecord tripSearchRecord, int position);
    }

    /**onAttach handles the listener when the fragment is attached*/
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()+
                    "must implement onFragmentInteractionListener");
        }
    }
    /**
     * OnCreateDialog is builds the TripRequest Fragment and fills the views with correct
     * information from database
     * */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.accept_trip_request_fragment, null);

        estimatedCost = view.findViewById(R.id.fragment_estimated_cost);
        startAdd = view.findViewById(R.id.fragment_startAdd);
        endAdd = view.findViewById(R.id.fragment_endAdd);
        rider = view.findViewById(R.id.fragment_riderName);
        driverDistance = view.findViewById(R.id.fragment_distance);
        viewContactButton = view.findViewById(R.id.viewContactButton);

        //If the trip search record exists add information to the fragment
        if (tripSearchRecord != null){
            estimatedCost.setText(tripSearchRecord.getEstimatedCost());
            startAdd.setText(tripSearchRecord.getStartAddress());
            endAdd.setText(tripSearchRecord.getEndAddress());
            rider.setText(tripSearchRecord.getRiderName());
            driverDistance.setText(tripSearchRecord.getDistanceFromDriver());
            viewContactButton.setOnClickListener(v -> {
                handleViewRiderContact(tripSearchRecord.getRiderID());
            });

            //finds and initializes the mapView fragment
            MapView mapView = view.findViewById(R.id.lite_map);
            MapsInitializer.initialize(getActivity());
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            //Gets the mapAsync and overlays the route polyline and start/end markers
            mapView.getMapAsync(googleMap -> {
                LatLng origin = tripSearchRecord.getStartLatLng();
                LatLng destination = tripSearchRecord.getEndLatLng();
                new GetPathFromLocation(origin, destination, polyLine -> {
                    routePointList = polyLine.getPoints();
                    googleMap.addPolyline(polyLine);
                }).execute();
                googleMap.addMarker(new MarkerOptions().position(origin).title("start"));
                googleMap.addMarker(new MarkerOptions().position(destination).title("end"));
                LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                boundsBuilder.include(origin);
                boundsBuilder.include(destination);
                int routePadding = 120;
                LatLngBounds latLngBounds = boundsBuilder.build();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,routePadding));
            });

        }

        //builds the dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.
                setView(view)
                .setTitle("View Trip")
                .setNegativeButton("Ignore", null)
                .setPositiveButton("Fair enough, offer ride", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       listener.onAcceptPressed(tripSearchRecord, position);
                    }
                }).create();
    }

    /**Handles the user clicking the View Contact Details button
     * @param riderID is the riderID - it is used to pull the correct information*/
    public void handleViewRiderContact(String riderID) {
        Intent contactIntent = new Intent(parentActivity, ContactViewerActivity.class);
        App.getController().handleViewContactInformation(parentActivity, contactIntent, riderID, null);
    }

    /**Destroys the map when the dialog fragment is closed. It prevents the app from
     * crashing when new maps are drawn*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getActivity()
                .getFragmentManager()
                .findFragmentById(R.id.lite_map);
        if (f != null)
            getActivity().getFragmentManager().beginTransaction()
                    .remove(f).commit();
    }

}
