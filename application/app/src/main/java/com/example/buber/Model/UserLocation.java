package com.example.buber.Model;

public class UserLocation {
    private double latitude;
    private double longitude;

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

    public double distanceTo(UserLocation end) {
        double raw = Math.pow((latitude - end.getLatitude()), 2) + Math.pow((longitude - end.getLongitude()), 2);
        return Math.abs(raw);
    }
}
