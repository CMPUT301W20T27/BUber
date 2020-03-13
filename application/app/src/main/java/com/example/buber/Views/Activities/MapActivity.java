package com.example.buber.Views.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.Model.UserLocation;
import com.example.buber.R;
import com.example.buber.Views.UIErrorHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.util.Observable;
import java.util.Observer;

import static com.example.buber.Model.User.TYPE.RIDER;

/**
 * Main Map activity. Activity uses similiar UI for Rider and Driver, but changes functionality based
 * on the type of user currently logged in (Rider, Driver). Activity links into the Google Maps
 * APi which is used to handle the current user location. Future iterations will include using
 * The sessionTrip object to display the route on the map and enable selecting start / end locations
 * TODO: MVC Updating and Error Handling.
 */
public class MapActivity extends AppCompatActivity implements Observer, OnMapReadyCallback, UIErrorHandler {

    // GOOGLE MAP STATE
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    private Location mLastKnownUserLocation;
    private static final float DEFAULT_ZOOM = 15;

    // TRIP STATE
    private Trip.STATUS currentTripStatus = null;

    // RIDER MAIN ACTION BUTTONS
    private Button riderRequestMainBtn;
    private Button riderRequestCancelMainBtn;

    // DRIVER MAIN ACTION BUTTONS
    private Button driverShowRequestsMainBtn;

    // SIDEBAR STATE
    private boolean showSideBar;
    private Button settingsButton;
    private View sideBarView;
    private Button statusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        if (googleConnectionSuccessful()) {
            getLocationPermission();
        } else {
            Toast.makeText(this,
                    "map connection failed", Toast.LENGTH_SHORT).show();
        }


        // INSTANTIATE SIDEBAR BUTTONS
        settingsButton = findViewById(R.id.settings_button);
        sideBarView = findViewById(R.id.sidebar);


        // INSTANTIATE RIDER MAIN ACTION BUTTONS
        riderRequestMainBtn = findViewById(R.id.rider_request_btn);
        riderRequestCancelMainBtn = findViewById(R.id.rider_request_cancel_btn);

        // INSTANTIATE DRIVER MAIN ACTION BUTTONS
        driverShowRequestsMainBtn = findViewById(R.id.driver_show_requests_btn);

        // INSTANTIATE STATUS BUTTON
        statusButton = findViewById(R.id.testButton);
        statusButton.setText("Ride Status");
        statusButton.setEnabled(false);
        // HIDE SIDEBAR
        hideSettingsPanel();

        App.getModel().addObserver(this);


        Trip sessionTrip = App.getModel().getSessionTrip();
        if (sessionTrip != null) {
            currentTripStatus = sessionTrip.getStatus();
            showActiveMainActionButton();
        }
    }

    /***** OBSERVING OBSERVABLES ******/
    @Override
    public void update(Observable o, Object arg) {
        Trip sessionTrip = App.getModel().getSessionTrip();
        if (sessionTrip == null) {
            this.setCurrentTripStatus(null);
            riderRequestMainBtn.setVisibility(View.VISIBLE);
            riderRequestCancelMainBtn.setVisibility(View.INVISIBLE);

        } else if (sessionTrip.getStatus() != currentTripStatus) {
            Toast.makeText(this, "Trip status changed to: " + sessionTrip.getStatus(), Toast.LENGTH_SHORT).show();
            this.setCurrentTripStatus(sessionTrip.getStatus());
        }
        if (sessionTrip != null && App.getModel().getSessionUser() != null) {
            showActiveMainActionButton();
        }
    }

    /***** MAIN ACTION BUTTON HANDLERS ******/
    public void handleRiderRequestBtn(View v) {
        Intent intent = new Intent(getBaseContext(), TripBuilderActivity.class);
        intent.putExtra(
                "currentLatLong",
                new double[]{mLastKnownUserLocation.getLatitude(), mLastKnownUserLocation.getLongitude()});
        startActivity(intent);
    }

    /**Handles user interaction with rider cancel button*/
    public void handleRiderCancelBtn(View v) {
        DialogInterface.OnClickListener dialogClickListener = ((DialogInterface dialog, int choice) -> {
            switch (choice) {
                case DialogInterface.BUTTON_POSITIVE:
                    Toast.makeText(MapActivity.this, "Cancelling trip...", Toast.LENGTH_SHORT).show();
                    ApplicationController.deleteRiderCurrentTrip(MapActivity.this);
                    statusButton.setEnabled(false);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cancel request?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    /**Handles interaction with driver show requests button*/
    public void handleDriverShowRequestsBtn(View v) {
        startActivity(new Intent(getBaseContext(), TripSearchActivity.class));
    }

    /**Shows active main action button when necessary*/
    public void showActiveMainActionButton() {
        hideMainActionButtons();

        if (this.currentTripStatus == null) {
            User sessionUser = App.getModel().getSessionUser();
            if (sessionUser.getType() == RIDER) {
                riderRequestMainBtn.setVisibility(View.VISIBLE);
            } else {
                driverShowRequestsMainBtn.setVisibility(View.VISIBLE);
            }
        } else {
            switch (this.currentTripStatus) {
                case PENDING:
                    riderRequestCancelMainBtn.setVisibility(View.VISIBLE);
                    statusButton.setEnabled(true);
                    break;
                case DRIVER_ACCEPT:
                    riderRequestCancelMainBtn.setVisibility(View.VISIBLE);
                    break;
                case DRIVER_PICKING_UP:
                    break;
                case EN_ROUTE:
                    break;
                case COMPLETED:
                    break;
            }
        }
    }

    /**Hides main action buttons when necessaru*/
    public void hideMainActionButtons() {
        riderRequestMainBtn.setVisibility(View.INVISIBLE);
        riderRequestCancelMainBtn.setVisibility(View.INVISIBLE);
        driverShowRequestsMainBtn.setVisibility(View.INVISIBLE);
    }

    /***** HANDLING SETTINGS PANEL BUTTONS ******/
    public void handleScreenClick(View v) {
        if (showSideBar) {
            hideSettingsPanel();
            showActiveMainActionButton();
        } else {
            // TODO: Handle Everything else
        }
    }

    public void handleTestClick(View v) {
        startActivity(new Intent(MapActivity.this, RequestStatusActivity.class));
    }

    /**Changes activity to EditAccountActivity when Account button is clicked*/
    public void handleAccountButtonClick(View v) {
        startActivity(new Intent(MapActivity.this, EditAccountActivity.class));
    }

    /**Logs user out of app when log out button is clicked*/
    public void handleLogoutButtonClick(View v) {
        User curUser = App.getModel().getSessionUser();
        curUser.setType(null);
        Log.d("APPSERVICE", "map logging out");
        App.getController().updateNonCriticalUserFields(curUser, this);

        App.getController().logout();
        startActivity(new Intent(MapActivity.this, LoginActivity.class));
        this.finish();
    }

    /**Shows settings sidebar panel when necessary*/
    public void showSettingsPanel(View v) {
        sideBarView.setVisibility(View.VISIBLE);
        settingsButton.setVisibility(View.INVISIBLE);
        showSideBar = true;

        hideMainActionButtons();
    }

    /**Hides settings sidebar panel when necessary*/
    public void hideSettingsPanel() {
        sideBarView.setVisibility(View.INVISIBLE);
        settingsButton.setVisibility(View.VISIBLE);
        showSideBar = false;

        showActiveMainActionButton();
    }


    /***** GETTING/SETTING LOCAL TRIP STATUS W.R.T. FB UPDATES *****/
    public Trip.STATUS getCurrentTripStatus() {
        return currentTripStatus;
    }

    public void setCurrentTripStatus(Trip.STATUS currentTripStatus) {
        this.currentTripStatus = currentTripStatus;
    }


    /***** DECLARING REST OF LIFECYCLE METHODS ******/
    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    @Override
    public void onError(Error e) {
        // TODO: Handle UI Errors
    }


    /***** BUILDING CUSTOM GOOGLE MAP (CODE REFERENCED FROM GOOGLE API DOCS) ******/
    public void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);  //calls onMapReady
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         * Code reference from Google API docs
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            initializeMap();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**Gets the location of the users device*/
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        FusedLocationProviderClient mFusedLocationProviderClient;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownUserLocation = (Location) task.getResult();
                        //TODO::fix controller lat and long

                        App.getController()
                                .updateUserLocation(
                                        new UserLocation(
                                                mLastKnownUserLocation.getLatitude(),
                                                mLastKnownUserLocation.getLongitude()
                                        )
                                );


                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownUserLocation.getLatitude(),
                                        mLastKnownUserLocation.getLongitude()), DEFAULT_ZOOM));

                    } else {
                        Log.d("NULLLOCATION", "Current location is null. Using defaults.");
                        Log.e("LOCATIONEXCEPTION", "Exception: %s", task.getException());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownUserLocation.getLatitude(),
                                        mLastKnownUserLocation.getLongitude()), DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**Returns true if connection to google api is successful*/
    boolean googleConnectionSuccessful() {
        int connection = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapActivity.this);
        if (connection == ConnectionResult.SUCCESS) {
            Log.d("MAPCONNECTIONSUCCESS", "Map connection Successful");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(connection)) {
            //for debugging. if an error occurs, print the error
            Log.e("MAPERROR", "an error occurred loading map");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapActivity.this, connection, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Log.e("MAPCONNECTIONFAILURE", "Map failed to connect");
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            Log.d("GETTING LOCATION", "Trying to get current location");
            getDeviceLocation();
        }

        mMap.setOnMapClickListener(latLng -> {
            if (showSideBar) {
                hideSettingsPanel();
                showActiveMainActionButton();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this code is copied from google map api documentation
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    initializeMap();
                    Log.d("LOCATIONPERMISSION", "UserLocation permission granted");
                } else {
                    Log.d("LOCATIONPERMISSION", "UserLocation permission denied");
                }
            }
        }
    }
}
