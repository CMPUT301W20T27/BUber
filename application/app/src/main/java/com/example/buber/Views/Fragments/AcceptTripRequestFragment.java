package com.example.buber.Views.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.buber.App;
import com.example.buber.Model.Trip;
import com.example.buber.R;
import com.example.buber.Views.Activities.ContactViewerActivity;
import com.example.buber.Views.Activities.TripBuilderActivity;
import com.example.buber.Views.Activities.TripSearchActivity;
import com.example.buber.Views.Components.TripSearchRecord;

/**
 * Fragment used to accept a trip. Generates a modal that shows trip details and allows a user to
 * accept a trip.
 */
public class AcceptTripRequestFragment extends DialogFragment {
    private TextView estimatedCost;
    private TextView startAdd;
    private TextView endAdd;
    private TextView rider;
    private TextView driverDistance;
    private Button viewContactButton;
    private TripSearchActivity parentActivity;

    private OnFragmentInteractionListener listener;
    private TripSearchRecord tripSearchRecord;
    private int position;

    /**
     * Constructor for AcceptTripRequestFragment
     * @param tripSearchRecord,position the tripSearchRecord and its position in the tripSearch list*/
    public AcceptTripRequestFragment(TripSearchRecord tripSearchRecord, int position, TripSearchActivity parentActivity){
        this.tripSearchRecord = tripSearchRecord;
        this.position = position;
        this.parentActivity = parentActivity;
    }

    /**
     * OnAcceptPressed allows the driver to accept trips by pressing the accept button
     * */
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
    /**
     * OnCreateDialog is builds the TripRequest Fragment and fills the views with correct
     * information from database
     * */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.accept_trip_request_fragment, null);

        estimatedCost = view.findViewById(R.id.fragment_estimated_cost);
        startAdd = view.findViewById(R.id.fragment_startAdd);
        endAdd = view.findViewById(R.id.fragment_endAdd);
        rider = view.findViewById(R.id.fragment_riderName);
        driverDistance = view.findViewById(R.id.fragment_distance);
        viewContactButton = view.findViewById(R.id.viewContactButton);



        if (tripSearchRecord != null){
            estimatedCost.setText(tripSearchRecord.getEstimatedCost());
            startAdd.setText(tripSearchRecord.getStartAddress());
            endAdd.setText(tripSearchRecord.getEndAddress());
            rider.setText(tripSearchRecord.getRiderName());
            driverDistance.setText(tripSearchRecord.getDistanceFromDriver());
            viewContactButton.setOnClickListener(v -> {
                handleViewRiderContact(tripSearchRecord.getRiderID());
            });
        }



        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.
                setView(view)
                .setTitle("View Trip")
                .setNegativeButton("Ignore", null)
                .setPositiveButton("Fair enough, offer ride", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       listener.onAcceptPressed(tripSearchRecord, position);
                    }
                }).create();


    }

    public void handleViewRiderContact(String riderID) {
        Intent contactIntent = new Intent(parentActivity, ContactViewerActivity.class);
        App.getController().handleViewContactInformation(parentActivity, contactIntent, riderID, null);
    }
}
