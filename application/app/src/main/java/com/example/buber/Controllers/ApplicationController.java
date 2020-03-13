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

    public void login(String email, String password, User.TYPE type, LoginActivity view, Intent i) {
        ApplicationService.loginUser(email, password, type, (resultData, err) -> {
            if (err != null) view.onError(err);
            else {
                User u = (User) resultData.get("user");
                updateNonCriticalUserFields(u,view);
                Toast.makeText(view.getApplicationContext(), "You are NOW logged in.", Toast.LENGTH_SHORT).show();
                model.setSessionUser(u);
                ApplicationController.loadSessionTrip(i, view);
            }
        });
    }

    public void logout() {
        ApplicationService.logoutUser();
        model.clearModelForLogout();
    }

    public void updateUserLocation(UserLocation l) {
        ApplicationModel m = App.getModel();
        if (m.getSessionUser() !=  null) {
            m.getSessionUser().setCurrentUserLocation(l);
            m.notifyObservers();
        }
    }

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
     * Updates the model to allow riders to find their ride requests
     * stored in the db
     * @param view
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

    /**Deletes the current trip for rider*/
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

    /**Handles Driver selecting a trip*/
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

    /**Used to update non critical user fields (ie. username, first/last name, phone number) when
     * they are edited by user*/
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
