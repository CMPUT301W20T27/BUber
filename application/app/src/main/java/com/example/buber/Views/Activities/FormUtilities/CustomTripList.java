package com.example.buber.Views.Activities.FormUtilities;

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

public class CustomTripList extends ArrayAdapter<TripSearchRecord> {
    private ArrayList<TripSearchRecord> tripRecords;
    private Context context;

    //constructor
    public CustomTripList(Context context, ArrayList<TripSearchRecord> tripRecords) {
        super(context, 0, tripRecords);
        this.tripRecords = tripRecords;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.trip_search_content, parent, false);
        }

        //Finds the particular row element using position in tripRecord
        TripSearchRecord tripSearchRecord = tripRecords.get(position);

        //trip_search_content.xml views are found and assigned here
        TextView riderName = view.findViewById(R.id.trip_search_rider);
        TextView startLat = view.findViewById(R.id.trip_search_startlatitude);
        TextView startLong = view.findViewById(R.id.trip_search_startlongitude);
        TextView endLat = view.findViewById(R.id.trip_search_endlatitude);
        TextView endLong = view.findViewById(R.id.trip_search_endlongitude);
        TextView estCost = view.findViewById(R.id.trip_search_est_cost);
        TextView driverDist = view.findViewById(R.id.trip_search_driver_distance);

        //Sets text in TextViews
        riderName.setText(tripSearchRecord.getRiderName());
        startLat.setText(tripSearchRecord.getStartLatitude());
        startLong.setText(tripSearchRecord.getStartLongitude());
        endLat.setText(tripSearchRecord.getEndLatitude());
        endLong.setText(tripSearchRecord.getEndLongitude());
        estCost.setText(tripSearchRecord.getEstimatedCost());
        driverDist.setText(tripSearchRecord.getDistanceFromDriver());

        return view;

    }
}
