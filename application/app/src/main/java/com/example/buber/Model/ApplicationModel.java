package com.example.buber.Model;

import java.util.List;
import java.util.Observable;

public class ApplicationModel extends Observable {
    private User sessionUser;
    private Trip sessionTrip;
    private List<Trip> sessionTripList;

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
}
