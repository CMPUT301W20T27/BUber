package com.example.buber.Services;

import com.example.buber.App;
import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Account;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.Model.UserLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ApplicationService {
    private static final String TAG = "ApplicationService";

    public static void createNewTrip(Trip tripRequest, EventCompletionListener controllerListener) {
        App
                .getDbManager()
                .createTrip(tripRequest,
                        (resultData, err) -> {
                            if (err != null) {
                                controllerListener.onCompletion(null, new Error(err.getMessage()));
                                return;
                            }
                        });
    }

    public static void createNewUser(
            String username,
            String password,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            User.TYPE type,
            EventCompletionListener controllerListener
    ) {

        Account newUserAccount = new Account(firstName, lastName, email, phoneNumber);
        App.getAuthDBManager().createFirebaseUser(email, password, (resultData, err) -> {
            if (err != null) {
                controllerListener.onCompletion(null, new Error(err.getMessage()));
                return;
            }
            String docID = (String) resultData.get("doc-id");
            // Right now, we just return a rider object, this should change if we provide the
            // option to login as both
            App.getDbManager().createRider(docID, new Rider(username, newUserAccount), controllerListener);
            App.getDbManager().createDriver(docID, new Driver(username, newUserAccount), ((resultData1, err1) -> {}));

        });

    }

    public static void loginUser(String email,
                                 String password,
                                 User.TYPE type,
                                 EventCompletionListener controllerListener) {
       App.getAuthDBManager().signIn(email, password, (resultData, err) -> {
           if (err != null) {
               controllerListener.onCompletion(null, new Error(err.getMessage()));
               return;
           }
           String docID = (String) resultData.get("doc-id");
           if (type == User.TYPE.DRIVER) {
               App.getDbManager().getDriver(docID, controllerListener);
           } else {
               App.getDbManager().getRider(docID, controllerListener);
           }
       });
    }

    public static void logoutUser() {
        App.getAuthDBManager().signOut();
    }

    public static void getFilteredTrips(UserLocation driverLocation, EventCompletionListener controllerListener) {
        Double RADIUS = 6.0; // TODO: Make this dynamic based on map bounds
        App.getDbManager().getTrips((resultData, err) -> {
            if (err != null) controllerListener.onCompletion(null, err);
            else {
                List<Trip> filterTrips = new LinkedList<>();
                List<String> filterTripUserNames = new LinkedList<>();
                List<Trip> tripData = (List<Trip>) resultData.get("all-trips");
                List<String> filterTripIds = new ArrayList<>();
                if (tripData != null && tripData.size() > 0) {
                    for (Trip t : tripData) {
                        double distance = driverLocation.distanceTo(t.getStartUserLocation());
                        if (distance <= RADIUS && t.getStatus() == Trip.STATUS.PENDING) {
                            filterTrips.add(t);
                            filterTripIds.add(t.getRiderID());
                        }
                    }

                    HashMap<String, List> filteredTripsData = new HashMap<>();
                    filteredTripsData.put("filtered-trips", filterTrips);
                    filteredTripsData.put("filter-trips-usernames", filterTripUserNames);
                    controllerListener.onCompletion(filteredTripsData, null);

                } else {
                    controllerListener.onCompletion(null, new Error("Could not find trips"));
                }
            }
        });
    }

    public static void updateTripStatus(String uid, Trip selectedTrip, EventCompletionListener controllerListener) {
        App.getDbManager().updateTrip(uid, selectedTrip, controllerListener);
    }
    public static void updateUser(User updateSessionUser, EventCompletionListener listener) {
        String uID = App.getAuthDBManager().getCurrentUserID();
        Driver tmpDriver = new Driver(updateSessionUser.getUsername(),updateSessionUser.getAccount());
        Rider tmpRider = new Rider(updateSessionUser.getUsername(),updateSessionUser.getAccount());
        App.getDbManager().updateRider(uID, tmpRider, (resultData, err) -> {
            if (err == null) {
                App.getDbManager().updateDriver(uID,tmpDriver, (resultData1, err1) -> {
                    if (err1 == null) {
                        listener.onCompletion(null, null);
                    }
                });
            }
        });
    }

}