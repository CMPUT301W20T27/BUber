package com.example.buber.Model;

import java.util.List;
import java.util.Observable;

public class ApplicationModel extends Observable {
    private User sessionUser;
    private Trip sessionTrip;
    private List<Trip> driverQueryResult;

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
        notifyObservers();
    }

    public List<Trip> getDriverQueryResult() {
        return driverQueryResult;
    }

    public void setDriverQueryResult(List<Trip> driverQueryResult) {
        this.driverQueryResult = driverQueryResult;
    }
}
