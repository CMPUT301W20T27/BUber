package com.example.buber.Model;
import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Represents a trip a rider is wishing to take. Stores two locations, one for the start and end.
 * Status represents the current status of the trip (pending is before a driver accepts, then
 * driveraccept, then inprogress, and cancelled if a user chooses to do so).
 */
public class Trip {

    @DocumentId
    private String docID;

    @ServerTimestamp
    private Date timeStamp;

    private String driverID;
    private String riderID;

    public enum STATUS {
            REQUESTED, // Rider has requested a Trip
            DRIVER_ACCEPT, // Driver has accepted the riders Trip
            RIDER_ACCEPT, // Rider has accepted the Driver
            COMMING,    // The Driver is comming to pick up rider
            ON_ROUTE,   // On route to destination
            COMPLETED,  // Ride is over :(
            CANCELED    // Oh no rider was canceled
    }

    private STATUS status;
    private UserLocation startUserLocation;
    private UserLocation endUserLocation;
    private double fareOffering;
    private String riderUserName;

    public Trip(){}

    public Trip(String riderID, double fareOffering, UserLocation startUserLocation, UserLocation endUserLocation, String riderUserName) {
        this.driverID = null;
        this.riderID = riderID;
        this.status = STATUS.REQUESTED;
        this.fareOffering = fareOffering;
        this.startUserLocation = startUserLocation;
        this.endUserLocation = endUserLocation;
        this.riderUserName = riderUserName;
    }

    public void setRiderID(String riderID) {
        this.riderID = riderID;
    }

    public UserLocation getEndUserLocation() {
        return endUserLocation;
    }

    public void setEndUserLocation(UserLocation endUserLocation) {
        this.endUserLocation = endUserLocation;
    }

    public UserLocation getStartUserLocation() {
        return startUserLocation;
    }

    public void setStartUserLocation(UserLocation startUserLocation) {
        this.startUserLocation = startUserLocation;
    }

    public double getFareOffering() {
        return fareOffering;
    }

    public void setFareOffering(double fareOffering) {
        this.fareOffering = fareOffering;
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


    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public String getRiderUserName() {
        return riderUserName;
    }

    public void setRiderUserName(String riderUserName) {
        this.riderUserName = riderUserName;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "Driver ID: " + driverID + " Rider ID: " + riderID + " From: " + startUserLocation.toString() + " To: " + endUserLocation.toString();
    }
}
