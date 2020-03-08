package com.example.buber.Model;

import androidx.annotation.NonNull;

public class UserLocation {
    private double latitude;
    private double longitude;

    // Important for db stuff
    public UserLocation() {}

    public UserLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double distancePriceEstimate(UserLocation end) {
        // Based on UberX's Price Constant From Edmonton, AB To Calgary, AB @ March 7, 2020
        // Rounding to two decimal places using the math round
        return Math.round(1.1823 * distanceTo(end) * 100.0) / 100.0;
    }
    
    public double distanceTo(UserLocation other) {
        double lat1 = this.getLatitude();
        double lon1 = this.getLongitude();
        double lat2 = other.getLatitude();
        double lon2 = other.getLongitude();

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        // Convert miles to km
        dist = dist * 1.609344;

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @NonNull
    @Override
    public String toString() {
        return "Lat: " + getLatitude() + " Long: " + getLongitude();
    }
}
