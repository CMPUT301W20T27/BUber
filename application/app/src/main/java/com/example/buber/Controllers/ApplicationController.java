package com.example.buber.Controllers;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.Model.UserLocation;
import com.example.buber.Services.ApplicationService;
import com.example.buber.Views.Activities.MapActivity;
import com.example.buber.Views.UIErrorHandler;

import java.util.List;
import java.util.Observer;

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

    public void login(String email, String password, User.TYPE type, UIErrorHandler view) {
        ApplicationService.loginUser(email, password, type, (resultData, err) -> {
            if (err != null) view.onError(err);
            else {
                User u = (User) resultData.get("user");
                model.setSessionUser(u);
            }
        });
    }

    public void logout() {
        ApplicationService.logoutUser();
        model.setSessionUser(null);
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
            if (err != null) view.onError(err);
            else {
                List<Trip> sessionTripList = (List<Trip>) resultData.get("filtered-trips");
                m.setSessionTripList(sessionTripList);
            }
        });
    }

    public static void handleDriverTripSelect(Trip selectedTrip) {
        ApplicationModel m = App.getModel();
        selectedTrip.setStatus(Trip.STATUS.DRIVERACCEPT);
        String userId = App.getAuthDBManager().getCurrentUserID();
        selectedTrip.setDriverID(userId);
        ApplicationService.updateTripStatus(userId, selectedTrip, ((resultData, err) -> {
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
