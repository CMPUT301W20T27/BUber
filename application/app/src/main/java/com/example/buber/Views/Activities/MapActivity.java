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

import static com.example.buber.Model.User.TYPE.DRIVER;
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

    /**onCreate method creates MapActivity when it is called
     * @param savedInstanceState is a previous saved state if applicable*/
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
        statusButton = findViewById(R.id.rideStatus);
        statusButton.setEnabled(false);

        // HIDE SIDEBAR
        hideSettingsPanel();

        App.getModel().addObserver(this);


        Trip sessionTrip = App.getModel().getSessionTrip();
        if (sessionTrip != null) {
            currentTripStatus = sessionTrip.getStatus();
            showActiveMainActionButton();
            statusButton.setEnabled(true);
        }
    }

    /***** OBSERVING OBSERVABLES ******/
    /**update method updates the activity when necessary
     * @param o,arg are instances of the observable and object*/
    @Override
    public void update(Observable o, Object arg) {
        Trip sessionTrip = App.getModel().getSessionTrip();
        User sessionUser = App.getModel().getSessionUser();
        if (sessionTrip == null) {
            this.setCurrentTripStatus(null);
        } else if (sessionTrip.getStatus() != currentTripStatus) {
            Toast.makeText(this, "Trip status changed to: " + sessionTrip.getStatus(), Toast.LENGTH_SHORT).show();
            this.setCurrentTripStatus(sessionTrip.getStatus());
        }
        if (sessionTrip != null && App.getModel().getSessionUser() != null) {
            if (sessionTrip.getStatus() == Trip.STATUS.DRIVER_ACCEPT
                    && sessionTrip.getStatus() != currentTripStatus
                    && sessionUser.getType() == DRIVER) {
                Toast.makeText(this, "Rider has canceled", Toast.LENGTH_SHORT).show();
            }

            // FOR DEMO PURPOSES ONLY (will be removed on project completion)
            if (sessionTrip.getStatus() == Trip.STATUS.DRIVER_ACCEPT) {
                ApplicationController.deleteRiderCurrentTrip(MapActivity.this);
                // TODO: part 3 cuts off here
                Toast
                        .makeText(
                                this,
                                "Driver has accepted! \n Cancelling trip and resetting \n...functionality to completed",
                                Toast.LENGTH_LONG)
                        .show();
            }
        }
        showActiveMainActionButton();
    }

    /***** MAIN ACTION BUTTON HANDLERS ******/
    /**handleRiderRequestBtn handles user interaction with the rider request button
     * @param v is an instance of the view*/
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

    /**Handles interaction with driver show requests button
     * @param v is the view instance*/
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
            } else if(sessionUser.getType() == DRIVER){
                driverShowRequestsMainBtn.setVisibility(View.VISIBLE);
            }
            statusButton.setEnabled(false);
        } else {
            switch (this.currentTripStatus) {
                case PENDING:
                    riderRequestCancelMainBtn.setVisibility(View.VISIBLE);
                    statusButton.setEnabled(true);
                    break;
                case DRIVER_ACCEPT:
                    // TODO: part 3 cuts off here
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
    /**handleScreenClick handles what happens when user clicks on the screen
     * @param v is the view instance*/
    public void handleScreenClick(View v) {
        if (showSideBar) {
            hideSettingsPanel();
            showActiveMainActionButton();
        } else {
            // TODO: Handle Everything else
        }
    }

    /**Handles click on test
     * @param v is the view instance*/
    public void handleTestClick(View v) {
        startActivity(new Intent(MapActivity.this, RequestStatusActivity.class));
    }

    /**Changes activity to EditAccountActivity when Account button is clicked*/
    public void handleAccountButtonClick(View v) {
        startActivity(new Intent(MapActivity.this, EditAccountActivity.class));
        hideSettingsPanel();
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

    /**setCurrentTripStatus will set the trip status depending on whether it has been taken or not
     * @param currentTripStatus is the status of current trip*/
    public void setCurrentTripStatus(Trip.STATUS currentTripStatus) {
        this.currentTripStatus = currentTripStatus;
    }


    /***** DECLARING REST OF LIFECYCLE METHODS ******/
    /**onDestroy method destructs activity if it is closed down*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    /**onError handles UI Errors if there are any*/
    @Override
    public void onError(Error e) {
        // TODO: Handle UI Errors
    }


    /***** BUILDING CUSTOM GOOGLE MAP (CODE REFERENCED FROM GOOGLE API DOCS) ******/
    public void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);  //calls onMapReady
    }

    /**getLocationPermission gets location permission from the user*/
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

    /**onMapReady lets the user know that the location is being gotten if permissions are granted
     * @param googleMap is the map instance of GoogleMap*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            Log.d("GETTING LOCATION", "Trying to get current location");
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapClickListener(latLng -> {
            if (showSideBar) {
                hideSettingsPanel();
                showActiveMainActionButton();
            }
        });
    }

    /**onRequestPermissionsResult gives the result of of the user permissions request
     * @param requestCode is the result of the request
     * @param permissions is a list of the permissions
     * @param grantResults is a list of results granted*/
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
