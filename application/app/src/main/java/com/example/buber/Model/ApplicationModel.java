package com.example.buber.Model;

import java.util.ArrayList;
import java.util.Observable;

public class ApplicationModel extends Observable {
    private User sessionUser;
    private ArrayList<RideRequest> currentRequests;


    public User getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(User sessionUser) {
        this.sessionUser = sessionUser;
        notifyObservers();
    }

    public ArrayList<RideRequest> getCurrentRequests() {
        return currentRequests;
    }

    public void setCurrentRequests(ArrayList<RideRequest> currentRequests) {
        this.currentRequests = currentRequests;
        notifyObservers();
    }

}
