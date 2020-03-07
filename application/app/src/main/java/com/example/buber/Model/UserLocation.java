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

    public double distanceTo(Location end) {
        double raw = Math.pow((latitude - end.getLatitude()), 2) + Math.pow((longitude - end.getLongitude()), 2);
        return Math.abs(raw);
    }

    public double distancePriceEstimate(Location end) {
        // Based on UberX's Price Constant From Edmonton, AB To Calgary, AB @ March 7, 2020
        // Rounding to two decimal places using the math round
        return Math.round(50.3860309543 * distanceTo(end) * 100.0) / 100.0;
    }
}
