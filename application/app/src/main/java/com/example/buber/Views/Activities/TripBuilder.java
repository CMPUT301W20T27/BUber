package com.example.buber.Views.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Location;
import com.example.buber.Model.Trip;
import com.example.buber.R;
import com.example.buber.Views.UIErrorHandler;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TripBuilder extends AppCompatActivity implements UIErrorHandler, Observer {
    Location startLoc, endLoc;
    PlacesClient placesClient;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String apiKey = "AIzaSyDFEIMmFpPoMijm_0YraJn4S33UvtlnqF8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_builder_layout);
        App.getModel().addObserver(this);

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(TripBuilder.this, apiKey);
        }
        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment fromAutocompleteSupportFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.from_autocomplete_fragment);

        final AutocompleteSupportFragment toAutocompleteSupportFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.to_autocomplete_fragment);

        fromAutocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        toAutocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        fromAutocompleteSupportFragment.getView().setBackgroundColor(Color.WHITE);
        toAutocompleteSupportFragment.getView().setBackgroundColor(Color.WHITE);


        // Set up a PlaceSelectionListener to handle the response.
        fromAutocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                final LatLng latLng = place.getLatLng();
                Toast.makeText(getBaseContext(),
                        "lat:" + latLng.latitude +
                                "long:" + latLng.longitude,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });

        toAutocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                final LatLng latLng = place.getLatLng();
                Toast.makeText(getBaseContext(),
                        "lat:" + latLng.latitude +
                                "long:" + latLng.longitude,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                final LatLng latLng = place.getLatLng();
                Toast.makeText(getBaseContext(),
                        "lat:" + latLng.latitude +
                                "long:" + latLng.longitude,
                        Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void handleSelectStartPointClick() {

    }

    @Override
    public void update(Observable o, Object arg) {
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
    }
}
