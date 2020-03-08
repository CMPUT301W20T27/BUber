package com.example.buber.Views.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.Model.UserLocation;
import com.example.buber.R;
import com.example.buber.Views.UIErrorHandler;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class TripBuilderActivity extends AppCompatActivity implements UIErrorHandler, Observer {
    // Trip tings
    Trip tripRequest;
    UserLocation startLoc, endLoc;
    LinearLayout fareOfferingLayout;
    double minimumFareOffering;
    double riderFareOffering;

    // UI
    EditText fareOfferingEditText;
    Button submitTripBtn;

    // AutoComplete Places API
    PlacesClient placesClient;
    String apiKey = "AIzaSyDFEIMmFpPoMijm_0YraJn4S33UvtlnqF8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_builder_layout);
        App.getModel().addObserver(this);


        // Set start location parameters from previous map activity
        double[] currentLatLong = getIntent().getDoubleArrayExtra("currentLatLong");
        startLoc = new UserLocation(currentLatLong[0], currentLatLong[1]);

        // Initially hide visibility of price offering
        fareOfferingLayout =  findViewById(R.id.fareOfferingEditTextEstimationlinearLayout);
        fareOfferingEditText = findViewById(R.id.fareOfferingEditText);

        // Submit trip button
        submitTripBtn = findViewById(R.id.submitTripBtn);

        // Disengage submission UI elements
        disEngageSubmissionUI();

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(TripBuilderActivity.this, apiKey);
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

        // UI
        fromAutocompleteSupportFragment.setHint("Currently Your Location");
        toAutocompleteSupportFragment.setHint("End Point");
        fromAutocompleteSupportFragment.getView().setBackgroundColor(Color.WHITE);
        toAutocompleteSupportFragment.getView().setBackgroundColor(Color.WHITE);


        /* When User Clears AutoComplete Map Fields */
        fromAutocompleteSupportFragment
                .getView()
                .findViewById(R.id.places_autocomplete_clear_button)
                .setOnClickListener((View v) -> {
                    startLoc = new UserLocation(currentLatLong[0], currentLatLong[1]);
                    if (endLoc == null) {
                        disEngageSubmissionUI();
                    } else {
                        recalculateFareOffering();
                    }
                    fromAutocompleteSupportFragment.setText("");

                });
        toAutocompleteSupportFragment
                .getView()
                .findViewById(R.id.places_autocomplete_clear_button)
                .setOnClickListener((View v) -> {
                    endLoc = null;
                    disEngageSubmissionUI();
                    toAutocompleteSupportFragment.setText("");
                });

        /* When User Enters on AutoComplete Map Fields */
        fromAutocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                final LatLng latLng = place.getLatLng();
                if (startLoc == null) {
                    startLoc = new UserLocation(latLng.latitude, latLng.longitude);
                } else {
                    startLoc.setLatitude(latLng.latitude);
                    startLoc.setLongitude(latLng.longitude);
                }

                if (startLoc != null && endLoc != null) {
                    recalculateFareOffering();
                    engageSubmissionUI();
                } else {
                    disEngageSubmissionUI();
                }
            }

            @Override
            public void onError(Status status) {
            }
        });
        toAutocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                final LatLng latLng = place.getLatLng();
                if (endLoc == null) {
                    endLoc = new UserLocation(latLng.latitude, latLng.longitude);
                } else {
                    endLoc.setLatitude(latLng.latitude);
                    endLoc.setLongitude(latLng.longitude);
                }

                if (startLoc != null && endLoc != null) {
                    recalculateFareOffering();
                    engageSubmissionUI();
                } else {
                    disEngageSubmissionUI();
                }
            }

            @Override
            public void onError(Status status) {
            }
        });

        submitTripBtn.setOnClickListener((View v) -> {
            double offeredFare = Double.valueOf(fareOfferingEditText.getText().toString().trim());
            if (offeredFare >= minimumFareOffering) {
                riderFareOffering = offeredFare;

                String riderID = App.getAuthDBManager().getCurrentUserID();
                tripRequest = new Trip(
                        riderID,
                        offeredFare,
                        startLoc,
                        endLoc);
                App.getController().createNewTrip(tripRequest, this);
                startActivity(new Intent(getBaseContext(), MapActivity.class));
            } else {
                // Remind user of minimum cost
                recalculateFareOffering();
                fareOfferingEditText.setError(
                        "Please enter an amount higher than" + minimumFareOffering);
            }
        });
    }


    private void recalculateFareOffering() {
        minimumFareOffering = startLoc.distancePriceEstimate(endLoc);
        riderFareOffering = minimumFareOffering;
        fareOfferingEditText.setText(Double.toString(riderFareOffering));
    }

    private void engageSubmissionUI() {
        fareOfferingLayout.setVisibility(View.VISIBLE);
        submitTripBtn.setVisibility(View.VISIBLE);
    }

    private void disEngageSubmissionUI() {
        fareOfferingLayout.setVisibility(View.GONE);
        submitTripBtn.setVisibility(View.GONE);
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
