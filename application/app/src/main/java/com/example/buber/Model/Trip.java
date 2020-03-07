package com.example.buber.Model;
import androidx.annotation.NonNull;

public class Trip {

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

    public Trip(String riderID, STATUS status, UserLocation startUserLocation, UserLocation endUserLocation) {
        this.driverID = null;
        this.riderID = riderID;
        this.status = STATUS.PENDING;
        this.startUserLocation = startUserLocation;
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

    public void setRiderOD(String riderID) {
        this.riderID = riderID;
    }

    @NonNull
    @Override
    public String toString() {
        return "Driver ID: " + driverID + "Rider ID: " + riderID;
    }
}
