package com.example.buber.Model;
import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

/**
 * Represents a trip a rider is wishing to take. Stores two locations, one for the start and end.
 * Status represents the current status of the trip (pending is before a driver accepts, then
 * driveraccept, then inprogress, and cancelled if a user chooses to do so).
 */
public class Trip {

    @DocumentId
    private String docID;

    private String driverID;
    private String riderID;

    public enum STATUS {
        PENDING,
        DRIVER_ACCEPT,
        DRIVER_PICKING_UP,
        EN_ROUTE,
        COMPLETED
    }

    private STATUS status;
    private UserLocation startUserLocation;
    private UserLocation endUserLocation;
    private double fareOffering;
    private String riderUserName;

    /**Trip(...) are the constructors for the Trip() class. One can be called without parameters,
     * the other requires @param RiderID, @param fareOffering, @param UserLocation (start and end),
     * and @param riderUserName*/
    public Trip(){}

    public Trip(String riderID, double fareOffering, UserLocation startUserLocation, UserLocation endUserLocation, String riderUserName) {
        this.driverID = null;
        this.riderID = riderID;
        this.status = STATUS.PENDING;
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

    public boolean nextStatusValid(Trip.STATUS newStatus) {
        if (this.getStatus() == Trip.STATUS.PENDING) {
            if (newStatus == Trip.STATUS.DRIVER_ACCEPT) {
                return true;
            }
        } else if (this.getStatus() == Trip.STATUS.DRIVER_ACCEPT) {
            if (newStatus == Trip.STATUS.DRIVER_PICKING_UP || newStatus == Trip.STATUS.PENDING) {
                return true;
            }
        } else if (this.getStatus() == Trip.STATUS.DRIVER_PICKING_UP) {
            if (newStatus == Trip.STATUS.EN_ROUTE || newStatus == STATUS.PENDING) {
                return true;
            }
        }else if (this.getStatus() == STATUS.EN_ROUTE) {
            if (newStatus == STATUS.COMPLETED || newStatus == STATUS.PENDING) {
                return true;
            }
        } else if (this.getStatus() == newStatus) {
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return "Driver ID: " + driverID + " Rider ID: " + riderID + " From: " + startUserLocation.toString() + " To: " + endUserLocation.toString();
    }
}
