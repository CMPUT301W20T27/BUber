package com.example.buber.Views.Components;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.Activities.EditAccountActivity;
import com.example.buber.Views.Activities.LoginActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.example.buber.Views.Activities.RequestStatusActivity;
import com.example.buber.Views.Activities.TripBuilderActivity;
import com.example.buber.Views.Activities.TripSearchActivity;

import static com.example.buber.Model.User.TYPE.DRIVER;
import static com.example.buber.Model.User.TYPE.RIDER;

public class BUberMapUIAddOnsManager {
    private MapActivity map;

    // RIDER MAIN ACTION BUTTON(s)
    private Button riderRequestMainBtn;
    private Button riderRequestCancelMainBtn;
    private Button riderCancelPickupBtn;

    // DRIVER MAIN ACTION BUTTON(s)
    private Button driverShowRequestsMainBtn;

    // STATUS BUTTON(s)
    private Button statusButton;

    // SIDEBAR BUTTONS
    private Button settingsButton;
    private Button accountButton;
    private Button logoutButton;

    // SIDEBAR STATE
    private boolean showSideBar;
    private View sideBarView;


    public BUberMapUIAddOnsManager(MapActivity map) {
        this.map = map;

        // INSTANTIATE RIDER MAIN ACTION BUTTONS
        this.riderRequestMainBtn = map.findViewById(R.id.rider_request_btn);
        this.riderRequestCancelMainBtn = map.findViewById(R.id.rider_request_cancel_btn);
        this.riderCancelPickupBtn = map.findViewById(R.id.rider_cancel_pickup_btn);

        // INSTANTIATE DRIVER MAIN ACTION BUTTONS
        this.driverShowRequestsMainBtn = map.findViewById(R.id.driver_show_requests_btn);

        // INSTANTIATE STATUS BUTTON
        statusButton = map.findViewById(R.id.rideStatus);

        // INSTANTIATE SIDEBAR BUTTONS
        settingsButton = map.findViewById(R.id.settings_button);
        accountButton = map.findViewById(R.id.account_button);
        logoutButton = map.findViewById(R.id.logout_button);

        // SIDEBAR STATE
        sideBarView = map.findViewById(R.id.sidebar);

        hideSettingsPanel();
        setMapButtonOnClickListeners();
    }

    private void setMapButtonOnClickListeners() {
        /**
         * handleRiderRequestBtn handles user interaction with the rider request button
         *
         * @param v is an instance of the view
         */
        riderRequestMainBtn.setOnClickListener((View v) -> {
            Intent intent = new Intent(map, TripBuilderActivity.class);
            intent.putExtra(
                    "currentLatLong",
                    new double[]{
                            map.mLastKnownUserLocation.getLatitude(),
                            map.mLastKnownUserLocation.getLongitude()});
            map.startActivity(intent);
        });

        /** Handles user interaction with rider cancel button **/
        riderRequestCancelMainBtn.setOnClickListener((View v) -> {
            DialogInterface.OnClickListener dialogClickListener = ((DialogInterface dialog, int choice) -> {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Toast.makeText(map, "Cancelling trip...", Toast.LENGTH_SHORT).show();
                        ApplicationController.deleteRiderCurrentTrip(map);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(map);
            builder.setMessage("Cancel request?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        });

        /** Handles user interaction with rider cancel pickup button **/
        riderCancelPickupBtn.setOnClickListener((View v) -> {
            Toast.makeText(map, "Cancelling trip...", Toast.LENGTH_SHORT).show();
            ApplicationController.deleteRiderCurrentTrip(map);
        });


        /**
         * Handles interaction with driver show requests button
         *
         * @param v is the view instance
         */
        driverShowRequestsMainBtn.setOnClickListener((View v) -> {
            map.startActivity(new Intent(map, TripSearchActivity.class));
        });


        /** Shows settings sidebar panel when necessary **/
        settingsButton.setOnClickListener((View v) -> {
            sideBarView.setVisibility(View.VISIBLE);
            settingsButton.setVisibility(View.INVISIBLE);
            setShowSideBar(true);

            hideMainActionButtons();
        });


        /**
         * Handles click on test
         *
         * @param v is the view instance
         */
        statusButton.setOnClickListener((View v) -> {
            map.startActivity(new Intent(map, RequestStatusActivity.class));
        });


        /** Changes activity to EditAccountActivity when Account button is clicked **/
        accountButton.setOnClickListener((View v) -> {
            map.startActivity(new Intent(map, EditAccountActivity.class));
            hideSettingsPanel();
        });


        /** Logs user out of app when log out button is clicked **/
        logoutButton.setOnClickListener((View v) -> {
            User curUser = App.getModel().getSessionUser();
            curUser.setType(null);
            Log.d("APPSERVICE", "map logging out");
            App.getController().updateNonCriticalUserFields(curUser, map);

            App.getController().logout();
            map.startActivity(new Intent(map, LoginActivity.class));
            map.finish();
        });
    }


    /**
     * Shows active main action button when necessary
     */
    public void showActiveMainActionButton() {
        hideMainActionButtons();
        User.TYPE currentUserType = App.getModel().getSessionUser().getType();

        if (map.currentTripStatus == null) {
            hideStatusButton();

            if (currentUserType == RIDER) {
                riderRequestMainBtn.setVisibility(View.VISIBLE);
            } else if (currentUserType == DRIVER) {
                driverShowRequestsMainBtn.setVisibility(View.VISIBLE);
            }

        } else {
            showStatusButton();

            switch (map.currentTripStatus) {
                case PENDING:
                    if (currentUserType == RIDER) {
                        riderRequestCancelMainBtn.setVisibility(View.VISIBLE);
                    }
                    break;
                case DRIVER_ACCEPT:
                    if (currentUserType == RIDER) {
                        handleRiderOfferAccept();
                    }
                    break;
                case DRIVER_PICKING_UP:
                    if (currentUserType == RIDER) {
                        riderCancelPickupBtn.setVisibility(View.VISIBLE);
                    }
                    break;
                case EN_ROUTE:
                    break;
                case COMPLETED:
                    break;
            }
        }
    }

    /**
     * Handles user interaction with rider accept button
     */
    public void handleRiderOfferAccept() {
        if (map.currentTripStatus != Trip.STATUS.DRIVER_ACCEPT) {
            return;
        }
        DialogInterface.OnClickListener dialogClickListener = ((DialogInterface dialog, int choice) -> {
            switch (choice) {
                case DialogInterface.BUTTON_POSITIVE:
                    ApplicationController.handleNotifyDriverForPickup();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Toast.makeText(map, "Cancelling trip...", Toast.LENGTH_SHORT).show();
                    ApplicationController.deleteRiderCurrentTrip(map);
                    break;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(map);
        builder.setMessage("A driver has accepted! Proceed?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    /**
     * Hides main action buttons when necessary
     */
    public void hideMainActionButtons() {
        riderRequestMainBtn.setVisibility(View.INVISIBLE);
        riderRequestCancelMainBtn.setVisibility(View.INVISIBLE);
        riderCancelPickupBtn.setVisibility(View.INVISIBLE);

        driverShowRequestsMainBtn.setVisibility(View.INVISIBLE);
    }


    /**
     * Hides settings sidebar panel when necessary
     */
    public void hideSettingsPanel() {
        sideBarView.setVisibility(View.INVISIBLE);
        settingsButton.setVisibility(View.VISIBLE);
        setShowSideBar(false);

        showActiveMainActionButton();
    }

    public void showStatusButton() {
        statusButton.setVisibility(View.VISIBLE);
    }

    public void hideStatusButton() {
        statusButton.setVisibility(View.GONE);
    }

    public boolean isShowSideBar() {
        return showSideBar;
    }

    public void setShowSideBar(boolean showSideBar) {
        this.showSideBar = showSideBar;
    }
}
