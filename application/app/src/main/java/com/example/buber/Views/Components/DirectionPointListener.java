package com.example.buber.Views.Components;

import com.google.android.gms.maps.model.PolylineOptions;

/**DirectionPointListener is a listener for direction points, used to with polylineOptions*/
//Source Citation: https://stackoverflow.com/questions/47492459/how-do-i-draw-a-route-along-an-existing-road-between-two-points
public interface DirectionPointListener {
    /**onPath() is the onPathMethod
     * @param polyLine is a PolylineOptions object*/
    public void onPath(PolylineOptions polyLine);
}