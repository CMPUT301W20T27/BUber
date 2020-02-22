package com.example.buber.Model;

import androidx.annotation.NonNull;

public class Trip {

    private String driverID;
    private String riderID;

    public Trip(){}         // VERY IMPORTANT TO HAVE


    public Trip(String driverID, String riderOD) {
        this.driverID = driverID;  //or doc id-not planned yet
        this.riderID = riderOD;
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
