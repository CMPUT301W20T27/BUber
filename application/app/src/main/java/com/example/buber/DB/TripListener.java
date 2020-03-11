package com.example.buber.DB;

import com.example.buber.Model.Trip;

public interface TripListener {
    public void onTripStatusUpdate(Trip.STATUS status);
}
