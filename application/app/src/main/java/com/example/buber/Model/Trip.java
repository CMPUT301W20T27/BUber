package com.example.buber.Model;
import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

public class Trip {

    @DocumentId
    private String docID;

    private String driverID;
    private String riderID;

    private enum STATUS {
            PENDING,
            DRIVERACCEPT,
            INPROGRESS,
            CANCELED
    }

    private STATUS status;
    private UserLocation startUserLocation;
    private UserLocation endUserLocation;

    public Trip(){}

    public Trip(String riderID, UserLocation startUserLocation, UserLocation endUserLocation) {
        this.driverID = null;
        this.riderID = riderID;
        this.status = STATUS.PENDING;
        this.startUserLocation = startUserLocation;
        this.endUserLocation = endUserLocation;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public void setRiderID(String riderID) {
        this.riderID = riderID;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public UserLocation getStartUserLocation() {
        return startUserLocation;
    }

    public void setStartUserLocation(UserLocation startUserLocation) {
        this.startUserLocation = startUserLocation;
    }

    public UserLocation getEndUserLocation() {
        return endUserLocation;
    }

    public void setEndUserLocation(UserLocation endUserLocation) {
        this.endUserLocation = endUserLocation;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getRiderID() {
        return riderID;
    }


    @NonNull
    @Override
    public String toString() {
        return "Driver ID: " + driverID + " Rider ID: " + riderID + " From: " + startUserLocation.toString() + " To: " + endUserLocation.toString();
    }
}
