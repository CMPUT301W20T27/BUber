package com.example.buber.Model;

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

    public ListenerRegistration getTripListener() {
        return tripListener;
    }

    public void setTripListener(ListenerRegistration tripListener) {
        this.tripListener = tripListener;
    }

    public void detachTripListener() {
        ListenerRegistration lr = this.getTripListener();
        if (lr != null) {
            lr.remove();
            this.setTripListener(null);
        }
    }

    public void clearModelForLogout() {
        this.sessionUser = null;
        this.tripListener = null;
        this.sessionTrip = null;
    }
}
