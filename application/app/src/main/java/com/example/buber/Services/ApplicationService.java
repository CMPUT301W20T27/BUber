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

import static com.example.buber.Model.User.TYPE.DRIVER;
import static com.example.buber.Model.User.TYPE.RIDER;


/**
 * Represents the service layer of our application. All interactions with database occur here
 * Handles communication between MVC frontend and database.
 * TODO: Better error handling and refactoring to improve cohesion.
 */
public class ApplicationService {
    private static final String TAG = "ApplicationService";

    /**Creates a new trip when called*/
    public static void createNewTrip(Trip tripRequest, EventCompletionListener controllerListener) {
        App
                .getDbManager()
                .createTrip(tripRequest,
                        (resultData, err) -> {
                            if (err != null) {
                                controllerListener.onCompletion(null, new Error(err.getMessage()));
                                return;
                            } else {
                                controllerListener.onCompletion(resultData, null);
                            }
                        }, true);
    }

    /**Creates a new user when called*/
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

    /**Logs user into app when called*/
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

    /**Logs user out of app when called*/
    public static void logoutUser() {
        App.getAuthDBManager().signOut();
    }

    /**Gets trips that are within a certain radius of the user*/
    public static void getFilteredTrips(UserLocation driverLocation, EventCompletionListener controllerListener) {
        Double RADIUS = 6.0; // TODO: Make this dynamic based on map bounds
        App.getDbManager().getTrips((resultData, err) -> {
            if (err != null) controllerListener.onCompletion(null, err);
            else {
                List<Trip> filterTrips = new LinkedList<>();
                List<Trip> tripData = (List<Trip>) resultData.get("all-trips");
                List<String> filterTripIds = new ArrayList<>();
                String currentUid = App.getAuthDBManager().getCurrentUserID();
                if (tripData != null && tripData.size() > 0) {
                    for (Trip t : tripData) {
                        double distance = driverLocation.distanceTo(t.getStartUserLocation());
                        if (distance <= RADIUS && t.getStatus() == Trip.STATUS.PENDING && t.getRiderID() != currentUid) {
                            filterTrips.add(t);
                            filterTripIds.add(t.getRiderID());
                        }
                    }

                    HashMap<String, List> filteredTripsData = new HashMap<>();
                    filteredTripsData.put("filtered-trips", filterTrips);
                    controllerListener.onCompletion(filteredTripsData, null);

                } else {
                    controllerListener.onCompletion(null, new Error("Could not find trips"));
                }
            }
        });
    }

    /**Gets rider users current trip location*/
    public static void riderCurrentTripUserLocation(EventCompletionListener controllerListener) {
        App.getDbManager().getTrips((resultData, err) -> {
            if (err != null) controllerListener.onCompletion(null, err);
            else {
                Trip filterTrips = new Trip();
                List<String> filterTripUserNames = new LinkedList<>();
                List<Trip> tripData = (List<Trip>) resultData.get("all-trips");
                if (tripData != null && tripData.size() > 0) {
                    for (Trip t : tripData) {
                        if (t.getRiderID().equals(App.getAuthDBManager().getCurrentUserID())) {
                            filterTrips = t;
                            break;
                        }
                    }

                    HashMap<String, Trip> filteredTripsData = new HashMap<>();
                    filteredTripsData.put("filtered-trips", filterTrips);

                    controllerListener.onCompletion(filteredTripsData, null);

                } else {
                    controllerListener.onCompletion(null, new Error("Could not find trips"));
                }
            }
        });
    }

    /**Selects a trip when called*/
    public static void selectTrip(String uid, Trip selectedTrip, EventCompletionListener controllerListener) {
        App.getDbManager().updateTrip(uid, selectedTrip, controllerListener, true);
    }

    /**deletes current trip for rider when called*/
    public static void deleteRiderCurrentTrip(String uid, EventCompletionListener controllerListener) {
        App.getDbManager().deleteTrip(uid, controllerListener);
    }

    /**Updates session user of the app as rider or driver depending on new user*/
    public static void updateUser(User updateSessionUser, EventCompletionListener listener) {
        String uID = App.getAuthDBManager().getCurrentUserID();
        Driver tmpDriver = new Driver(updateSessionUser.getUsername(),updateSessionUser.getAccount());
        Rider tmpRider = new Rider(updateSessionUser.getUsername(),updateSessionUser.getAccount());
        if(updateSessionUser.getType()==RIDER){
            tmpDriver.setLoggedOn(false);
            tmpRider.setRiderLoggedOn(true);
        }
        else if(updateSessionUser.getType()==DRIVER){
            tmpDriver.setLoggedOn(true);
            tmpRider.setRiderLoggedOn(false);
        }
        else{  //logging out
            tmpDriver.setLoggedOn(false);
            tmpRider.setRiderLoggedOn(false);
        }
        App.getDbManager().updateRider(uID, tmpRider, (resultData, err) -> {
            if (err == null) {
                App.getDbManager().updateDriver(uID,tmpDriver, (resultData1, err1) -> {
                    if (err1 == null) {
                        listener.onCompletion(null, null);
                    }
                });
            }
        });
        App.getDbManager().updateDriver(uID,tmpDriver, (resultData1, err1) -> {
            if (err1 == null) {
                listener.onCompletion(null, null);
            }
        });

    }

}