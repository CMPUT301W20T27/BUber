package com.example.buber.Views.Components;

import java.io.Serializable;

public class TripSearchRecord implements Serializable {
    public String riderName;
    public String startLatitude;
    public String startLongitude;
    public String endLatitude;
    public String endLongitude;
    public String estimatedCost;
    public String distanceFromDriver;

    public TripSearchRecord(String riderName, String startLatitude, String startLongitude, String endLatitude,
                     String endLongitude, String estimatedCost, String distanceFromDriver){
        this.riderName = riderName;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.estimatedCost = estimatedCost;
        this.distanceFromDriver = distanceFromDriver;
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
