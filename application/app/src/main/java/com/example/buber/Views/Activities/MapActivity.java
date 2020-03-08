package com.example.buber.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
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

public class MapActivity extends AppCompatActivity implements Observer, OnMapReadyCallback, UIErrorHandler {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownUserLocation;
    private static final float DEFAULT_ZOOM = 15;

    // Views
    private Button settingsButton;
    private Button accountButton;
    private View sideBarView;
    private Button riderRequestButton;

    // State
    private boolean showSideBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("map","in map");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        if(googleConnectionSuccessful()){
            getLocationPermission();
        }
        else{
            Toast.makeText(this,
                    "map connection failed",Toast.LENGTH_SHORT).show();
        }

        settingsButton = findViewById(R.id.settings_button);
        accountButton = findViewById(R.id.account_button);
        sideBarView = findViewById(R.id.sidebar);
        riderRequestButton = findViewById(R.id.rider_request_button);

        sideBarView.setVisibility(View.INVISIBLE);
        settingsButton.setVisibility(View.VISIBLE);
        riderRequestButton.setVisibility(View.VISIBLE);
        showSideBar = false;

        App.getModel().addObserver(this);

        riderRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TripSearchActivity.class);
                startActivity(intent);
            }
        });


    }

    public void initializeMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);  //calls onMapReady
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO: Update Map View
    }

    /**
    this code is copied from google map api documentation
     In order to get current location at runtime, permissions must be fulfilled
     **/
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
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

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownUserLocation =  (Location) task.getResult();
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
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     //this code is copied from google map api documentation
     **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    initializeMap();
                    Log.d("LOCATIONPERMISSION","UserLocation permission granted");
                }
                else{
                    Log.d("LOCATIONPERMISSION","UserLocation permission denied");
                }
            }
        }
    }

    boolean googleConnectionSuccessful(){
        int connection = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapActivity.this);
        if(connection == ConnectionResult.SUCCESS){
            Log.d("MAPCONNECTIONSUCCESS","Map connection Successful");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(connection)){
            //for debugging. if an error occurs, print the error
            Log.d("MAPERROR","an error occurred loading map");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapActivity.this,connection,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Log.d("MAPCONNECTIONFAILURE","Map failed to connect");
        }
        return false;
    }


    /* CLICK HANLDERS */
    public void handleSettingsButtonClick(View v) {
        sideBarView.setVisibility(View.VISIBLE);
        settingsButton.setVisibility(View.INVISIBLE);
        riderRequestButton.setVisibility(View.INVISIBLE);
        showSideBar = true;
    }

    public void handleAccountButtonClick(View v) {
        startActivity(new Intent(MapActivity.this, EditAccountActivity.class));
    }

    public void handleLogoutButtonClick(View v) {
        App.getController().logout();
        startActivity(new Intent(MapActivity.this, LoginActivity.class));
        this.finish();
    }


    public void handleScreenClick(View v) {
        if (showSideBar) {
            showSideBar = false;
            sideBarView.setVisibility(View.INVISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
            riderRequestButton.setVisibility(View.VISIBLE);
        } else {
            // TODO: Handle Everything else
        }
    }

    public void handleRideRequestClick(View v) {
        Intent tripBuilderIntent = new Intent(MapActivity.this, TripBuilderActivity.class);
        tripBuilderIntent.putExtra(
                "currentLatLong",
                new double[] {mLastKnownUserLocation.getLatitude(), mLastKnownUserLocation.getLongitude()});
        startActivity(tripBuilderIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mLocationPermissionGranted){
            Log.d("GETTING LOCATION","Trying to get current location");
            getDeviceLocation();
        }

        mMap.setOnMapClickListener(latLng -> {
            if (showSideBar) {
                sideBarView.setVisibility(View.INVISIBLE);
                riderRequestButton.setVisibility(View.VISIBLE);
                settingsButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onError(Error e) {
        // TODO: Handle UI Errors
    }
}
