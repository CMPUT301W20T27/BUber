package com.example.buber.Model;

import android.util.Log;

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

    private static final String TAG = "ApplicationModel";

    private User sessionUser;
    private Trip sessionTrip;
    private List<Trip> sessionTripList;
    private List<String> sesssionTripUserNameList;
    private List<Observer> obs = new ArrayList<>();
    private Double mapBounds[];

    public List<Trip> getSessionTripList() {
        return sessionTripList;
    }

    public void setSessionTripList(List<Trip> sessionTripList) {
        this.sessionTripList = sessionTripList;
        setChanged();
        notifyObservers();
    }

    public User getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(User sessionUser) {
        this.sessionUser = sessionUser;
        setChanged();
        notifyObservers();
    }

    public Trip getSessionTrip() {
        return sessionTrip;
    }

    public void setSessionTrip(Trip sessionTrip) {
        this.sessionTrip = sessionTrip;
        setChanged();
        Log.d(TAG, "Notifying Observers... Trip: " + sessionTrip);
        notifyObservers();
    }

    public List<Observer> getObserversMatchingClass(Class c) {
        List<Observer> res = new ArrayList<>();
        for (Observer o: this.obs) {
            if (o.getClass().equals(c)) {
                res.add(o);
            }
        }
        return res;
    }

    public List<String> getSesssionTripUserNameList() {
        return sesssionTripUserNameList;
    }

    public void setSesssionTripUserNameList(List<String> sesssionTripUserNameList) {
        this.sesssionTripUserNameList = sesssionTripUserNameList;
    }


    @Override
    public synchronized void addObserver(Observer o) {
        this.obs.add(o);
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        this.obs.remove(o);
        super.deleteObserver(o);
    }
}
