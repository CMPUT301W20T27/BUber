package com.example.buber.Model;

import com.example.buber.App;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Root class for our application data. Contains all needed data that views require. Extends the
 * Observable class to implement MVC. Contains data like current user, current trip (if one exists),
 * query results and mapstate.
 */
public class ApplicationModel extends Observable {
    private User sessionUser;
    private Trip sessionTrip;
    private List<Trip> sessionTripList;
    private List<Observer> obs = new ArrayList<>();
    private Double mapBounds[];
    private ListenerRegistration tripListener;
    /**
     * Getting the current sessions list of trips
     */
    public List<Trip> getSessionTripList() {
        return sessionTripList;
    }
    /**
     * Setting the current sessions list of trips
     * @param sessionTripList the new list of trips
     */
    public void setSessionTripList(List<Trip> sessionTripList) {
        this.sessionTripList = sessionTripList;
        setChanged();
        notifyObservers();
    }
    /**
     * Getting the sessions user
     */
    public User getSessionUser() {
        return sessionUser;
    }
    /**
     * Setting the sessions user and notifying all the views
     * @param sessionUser the session user
     */
    public void setSessionUser(User sessionUser) {
        this.sessionUser = sessionUser;
        setChanged();
        notifyObservers();
    }
    /**
     * Getting the current sessions user
     */
    public Trip getSessionTrip() {
        return sessionTrip;
    }
    /**
     * Setting the sessions Trip and notifying all the views
     * @param sessionTrip the session trip
     */
    public void setSessionTrip(Trip sessionTrip) {
        if (sessionTrip == null) {
            this.detachTripListener();
        }

        this.sessionTrip = sessionTrip;
        setChanged();
        notifyObservers();
    }

    /**
     * List of observers for the model
     */
    public List<Observer> getObserversMatchingClass(Class c) {
        List<Observer> res = new ArrayList<>();
        for (Observer o: this.obs) {
            if (o.getClass().equals(c)) {
                res.add(o);
            }
        }
        return res;
    }
    /**
     * Adding an observer to the list of observers of the model
     * @param observer the new observer
     */
    @Override
    public synchronized void addObserver(Observer observer) {
        this.obs.add(observer);
        super.addObserver(observer);
    }
    /**
     * Removing an observer from the list of observers of the model
     */
    @Override
    public synchronized void deleteObserver(Observer o) {
        this.obs.remove(o);
        super.deleteObserver(o);
    }
    /**
     * Getting a trip listener to listen to a trip status change
     */
    public ListenerRegistration getTripListener() {
        return tripListener;
    }
    /**
     *  Setting a trip listener to a trip
     * @param tripListener the listener for the trip
     */
    public void setTripListener(ListenerRegistration tripListener) {
        this.tripListener = tripListener;
    }
    /**
     * Removing the Trip listener
     */
    public void detachTripListener() {
        ListenerRegistration lr = this.getTripListener();
        if (lr != null) {
            lr.remove();
            this.setTripListener(null);
        }
    }
    /**
     * Set session Trip/User/Listener to null on user logout
     */
    public void clearModelForLogout() {
        this.sessionUser = null;
        this.tripListener = null;
        this.sessionTrip = null;
    }

    public void handleTripStatusChanges(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot != null) {
            Trip trip = documentSnapshot.toObject(Trip.class);
            if (trip != null) {
                Trip.STATUS newStatus = trip.getStatus();
                if (trip.nextStatusValid(newStatus)) {
                    Trip newTrip = App.getModel().getSessionTrip();
                    newTrip.setStatus(newStatus);
                    setSessionTrip(newTrip);
                }
            } else {
                User sessionUser = App.getModel().getSessionUser();
                if (sessionUser != null && sessionUser.getType() == User.TYPE.DRIVER) {
                    // If driver profile is loaded, get the next session trip from the queue
                    Driver driverSessionUser = (Driver) sessionUser;
                    List<String> tripIds = driverSessionUser.getAcceptedTripIds();
                    if (tripIds != null && tripIds.size() > 0) {
                        String nxtTripID = tripIds.get(0);
                        App.getDbManager().getTrip(nxtTripID, (resultData, err) -> {
                            if (resultData != null) {
                                Trip nxtTrip = (Trip) resultData.get("trip");
                                nxtTrip.setDriverID(App.getAuthDBManager().getCurrentUserID());
                                nxtTrip.setStatus(Trip.STATUS.DRIVER_ACCEPT);
                                App.getDbManager().updateTrip(nxtTripID, nxtTrip, ((resultData1, err1) -> {}), true);
                            }
                        }, false);
                    } else {
                        setSessionTrip(null);
                    }
                }
            }
        }
    }
}
