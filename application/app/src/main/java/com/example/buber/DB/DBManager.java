package com.example.buber.DB;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class DBManager {

    private static final String TAG = "In Database Manager";

    private FirebaseFirestore database;      // Database connection

    private CollectionReference collectionDriver, collectionRider, collectionTrip;

    public DBManager(String driverCollectionName,
                     String riderCollectionName,
                     String tripCollectionName) {
        database = FirebaseFirestore.getInstance();
        collectionDriver = database.collection(driverCollectionName);
        collectionRider = database.collection(riderCollectionName);
        collectionTrip = database.collection(tripCollectionName);
    }

    /* CREATE */
    public void createRider(String docID, Rider r, EventCompletionListener listener) {
        r.setDocID(docID);
        collectionRider.document(docID).set(r)
                .addOnSuccessListener(documentReference -> {
                    HashMap<String, Rider> toReturn = new HashMap<>();
                    toReturn.put("user", r);
                    listener.onCompletion(toReturn, null);
                }).addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    listener.onCompletion(null, new Error("Login failed. Please try again," +
                            "if the issue persists, close and restart the app."));
        });
    }

    public void createDriver(String docID, Driver d, EventCompletionListener listener) {
        d.setDocID(docID);
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

    public void createTrip(String riderID, String driverID) {
        // todo
    }


    /* READ */
    public ArrayList getAll(String rootCollection) {
        // todo
        return new ArrayList();
    }

    public void getRider(String docID, EventCompletionListener listener) {
        collectionRider.document(docID)
                .get().addOnSuccessListener(documentSnapshot -> {
            HashMap<String, Rider> toReturn = new HashMap<>();
            toReturn.put("user", documentSnapshot.toObject(Rider.class));
            listener.onCompletion(toReturn,null);
        }).addOnFailureListener((@NonNull Exception e) -> {
            Log.d(TAG, e.getMessage());
            listener.onCompletion(null, new Error("Login failed. Please try again," +
                    "if the issue persists, close and restart the app."));
        });
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

    /* UPDATE */
    public void updateRider(String docID) {
        // todo
    }

    public void updateDriver(String docID) {
        // todo
    }

    public void updateTrip(String riderID, String driverID) {
        // todo
    }

    /* DELETE */
    public void deleteRider(String docId) {
        // todo
    }

    public void deleteDriver(String docId) {
        // todo
    }

    public void deleteTrip(String riderID, String driverID) {
        // todo
    }
}