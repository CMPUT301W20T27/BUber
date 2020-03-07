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
    private Location startLocation;
    private Location endLocation;

    public Trip(){}

    public Trip(String riderID, STATUS status, Location startLocation, Location endLocation) {
        this.driverID = null;
        this.riderID = riderID;
        this.status = STATUS.PENDING;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
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
