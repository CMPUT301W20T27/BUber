package com.example.buber.Views.Components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.buber.R;

import java.util.ArrayList;

/**
 * UI Fragment for displaying a list of trip data.
 */
public class CustomTripList extends ArrayAdapter<TripSearchRecord> {
    public ArrayList<TripSearchRecord> tripRecords;
    public Context context;

    //constructor
    public CustomTripList(Context context, ArrayList<TripSearchRecord> tripRecords) {
        super(context, 0, tripRecords);
        this.tripRecords = tripRecords;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.trip_search_content, parent, false);
        }

        //Finds the particular row element using position in tripRecord
        TripSearchRecord tripSearchRecord = tripRecords.get(position);

        //trip_search_content.xml views are found and assigned here
        TextView riderName = view.findViewById(R.id.trip_search_rider);
        TextView startAdd = view.findViewById(R.id.trip_search_startaddress);
        TextView endAdd = view.findViewById(R.id.trip_search_endaddress);
        TextView estCost = view.findViewById(R.id.trip_search_est_cost);
        TextView driverDist = view.findViewById(R.id.trip_search_driver_distance);

        //Sets text in TextViews
        riderName.setText(tripSearchRecord.getRiderName());
        startAdd.setText(tripSearchRecord.getStartAddress());
        endAdd.setText(tripSearchRecord.getEndAddress());
        estCost.setText(tripSearchRecord.getEstimatedCost());
        driverDist.setText(tripSearchRecord.getDistanceFromDriver());

        return view;
    }
}
