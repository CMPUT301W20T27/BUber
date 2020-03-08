package com.example.buber.Views.Activities.FormUtilities;

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

    String getRiderName(){return this.riderName;}
    String getStartLatitude(){return this.startLatitude;}
    String getStartLongitude(){return this.startLongitude;}
    String getEndLatitude(){return this.endLatitude;}
    String getEndLongitude(){return this.endLongitude;}
    String getEstimatedCost(){return this.estimatedCost;}
    String getDistanceFromDriver(){return this.distanceFromDriver;}

    void setRiderName(String newRiderName){this.riderName = newRiderName;}
    void setStartLatitude(String newStartLat){this.startLatitude = newStartLat;}
    void setStartLongitude(String newStartLong){this.startLongitude = newStartLong;}
    void setEndLatitude(String newEndLat){this.endLatitude = newEndLat;}
    void setEndLongitude(String newEndLong){this.endLongitude = newEndLong;}
    void setEstimatedCost(String newEstimatedCost){this.estimatedCost = newEstimatedCost;}
    void setDistanceFromDriver(String newDistance){this.distanceFromDriver = newDistance;}
}
