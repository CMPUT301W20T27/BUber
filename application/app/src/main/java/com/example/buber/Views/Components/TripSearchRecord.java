package com.example.buber.Views.Components;

import com.example.buber.Model.Trip;
import com.example.buber.Model.UserLocation;

import java.io.Serializable;

/**
 * Formatted Trip used for displaying trip information in the trip selection modal.
 */
public class TripSearchRecord implements Serializable {
    private String riderName;
    private String startAddress;
    private String endAddress;
    private String endLatitude;
    private String endLongitude;
    private String estimatedCost;
    private String distanceFromDriver;
    private String riderID;

    /**The following function is used assign user info for use in TripSearch views
     * @param t The stored trip information
     * @param driverLocation the location of the driver user*/
    public TripSearchRecord(Trip t, UserLocation driverLocation){
        this.riderName = t.getRiderUserName();
        this.startAddress = t.getStartUserLocation().getAddress();
        this.endAddress = t.getEndUserLocation().getAddress();
        this.estimatedCost = ((Double) t.getFareOffering()).toString();
        this.distanceFromDriver = ((Double) t.getStartUserLocation().distanceTo(driverLocation))
                .toString();
        this.riderID = t.getRiderID();
    }

    public String getRiderName(){return this.riderName;}
    public String getStartAddress(){return this.startAddress;}
    public String getEndAddress(){return this.endAddress;}
    public String getEstimatedCost(){return this.estimatedCost;}
    public String getDistanceFromDriver(){return this.distanceFromDriver;}

    public void setRiderName(String newRiderName){this.riderName = newRiderName;}
    public void setStartAddress(String newStartLat){this.startAddress = newStartLat;}
    public void setEndAddress(String newStartLong){this.endAddress = newStartLong;}
    public void setEstimatedCost(String newEstimatedCost){this.estimatedCost = newEstimatedCost;}
    public void setDistanceFromDriver(String newDistance){this.distanceFromDriver = newDistance;}

    public String getRiderID() {
        return riderID;
    }

    public void setRiderID(String riderID) {
        this.riderID = riderID;
    }
}
