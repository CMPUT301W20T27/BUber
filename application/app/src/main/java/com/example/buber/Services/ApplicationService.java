package com.example.buber.Services;

import com.example.buber.App;
import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Account;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.Model.UserLocation;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

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

    /**
     * Calls the DBManger class to create a trip in Firebase. On success the listener returns
     * the trip object. On failure the listener returns the exception
     * @param tripRequest the trip object created
     * @param controllerListener the listener that gets results from the Firebase call.
     * @returns listener on successful/failed trip creation.
     */
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

    /**
     * Calls the DBManger class to create a user in Firebase. On success the listener gets
     * the trip object. On failure the listener returns the exception
     * @param username,password,firstName,lastName,email,phoneNumber,type The users information
     * @param controllerListener the listener that gets results from the Firebase call.
     */
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

    /**
     * Calls the AuthDBManager class to login a user. On success the listener returns
     * the driver or rider object. On failure the listener returns the exception
     * @param email,password,type The users information
     * @param controllerListener the listener that gets results from the Firebase call.

     */
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

    /**
     * Calls the AuthDBManager class to logout the user.
     */
    public static void logoutUser() {
        App.getAuthDBManager().signOut();
    }

    /**
     * Calls the DBManager class to get the trips filtered by geolocation. On success the listener returns
     * a list of filteredTripsData. On failure the listener returns the exception.
     * @param driverLocation the center location for the geosearch
     * @param controllerListener the listener that gets results from the Firebase call.
     */
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
                        if (distance <= RADIUS && t.getStatus() == Trip.STATUS.PENDING && !t.getRiderID().equals(currentUid)) {
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

    /**
     * Calls the DBManager class to get the Users current trip session. On success the listener returns
     * the trip. On failure the listener returns the exception.
     * @param controllerListener the listener that gets results from the Firebase call.
     */
    public static void getSessionTripForUser(EventCompletionListener controllerListener) {
        String userUID = App.getAuthDBManager().getCurrentUserID();
        User sessionUser = App.getModel().getSessionUser();
        CollectionReference tripsReference = App.getDbManager().getCollectionTrip();
        if (sessionUser != null) {
            if (sessionUser.getType() == RIDER) {
                // Get the trip by directly query the document
                App.getDbManager().getTrip(userUID, ((resultData, err) -> {
                    if (resultData.containsKey("trip") && err == null) {
                        controllerListener.onCompletion(resultData, null);
                    } else if (!resultData.containsKey("trip") && err == null) {
                        controllerListener.onCompletion(null, null);
                    } else {
                        controllerListener.onCompletion(null, null);
                    }
                }), true);
            } else {
                // Query trips db based on driverId
                tripsReference.whereEqualTo("driverID", userUID).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    if (docs.size() == 1) {
                        Trip sessionTrip = docs.get(0).toObject(Trip.class);
                        HashMap<String, Trip> res = new HashMap<>();
                        res.put("trip", sessionTrip);
                        controllerListener.onCompletion(res, null);
                    } else if (docs.size() == 0) {
                        // No current Trip
                        controllerListener.onCompletion(null, null);
                    } else {
                        // Throw an error, we got issues
                        controllerListener.onCompletion(null, new Error("Driver has more than 1 trip assigned"));
                    }
                }).addOnFailureListener(e -> {
                    controllerListener.onCompletion(null, new Error(e.getMessage()));
                });
            }
        }
    }


    /**
     * Calls the DBManager class to update the trip selected. On success the listener returns
     * the trip updates. On failure the listener returns the exception.
     * @param uid The document id of the trip
     * @param selectedTrip the trip object selected
     * @param controllerListener the listener that gets results from the Firebase call.
     */
    public static void selectTrip(String uid, Trip selectedTrip, EventCompletionListener controllerListener) {
        App.getDbManager().updateTrip(uid, selectedTrip, controllerListener, true);
    }

    /**
     * Calls the DBManager class to deletes current trip for rider. On success the listener returns.
     *  @param uid The document id of the trip to delete
     *  @param controllerListener the listener that gets results from the Firebase call.
     */
    public static void deleteRiderCurrentTrip(String uid, EventCompletionListener controllerListener) {
        App.getDbManager().deleteTrip(uid, controllerListener);
    }

    /**
     * Calls the DBManager class to fetch the correct last lodged in user from the correct collection. On success the listener returns the update.
     *  @param updateSessionUser The current logged in user
     *  @param listener the listener that gets results from the Firebase call.
     */
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

        if (tmpRider != null && uID != null) {
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
        } else {
            listener.onCompletion(null, new Error("Current session user does not exist"));
        }
    }

}