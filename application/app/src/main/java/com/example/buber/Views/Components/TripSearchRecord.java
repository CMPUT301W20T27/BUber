package com.example.buber.Views.Components;

import com.example.buber.Model.Trip;
import com.example.buber.Model.UserLocation;

import java.io.Serializable;

public class TripSearchRecord implements Serializable {
    private String riderName;
    private String startLatitude;
    private String startLongitude;
    private String endLatitude;
    private String endLongitude;
    private String estimatedCost;
    private String distanceFromDriver;

    public TripSearchRecord(Trip t, UserLocation driverLocation, String riderName){
        this.riderName = riderName;
        this.startLatitude = ((Double) t.getStartUserLocation().getLatitude()).toString();
        this.startLongitude = ((Double) t.getStartUserLocation().getLongitude()).toString();
        this.endLatitude = ((Double) t.getEndUserLocation().getLatitude()).toString();
        this.endLongitude = ((Double) t.getEndUserLocation().getLongitude()).toString();
        this.estimatedCost = ((Double) t.getFareOffering()).toString();
        this.distanceFromDriver = ((Double) t.getStartUserLocation().distanceTo(driverLocation))
                .toString();
    }

    public String getRiderName(){return this.riderName;}
    public String getStartLatitude(){return this.startLatitude;}
    public String getStartLongitude(){return this.startLongitude;}
    public String getEndLatitude(){return this.endLatitude;}
    public String getEndLongitude(){return this.endLongitude;}
    public String getEstimatedCost(){return this.estimatedCost;}
    public String getDistanceFromDriver(){return this.distanceFromDriver;}

    public void setRiderName(String newRiderName){this.riderName = newRiderName;}
    public void setStartLatitude(String newStartLat){this.startLatitude = newStartLat;}
    public void setStartLongitude(String newStartLong){this.startLongitude = newStartLong;}
    public void setEndLatitude(String newEndLat){this.endLatitude = newEndLat;}
    public void setEndLongitude(String newEndLong){this.endLongitude = newEndLong;}
    public void setEstimatedCost(String newEstimatedCost){this.estimatedCost = newEstimatedCost;}
    public void setDistanceFromDriver(String newDistance){this.distanceFromDriver = newDistance;}
}
