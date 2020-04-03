package com.example.buber.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.Model.UserLocation;
import com.example.buber.Services.ApplicationService;
import com.example.buber.Views.Activities.LoginActivity;
import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.example.buber.Views.Activities.RatingActivity;
import com.example.buber.Views.UIErrorHandler;

import java.util.List;
import java.util.Observer;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

import static com.example.buber.Model.User.TYPE.RIDER;

/**
 * ApplicationController handles the interaction between our service layer and the model.
 * It also handles all our updates to the application controller and bubbles up errors for our
 * UI views to display.
 * TODO: Better error handling and refactoring to improve cohesion.
 */
public class ApplicationController {
    private ApplicationModel model;

    public ApplicationController(ApplicationModel model) {
        this.model = model;
    }

    /**
     * Calls the  ApplicationService class to create a user. On success the listener gets
     * the user object. On failure the  error listener returns the exception. And updates the model
     * @param username,password,firstName,lastName,email,phoneNumber,type The users information
     * @param view the UI Error Handler interface callback.
     */
    public void createNewUser(String username,
                              String password,
                              String firstName,
                              String lastName,
                              String email,
                              String phoneNumber,
                              User.TYPE type,
                              UIErrorHandler view) {
        ApplicationService.createNewUser(
                username,
                password,
                firstName,
                lastName,
                email,
                phoneNumber,
                type,
                (resultData, err) -> {
                    if (err != null) view.onError(err);
                    else {
                        User u = (User) resultData.get("user");
                        model.setSessionUser(u);
                        view.finish();
                    }
                }
        );
    }
    /**
     * Calls the  ApplicationService class to login a user. On success the listener gets
     * the user object. On failure the  error listener returns the exception. And updates the model
     * @param email,password,type The users information
     * @param view the UI Error Handler interface callback.
     * @param intent the activity that will open on successful login
     */
    public void login(String email, String password, User.TYPE type, LoginActivity view, Intent intent) {
        ApplicationService.loginUser(email, password, type, (resultData, err) -> {
            if (err != null) view.onError(err);
            else {
                User u = (User) resultData.get("user");
                updateNonCriticalUserFields(true, u, type, view);
                Toast.makeText(view.getApplicationContext(), "You are NOW logged in.", Toast.LENGTH_SHORT).show();
                u.setType(type);
                model.setSessionUser(u);
                ApplicationController.loadSessionTrip(intent, view);
            }
        });
    }

    /**
     * Calls the  ApplicationService class to create a trip. On success the listener gets
     * the trip object. On failure the  error listener returns the exception. And updates the model
     * @param tripRequest The created trips object
     * @param view the UI Error Handler interface callback.
     */
    public void createNewTrip(Trip tripRequest,
                              UIErrorHandler view,
                              CircularProgressButton submitTripBtn) {
        ApplicationService.createNewTrip(tripRequest,
                (resultData, err) -> {
                    if (err != null) view.onError(err);
                    else {
                        Trip tripData =  (Trip) resultData.get("trip");
                        model.setSessionTrip(tripData);
                        view.finish();
                        submitTripBtn.stopAnimation();
                    }
                }
        );
    }

    /**
     *  Updates the users location in the model and calls notifyObservers() in the ApplicationModel
     *  @param location the users new location
     */
    public void updateUserLocation(UserLocation location) {
        ApplicationModel m = App.getModel();
        if (m.getSessionUser() !=  null) {
            m.getSessionUser().setCurrentUserLocation(location);
            m.notifyObservers();
        }
    }

    /**
     *  Gets the all the trips for the user. And updates the model with the trip list.
     *   @param view the UI Error Handler interface callback.
     */
    public static void getTripsForUser(UIErrorHandler view) {
        ApplicationModel m = App.getModel();
        UserLocation sessionUserLocation = m.getSessionUser().getCurrentUserLocation();
        ApplicationService.getFilteredTrips(sessionUserLocation, (resultData, err) -> {
            if (err != null && view != null) {
                view.onError(err);
            } else {
                List<Trip> sessionTripList = (List<Trip>) resultData.get("filtered-trips");
                m.setSessionTripList(sessionTripList);
            }
        });
    }

    /**
     *  Gets the all the trips for the user. And updates the model with the trip list.
     *   @param view the UI Error Handler interface callback.
     */
    public static void getPendingTripsForDriver(UIErrorHandler view) {
        ApplicationModel m = App.getModel();
        ApplicationService.getFilteredPendingTripsForDriver((resultData, err) -> {
            if (err != null && view != null) {
                view.onError(err);
            } else {
                List<Trip> driverAcceptedPendingRides = (List<Trip>) resultData.get("filtered-trips");
                m.setDriverAcceptedPendingRides(driverAcceptedPendingRides);
            }
        });
    }


    /**
     * Updates the model to hold the riders current trip request. On success start the new activity. On failure send exception to MainActivity
     * @param completionIntent Start the MapActivity  after successful retrieval of session trip
     * @param view the MainActivity view
     */
    public static void loadSessionTrip(Intent completionIntent, MainActivity view){
        ApplicationModel m = App.getModel();
        ApplicationService.getSessionTripForUser((resultData, err) -> {
            if (err != null) view.onError(err);
            else {
                if (resultData != null && resultData.containsKey("trip")) {
                    Trip sessionTrip = (Trip) resultData.get("trip");
                    m.setSessionTrip(sessionTrip);
                }
                view.startActivity(completionIntent);
                view.finish();
            }
        });
    }

    /**
     * Updates the model to hold the riders current trip request. On success start the new activity. On failure send exception to LoginActivity
     * @param completionIntent Start the MapActivity  after successful retrieval of session trip
     * @param view the LoginActivity view
     */
    public static void loadSessionTrip(Intent completionIntent, LoginActivity view){
        ApplicationModel m = App.getModel();
        ApplicationService.getSessionTripForUser((resultData, err) -> {
            if (err != null) view.onError(err);
            else {
                if (resultData != null && resultData.containsKey("trip")) {
                    Trip sessionTrip = (Trip) resultData.get("trip");
                    m.setSessionTrip(sessionTrip);
                }
                view.startActivity(completionIntent);
                view.finish();
            }
        });
    }

    /**
     * Updates the model session trip to null and deletes the current trip request and trip listeners.
     * @param view the UI Error Handler interface callback.
     */
    public static void deleteRiderCurrentTrip(UIErrorHandler view){
        ApplicationModel m = App.getModel();
        ApplicationService.deleteCurrentTrip(m.getSessionTrip(), (resultData, err) -> {
            if (err != null) view.onError(err);
        });
    }

    /**
     * Gets the driver user's selected trip and changes the Trip status in Firebase
     * @param selectedTrip the drivers selected trip
     */
    public static void handleDriverTripSelect(Trip selectedTrip) {
        ApplicationModel m = App.getModel();
        selectedTrip.setStatus(Trip.STATUS.DRIVER_ACCEPT);
        String userId = App.getAuthDBManager().getCurrentUserID();
        selectedTrip.setDriverID(userId);
        ApplicationService.selectTrip(selectedTrip.getRiderID(), selectedTrip, ((resultData, err) -> {
            if (err != null) {
                List<Observer> mapObservers = m.getObserversMatchingClass(MapActivity.class);
                for (Observer map : mapObservers) {
                    ((UIErrorHandler) map).onError(err);
                }
            } else {
                // Edge case: don't override current active trip if there is one
                if (m.getSessionTrip() == null) {
                    m.setSessionTrip(selectedTrip);
                    m.setSessionTripList(null);
                }
            }
        }));
    }

    /** Controls what happens after rider accept ride offer **/
    public static void handleNotifyDriverForPickup() {
        Trip currentTrip = App.getModel().getSessionTrip();
        ApplicationService.notifyDriverForPickup(currentTrip.getRiderID(), currentTrip, ((resultData, err) -> {
            if (err != null) {
                List<Observer> mapObservers = App.getModel().getObserversMatchingClass(MapActivity.class);
                for (Observer map : mapObservers) {
                    ((UIErrorHandler) map).onError(err);
                }
            }
        }));
    }

    /** Controls what happens after rider accepts thr ride offer **/
    public static void handleNotifyRiderForPickup() {
        Trip currentTrip = App.getModel().getSessionTrip();
        ApplicationService.notifyRiderForPickup(currentTrip.getRiderID(), currentTrip, ((resultData, err) -> {
            if (err != null) {
                List<Observer> mapObservers = App.getModel().getObserversMatchingClass(MapActivity.class);
                for (Observer map : mapObservers) {
                    ((UIErrorHandler) map).onError(err);
                }
            }
        }));
    }

    /** Controls what happens after rider accept ride offer **/
    public static void beginTrip() {
        Trip currentTrip = App.getModel().getSessionTrip();
        currentTrip.setStatus(Trip.STATUS.EN_ROUTE);
        ApplicationService.beginTrip(currentTrip.getRiderID(), currentTrip, ((resultData, err) -> {
            if (err != null) {
                List<Observer> mapObservers = App.getModel().getObserversMatchingClass(MapActivity.class);
                for (Observer map : mapObservers) {
                    ((UIErrorHandler) map).onError(err);
                }
            } else {
                App.getModel().setSessionTrip(currentTrip);
            }
        }));
    }

    /** Complete trip **/
    public static void completeTrip() {
        Trip currentTrip = App.getModel().getSessionTrip();
        ApplicationService.completeTrip(currentTrip.getRiderID(), currentTrip, ((resultData, err) -> {
            if (err != null) {
                List<Observer> mapObservers = App.getModel().getObserversMatchingClass(MapActivity.class);
                for (Observer map : mapObservers) {
                    ((UIErrorHandler) map).onError(err);
                }
            }
        }));
    }

    /**
     * manageLoggedStateAcrossTwoUserCollections
     * @param updatedSessionUser the updated user object
     * @param view the UI Error Handler interface callback.
     */
    /**/
    public static void manageLoggedStateAcrossTwoUserCollections(boolean loggingIn,
                                                   User updatedSessionUser,
                                                   User.TYPE userType,
                                                   UIErrorHandler view) {
        ApplicationService.manageLoggedStateAcrossTwoUserCollections(
                loggingIn,
                updatedSessionUser,
                userType,
                ((resultData, err) -> {
                    if (err == null) {
                        App.getModel().setSessionUser(updatedSessionUser);
                        view.finish();
                    } else {
                        // TODO: Handle Errors
                    }
                }));
    }

    /**
     * This method is called when logging in(to update the login boolean) or when editing account
     * @param loggingIn login boolean to be updated
     * @param updatedSessionUser
     * @param userType
     * @param view
     */
    public static void updateNonCriticalUserFields(boolean loggingIn,
                                                   User updatedSessionUser,
                                                   User.TYPE userType,
                                                   UIErrorHandler view) {
        ApplicationService.manageLoggedStateAcrossTwoUserCollections(
                loggingIn,
                updatedSessionUser,
                userType,
                ((resultData, err) -> {
                    if (err == null) {
                        App.getModel().setSessionUser(updatedSessionUser);
                        view.finish();
                    } else {
                        // TODO: Handle Errors
                    }
                }));
    }
    /**Used to update non critical user fields (ie. username, first/last name, phone number) when
     * they are edited by user*/
    public static void editAccountUpdate(User updatedSessionUser, UIErrorHandler view){
            ApplicationService.updateUser(updatedSessionUser,((resultData, err) -> {
                if (err == null) {
                    App.getModel().setSessionUser(updatedSessionUser);
                } else {
                    // TODO: Handle Errors
                }
            }));

    }

    /**
     * Called from the rating activity. Takes either a thumbs up or thumbs down and adjusts the
     * rating in the db.
     * @param view is the rating activity at which this method is called
     * @param driverID  is the driver that needs to be updated
     * @param giveThumbsUp  boolean for if thumbs up was pressed or not
     */
    public static void updateDriverRating(RatingActivity view, String driverID, boolean giveThumbsUp){
        App.getDbManager().getDriver(driverID, ((resultData, err) -> {
            if (err == null) {
                Driver tmpDriver = (Driver) resultData.get("user");
                if(giveThumbsUp){
                    tmpDriver.setNumThumbsUp(tmpDriver.getNumThumbsUp() + 1);
                }
                else{
                    tmpDriver.setNumThumbsDown(tmpDriver.getNumThumbsDown()+1);
                }
                //Rating algorithm
                tmpDriver.setRating((tmpDriver.getNumThumbsUp() / (tmpDriver.getNumThumbsDown() +
                        tmpDriver.getNumThumbsUp())) * 100);
                App.getDbManager().updateDriver(driverID,tmpDriver, (resultData1, err1) -> {
                    if (err1 != null) {
                        view.finish();
                    }
                });
            }

        }));
    }

    public static void handleViewContactInformation(Activity view, Intent contactIntent, String riderID, String driverID) {
        if (App.getModel().getSessionUser().getType() == RIDER) {
            App.getDbManager().getDriver(driverID, ((resultData, err) -> {
                if (err == null) {
                    Driver d = (Driver) resultData.get("user");
                    contactIntent.putExtra("ID", d.getDocID());
                    contactIntent.putExtra("username", d.getUsername());
                    contactIntent.putExtra("email", d.getAccount().getEmail());
                    contactIntent.putExtra("phoneNumber", d.getAccount().getPhoneNumber());
                    view.startActivity(contactIntent);
                }

            }));
        } else {
            App.getDbManager().getRider(riderID, ((resultData, err) -> {
                if (err == null) {
                    Rider r = (Rider) resultData.get("user");
                    contactIntent.putExtra("ID", r.getDocID());
                    contactIntent.putExtra("username", r.getUsername());
                    contactIntent.putExtra("email", r.getAccount().getEmail());
                    contactIntent.putExtra("phoneNumber", r.getAccount().getPhoneNumber());
                    view.startActivity(contactIntent);
                }
            }));
        }
    }
}
