package com.example.buber.Views.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
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
import com.rtchagas.pingplacepicker.PingPlacePicker;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

/**
 * Main Rider activity for building and creating a trip. Activity generates a form that uses the
 * Google search api to gather user locations. When form is submitted, new Trip instance is created
 * and added to Firebase.
 * TODO: MVC Updating and Error Handling.
 */


public class TripBuilderActivity extends AppCompatActivity implements UIErrorHandler, Observer {
    // Trip tings
    Trip tripRequest;
    UserLocation currentUserLoc, startLoc, endLoc;
    Geocoder geocoder;
    double minimumFareOffering;
    double riderFareOffering;
    int choosingBtnID;

    // UI
    ConstraintLayout tripSubmissionLayout;
    TextView startPointTextView;
    TextView endPointTextView;
    EditText fareOfferingEditText;
    public CircularProgressButton submitTripBtn;

    // AutoComplete Places API
    String apiKey = "AIzaSyDFEIMmFpPoMijm_0YraJn4S33UvtlnqF8";
    int REQUEST_PLACE_PICKER = 1;

    /**onCreate method will create TripBuilderActivity when it is run
     * @param savedInstanceState will get a previous saved state if available*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_builder_layout);
        App.getModel().addObserver(this);


        // Set start location parameters from previous map activity
        double[] currentLatLong = getIntent().getDoubleArrayExtra("currentLatLong");
        currentUserLoc = new UserLocation(currentLatLong[0], currentLatLong[1]);
        startLoc = new UserLocation(currentLatLong[0], currentLatLong[1]);
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    currentLatLong[0],
                    currentLatLong[1],
                    1 // represent max location result returned, recommended 1-5
            );
            currentUserLoc.setAddress(addresses.get(0).getAddressLine(0));
            startLoc.setAddress(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Instantiate TextViews/EditText
        startPointTextView = findViewById(R.id.startPointTextView);
        startPointTextView.setText("Set to your location: \n" + startLoc.getAddress());
        fareOfferingEditText = findViewById(R.id.fareOfferingEditText);
        endPointTextView = findViewById(R.id.endPointTextView);
        endPointTextView.setText("Where to?");

        // Instantiate submit button
        submitTripBtn = findViewById(R.id.submitTripBtn);

        // Disengage submission UI elements
        tripSubmissionLayout = findViewById(R.id.tripSubmissionLayout);
        tripSubmissionLayout.setVisibility(View.GONE);
    }


    public void handleClearStartPtBtn(View v) {
        startLoc = new UserLocation(currentUserLoc.getLatitude(), currentUserLoc.getLongitude());
        startLoc.setAddress(currentUserLoc.getAddress());
        if (endLoc == null) {
            tripSubmissionLayout.setVisibility(View.GONE);
        } else {
            recalculateFareOffering();
        }
        startPointTextView.setText("Set to your location: \n" + currentUserLoc.getAddress());
    }

    public void handleClearEndPtBtn(View v) {
        endLoc = null;
        tripSubmissionLayout.setVisibility(View.GONE);
        endPointTextView.setText("Where to?");
    }

    public void engagePlacePicker(View v) {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey(apiKey).setMapsApiKey(apiKey);

        // If you want to set a initial location rather then the current device location.
        // NOTE: enable_nearby_search MUST be true.z
        // builder.setLatLng(new LatLng(37.4219999, -122.0862462))

        try {
            Intent placeIntent = builder.build(this);
            startActivityForResult(placeIntent, REQUEST_PLACE_PICKER);
            choosingBtnID = v.getId();
        }
        catch (Exception ex) {
            // Google Play services is not available...
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_PLACE_PICKER) && (resultCode == RESULT_OK)) {
            Place place = PingPlacePicker.getPlace(data);
            if (place != null) {
                final LatLng latLng = place.getLatLng();

                switch (choosingBtnID) {
                    case R.id.selectStartPointBtn:
                        startPointTextView.setText(place.getAddress());
                        if (startLoc == null) {
                            startLoc = new UserLocation(latLng.latitude, latLng.longitude);
                            startLoc.setAddress(place.getAddress());
                        } else {
                            startLoc.setLatitude(latLng.latitude);
                            startLoc.setLongitude(latLng.longitude);
                            startLoc.setAddress(place.getAddress());
                        }
                        break;
                    case R.id.selectEndPointBtn:
                        endPointTextView.setText(place.getAddress());
                        if (endLoc == null) {
                            endLoc = new UserLocation(latLng.latitude, latLng.longitude);
                            endLoc.setAddress(place.getAddress());
                        } else {
                            endLoc.setLatitude(latLng.latitude);
                            endLoc.setLongitude(latLng.longitude);
                            endLoc.setAddress(place.getAddress());
                        }
                        break;
                }

                if (startLoc != null && endLoc != null) {
                    recalculateFareOffering();
                    tripSubmissionLayout.setVisibility(View.VISIBLE);
                } else {
                    tripSubmissionLayout.setVisibility(View.GONE);
                }
            }
        }
    }


    /**Used to recalculate ride fare*/
    private void recalculateFareOffering() {
        minimumFareOffering = startLoc.distancePriceEstimate(endLoc);
        riderFareOffering = minimumFareOffering;
        fareOfferingEditText.setText(Double.toString(riderFareOffering));
    }


    public void handleTripSubmitBtn(View v) {
        submitTripBtn.startAnimation();
        String username = App.getModel().getSessionUser().getUsername();
        double offeredFare = Double.valueOf(fareOfferingEditText.getText().toString().trim());
        if (offeredFare >= minimumFareOffering) {
            riderFareOffering = offeredFare;

            String riderID = App.getAuthDBManager().getCurrentUserID();
            tripRequest = new Trip(
                    riderID,
                    offeredFare,
                    startLoc,
                    endLoc,
                    username
            );
            App.getController().createNewTrip(tripRequest, this, submitTripBtn);
            Toast.makeText(getBaseContext(), "Submitting Trip...", Toast.LENGTH_SHORT).show();
        } else {
            // Remind user of minimum cost+
            recalculateFareOffering();
            fareOfferingEditText.setError(
                    "Please enter an amount higher than or equal to: \n" + minimumFareOffering);
        }
    }


    /**update function updates the activity
     * @param o,arg are the observable and the object*/
    @Override
    public void update(Observable o, Object arg) {
    }

    /**onDestroy closes the activity when it needs to be closed*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    /**onError handles errors in the TripBuilderActivity
     * @param e is the error called*/
    @Override
    public void onError(Error e) {
        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
