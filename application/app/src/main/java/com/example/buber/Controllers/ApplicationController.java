package com.example.buber.Controllers;

import android.content.Intent;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.Model.UserLocation;
import com.example.buber.Services.ApplicationService;
import com.example.buber.Views.Activities.LoginActivity;
import com.example.buber.Views.Activities.MainActivity;
import com.example.buber.Views.Activities.MapActivity;
import com.example.buber.Views.UIErrorHandler;

import java.util.List;
import java.util.Observer;

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
     * Calls the  ApplicationService class to create a trip. On success the listener gets
     * the trip object. On failure the  error listener returns the exception. And updates the model
     * @param tripRequest The created trips object
     * @param view the UI Error Handler interface callback.
     */

    public void createNewTrip(Trip tripRequest,
                              UIErrorHandler view) {
        ApplicationService.createNewTrip(tripRequest,
                (resultData, err) -> {
                    if (err != null) view.onError(err);
                    else {
                        Trip tripData =  (Trip) resultData.get("trip");
                        model.setSessionTrip(tripData);
                    }
                }
        );
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
                updateNonCriticalUserFields(u,view);
                Toast.makeText(view.getApplicationContext(), "You are NOW logged in.", Toast.LENGTH_SHORT).show();
                model.setSessionUser(u);
                ApplicationController.loadSessionTrip(intent, view);
            }
        });
    }
    /**
     * Calls the  ApplicationService class to logout a user. And updates the model
     */
    public void logout() {
        ApplicationService.logoutUser();
        model.clearModelForLogout();
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
            if (err != null && view != null) view.onError(err);
            else {
                List<Trip> sessionTripList = (List<Trip>) resultData.get("filtered-trips");
                m.setSessionTripList(sessionTripList);
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
        ApplicationService.deleteRiderCurrentTrip(m.getSessionTrip().getRiderID(), (resultData, err) -> {
            if (err != null) view.onError(err);
            else {
                m.detachTripListener();
                m.setSessionTrip(null);
            }
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
                m.setSessionTrip(selectedTrip);
                m.setSessionTripList(null);
            }
        }));
    }
    /**
     * Updates non critical user fields when they are edited by user. On success set the new session user.
     * @param updatedSessionUser the updated user object
     * @param view the UI Error Handler interface callback.
     */
    /**/
    public static void updateNonCriticalUserFields(User updatedSessionUser, UIErrorHandler view) {
        ApplicationService.updateUser(updatedSessionUser,((resultData, err) -> {
            if (err == null) {
                App.getModel().setSessionUser(updatedSessionUser);
            } else {
                // TODO: Handle Errors
            }
        }));
    }

}
