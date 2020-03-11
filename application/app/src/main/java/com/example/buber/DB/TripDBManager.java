package com.example.buber.DB;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Observable;
import java.util.Observer;


public class TripDBManager implements Observer {


    private static final String TAG = "In Trips DB Manager";

    private FirebaseFirestore database;      // Database connection

    private CollectionReference collectionTrip;

    private TripListener listener;
    private ListenerRegistration registration;  // Need to delete listener

    private Trip trip;

    public TripDBManager() {
        Log.d(TAG, "TripDBManager: ");
        database = FirebaseFirestore.getInstance();
        collectionTrip = database.collection("Trips");
        App.getModel().addObserver(this);

    }

    public void setListener(TripListener listener) {
        this.listener = listener;
    }


    @Override
    public void update(Observable o, Object arg) {
        Log.d(TAG, "Updating Trip DB Manager...");
        ApplicationModel model = (ApplicationModel) o;
        if (model.getSessionTrip() != null) {
            subscribeToTrip(model.getSessionTrip());
        } else {
            unsubscribe();
        }

    }

    public void subscribeToTrip(Trip trip) {
        Log.d(TAG, "In watch Trip");
        unsubscribe();
        this.trip = trip;


        registration = database.collection("Trips").document(trip.getDocID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        Log.d(TAG, "Trip updated");

                        Trip newTrip = documentSnapshot.toObject(Trip.class);
                        Trip.STATUS newStatus = newTrip.getStatus();

                        Log.d(TAG, "Status is Valid: " + statusIsValid(newStatus));

                        if (statusIsValid(newStatus)) {
                            App.getModel().getSessionTrip().setStatus(newStatus);
                            if (listener != null) {
                                Log.d(TAG, "Listener not null");
                                listener.onTripStatusUpdate(newStatus);
                            }
                        }


                    }
                });
    }

    public void unsubscribe() {
        if (registration != null) {
            registration.remove();
        }
    }

    private boolean statusIsValid(Trip.STATUS new_status) {

        if (trip.getStatus() == Trip.STATUS.REQUESTED) {
            if (new_status == Trip.STATUS.DRIVER_ACCEPT || new_status == Trip.STATUS.CANCELED) {
                return true;
            }
        } else if (trip.getStatus() == Trip.STATUS.DRIVER_ACCEPT) {
            if (new_status == Trip.STATUS.RIDER_ACCEPT || new_status == Trip.STATUS.REQUESTED || new_status == Trip.STATUS.CANCELED) {
                return true;
            }
        } else if (trip.getStatus() == Trip.STATUS.RIDER_ACCEPT) {
            if (new_status == Trip.STATUS.COMMING || new_status == Trip.STATUS.CANCELED) {
                return true;
            }

        } else if (trip.getStatus() == Trip.STATUS.COMMING) {
            if (new_status == Trip.STATUS.ON_ROUTE || new_status == Trip.STATUS.CANCELED) {
                return true;
            }

        } else if (trip.getStatus() == Trip.STATUS.ON_ROUTE) {
            if (new_status == Trip.STATUS.COMPLETED) {
                return true;
            }

        }
        Log.d(TAG, "Trip status illegal. Cannot switch from " + trip.getStatus() + " to " + new_status);
        return false;
    }

}
