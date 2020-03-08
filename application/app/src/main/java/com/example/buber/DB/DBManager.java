package com.example.buber.DB;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.Trip;
import com.example.buber.Model.UserLocation;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class DBManager {

    private static final String TAG = "In Database Manager";

    private FirebaseFirestore database;      // Database connection

    private ArrayList<Trip> allTrips;
    private ArrayList<Trip> filteredTrips;
    private EventCompletionListener allTripsListener;
    private EventCompletionListener filteredTripsListener;

    private CollectionReference collectionDriver, collectionRider, collectionTrip;

    public DBManager(String driverCollectionName,
                     String riderCollectionName,
                     String tripCollectionName) {
        database = FirebaseFirestore.getInstance();
        collectionDriver = database.collection(driverCollectionName);
        collectionRider = database.collection(riderCollectionName);
        collectionRider = database.collection(riderCollectionName);
        collectionTrip = database.collection(tripCollectionName);

        allTrips = new ArrayList<>();
        filteredTrips = new ArrayList<>();

        collectionTrip.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                allTrips.clear();
                Log.d(TAG, "Got Trips");

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Trip trip = doc.toObject(Trip.class);
                    allTrips.add(trip);
                    Log.d(TAG, "Trip: " + trip.toString());
                }

                filterTrips();
            }
        });
    }

    /* CREATE */
    public void createRider(String docID, Rider r, EventCompletionListener listener) {
        collectionRider.document(docID).set(r)
                .addOnSuccessListener(aVoid -> {
                    HashMap<String, Rider> toReturn = new HashMap<>();
                    toReturn.put("user", r);
                    listener.onCompletion(toReturn, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    listener.onCompletion(null, new Error("Login failed. Please try again," +
                            "if the issue persists, close and restart the app."));
                });
    }

    public void createDriver(String docID, Driver d, EventCompletionListener listener) {
        collectionDriver.document(docID).set(d)
                .addOnSuccessListener(documentReference -> {
                    HashMap<String, Driver> toReturn = new HashMap<>();
                    toReturn.put("user", d);
                    listener.onCompletion(toReturn, null);
                }).addOnFailureListener((@NonNull Exception e) -> {
            Log.d(TAG, e.getMessage());
            listener.onCompletion(null, new Error("Login failed. Please try again," +
                    "if the issue persists, close and restart the app."));
        });
    }

    public void createTrip(String docID, Trip t, EventCompletionListener listener) {
        collectionTrip.document(docID).set(t)
                .addOnSuccessListener(documentReference -> {
                    HashMap<String, Trip> toReturn = new HashMap<>();
                    toReturn.put("trip", t);
                    listener.onCompletion(toReturn, null);
                }).addOnFailureListener((@NonNull Exception e) -> {
            Log.d(TAG, e.getMessage());
            listener.onCompletion(null, new Error("Login failed. Please try again," +
                    "if the issue persists, close and restart the app."));
        });
    }


    /* GET */
    public void getRider(String docID, EventCompletionListener listener) {
        try {
            collectionRider.document(docID)
                    .get().addOnSuccessListener(documentSnapshot -> {
                HashMap<String, Rider> toReturn = new HashMap<>();
                toReturn.put("user", documentSnapshot.toObject(Rider.class));
                listener.onCompletion(toReturn, null);
            }).addOnFailureListener((@NonNull Exception e) -> {
                Log.d(TAG, e.getMessage());
                listener.onCompletion(null, new Error("Login failed. Please try again," +
                        "if the issue persists, close and restart the app."));
            });
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

    }

    public void getDriver(String docID, EventCompletionListener listener) {
        collectionDriver.document(docID)
                .get().addOnSuccessListener(documentSnapshot -> {
            HashMap<String, Rider> toReturn = new HashMap<>();
            toReturn.put("user", documentSnapshot.toObject(Rider.class));
            listener.onCompletion(toReturn, null);
        }).addOnFailureListener((@NonNull Exception e) -> {
            Log.d(TAG, e.getMessage());
            listener.onCompletion(null, new Error("Login failed. Please try again," +
                    "if the issue persists, close and restart the app."));
        });
    }

    public void getTrip(String docID, EventCompletionListener listener) {
        collectionTrip.document(docID)
                .get().addOnSuccessListener(documentSnapshot -> {
            HashMap<String, Rider> toReturn = new HashMap<>();
            toReturn.put("trip", documentSnapshot.toObject(Rider.class));
            listener.onCompletion(toReturn, null);
        }).addOnFailureListener((@NonNull Exception e) -> {
            Log.d(TAG, e.getMessage());
            listener.onCompletion(null, new Error("Login failed. Please try again," +
                    "if the issue persists, close and restart the app."));
        });
    }

    /* UPDATE */
    public void updateRider(String docID, Rider updatedRider, EventCompletionListener listener) {
        collectionRider.document(docID)
                .set(updatedRider, SetOptions.merge())
                .addOnSuccessListener(documentSnapshot -> {
                    listener.onCompletion(null, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    Error err = new Error("Failed to update rider");
                    listener.onCompletion(null, err);
                });
    }

    public void updateDriver(String docID, Driver updatedDriver, EventCompletionListener listener) {
        collectionDriver.document(docID)
                .set(updatedDriver, SetOptions.merge())
                .addOnSuccessListener(documentSnapshot -> {
                    listener.onCompletion(null, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    Error err = new Error("Failed to update driver");
                    listener.onCompletion(null, err);
                });
    }

    public void updateTrip(String docID, Trip updatedTrip, EventCompletionListener listener) {
        collectionTrip.document(docID)
                .set(updatedTrip, SetOptions.merge())
                .addOnSuccessListener(documentSnapshot -> {
                    listener.onCompletion(null, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    Error err = new Error("Failed to update trip");
                    listener.onCompletion(null, err);
                });
    }

    /* DELETE */
    public void deleteRider(String docID, EventCompletionListener listener) {
        collectionRider.document(docID)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listener.onCompletion(null, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    Error err = new Error("Failed to update trip");
                    listener.onCompletion(null, err);
                });
    }

    public void deleteDriver(String docID, EventCompletionListener listener) {
        collectionDriver.document(docID)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listener.onCompletion(null, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    Error err = new Error("Failed to update trip");
                    listener.onCompletion(null, err);
                });
    }

    public void deleteTrip(String docID, EventCompletionListener listener) {
        collectionTrip.document(docID)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listener.onCompletion(null, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    Error err = new Error("Failed to update trip");
                    listener.onCompletion(null, err);
                });
    }

    public ArrayList<Trip> getTrips(EventCompletionListener listener) {
        this.allTripsListener = listener;
        return allTrips;
    }

    public ArrayList<Trip> getFilteredTrips(EventCompletionListener listener) {
        this.filteredTripsListener = listener;
        return filteredTrips;
    }

    private void filterTrips() {
        double RADIUS = 6.0;
        UserLocation myLocation = new UserLocation(53.558933, -113.469829);
        filteredTrips.clear();

        for (Trip obj : allTrips) {
            Log.d(TAG, "Filtering Trip: " + obj.getDocID());
            double distance = myLocation.distanceTo(obj.getStartUserLocation());
            if (distance <= RADIUS) {
                Log.d(TAG, "Added");
                filteredTrips.add(obj);
            }
        }

        updateTripListeners();
    }

    private void updateTripListeners() {
        if (allTripsListener != null) {
            allTripsListener.onCompletion(null, null);
        }

        if (filteredTripsListener != null) {
            filteredTripsListener.onCompletion(null, null);
        }
    }
}