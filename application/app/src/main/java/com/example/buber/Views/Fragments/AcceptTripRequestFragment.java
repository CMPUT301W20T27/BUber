package com.example.buber.Views.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.buber.R;
import com.example.buber.Views.Components.TripSearchRecord;

public class AcceptTripRequestFragment extends DialogFragment {
    private TextView estimatedCost;
    private TextView startLat;
    private TextView startLong;
    private TextView endLat;
    private TextView endLong;
    private TextView rider;
    private TextView driverDistance;

    private OnFragmentInteractionListener listener;
    private TripSearchRecord tripSearchRecord;
    private int position;

    public AcceptTripRequestFragment(TripSearchRecord tripSearchRecord, int position){
        this.tripSearchRecord = tripSearchRecord;
        this.position = position;
    }

    public interface OnFragmentInteractionListener{
        void onAcceptPressed(TripSearchRecord tripSearchRecord, int position);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()+
                    "must implement onFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.accept_trip_request_fragment, null);

        estimatedCost = view.findViewById(R.id.fragment_estimated_cost);
        startLat = view.findViewById(R.id.fragment_startLat);
        startLong = view.findViewById(R.id.fragment_startLong);
        endLat = view.findViewById(R.id.fragment_endLat);
        endLong = view.findViewById(R.id.fragment_endLong);
        rider = view.findViewById(R.id.fragment_riderName);
        driverDistance = view.findViewById(R.id.fragment_distance);

        if (tripSearchRecord != null){
            estimatedCost.setText(tripSearchRecord.getEstimatedCost());
            startLat.setText(tripSearchRecord.getStartLatitude());
            startLong.setText(tripSearchRecord.getStartLongitude());
            endLat.setText(tripSearchRecord.getEndLatitude());
            endLong.setText(tripSearchRecord.getEndLongitude());
            rider.setText(tripSearchRecord.getRiderName());
            driverDistance.setText(tripSearchRecord.getDistanceFromDriver());

            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder.
                    setView(view)
                    .setTitle("View Trip")
                    .setNegativeButton("Decline", null)
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onAcceptPressed(tripSearchRecord, position);
                        }
                    }).create();

        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.
                setView(view)
                .setTitle("View Trip")
                .setNegativeButton("Decline", null)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       listener.onAcceptPressed(tripSearchRecord, position);
                    }
                }).create();
    }
}
