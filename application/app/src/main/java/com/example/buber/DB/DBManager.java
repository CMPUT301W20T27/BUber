package com.example.buber.DB;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.buber.App;
import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.Trip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Repository for accessing firebase. Used to perform CRUD (Create, Read, Update Destroy) on
 * our Firebase collections. Uses listeners to handle asynchronous reads/writes to Firebase.
 */
public class DBManager {

    private static final String TAG = "In Database Manager";

    private FirebaseFirestore database;      // Database connection

    private CollectionReference collectionDriver, collectionRider, collectionTrip;

    public FirebaseFirestore getDatabase() {
        return database;
    }

    public CollectionReference getCollectionDriver() {
        return collectionDriver;
    }

    public CollectionReference getCollectionRider() {
        return collectionRider;
    }

    public CollectionReference getCollectionTrip() {
        return collectionTrip;
    }

    public DBManager(String driverCollectionName,
                     String riderCollectionName,
                     String tripCollectionName) {
        database = FirebaseFirestore.getInstance();
        collectionDriver = database.collection(driverCollectionName);
        collectionRider = database.collection(riderCollectionName);
        collectionTrip = database.collection(tripCollectionName);
        Log.d(TAG, "DBManager instansitated");
    }
    /*
    public boolean isRiderLoggedOn(String docID){
        Log.d("AnotherDBTEST",docID);
        boolean retValue;
        collectionRider.document(docID)
                .get().addOnSuccessListener(documentSnapshot -> {
           Rider tempRider = documentSnapshot.toObject(Rider.class);
           if(tempRider.getRiderLoggedOn()){
               Log.d("HURRAY","Rider is logged on");
               retValue = true;
            }
           else{
               Log.d("NOTHURRAY","Rider is not logged on");
           }
        }).addOnFailureListener((@NonNull Exception e) -> {
            Log.d(TAG, e.getMessage());
        });

        return false;
    }
     */
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

    public void createTrip(Trip tripRequest, EventCompletionListener listener) {
        Log.d(TAG, "Creating trip...");
        collectionTrip
                .add(tripRequest)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Trip Created Successfully");
                    tripRequest.setDocID(documentReference.getId()); // Set the doc id for the trip. VERY IMPORTANT!!!

                    HashMap<String, Trip> toReturn = new HashMap<>();
                    toReturn.put("trip", tripRequest);
                    listener.onCompletion(toReturn, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    listener.onCompletion(null, new Error("Could not submit trip request."));
                });
    }


    /* GET */
    public void getRider(String docID, EventCompletionListener listener) {
        try {
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
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

    }

    public void getDriver(String docID, EventCompletionListener listener) {
        collectionDriver.document(docID)
                .get().addOnSuccessListener(documentSnapshot -> {
            HashMap<String, Driver> toReturn = new HashMap<>();
            toReturn.put("user", documentSnapshot.toObject(Driver.class));
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
            HashMap<String, Trip> toReturn = new HashMap<>();
            toReturn.put("trip", documentSnapshot.toObject(Trip.class));
            listener.onCompletion(toReturn, null);
        }).addOnFailureListener((@NonNull Exception e) -> {
            Log.d(TAG, e.getMessage());
            listener.onCompletion(null, new Error("Login failed. Please try again," +
                    "if the issue persists, close and restart the app."));
        });
    }

    public void getTrips(EventCompletionListener listener) {
        collectionTrip.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> data = queryDocumentSnapshots.getDocuments();
            HashMap<String, List<Trip>> toReturn = new HashMap<>();
            List<Trip> tripData = new LinkedList<>();
            for (DocumentSnapshot snapshot : data) {
                tripData.add(snapshot.toObject(Trip.class));
            }
            toReturn.put("all-trips", tripData);
            listener.onCompletion(toReturn, null);
        }).addOnFailureListener(e -> {
            listener.onCompletion(null, new Error(e.getMessage()));
        });
    }

    /* UPDATE */
    public void updateRider(String docID, Rider updatedRider, EventCompletionListener listener) {
        Log.d("DBMANAGER","Updating Rider");
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
        Log.d("DBMANAGER","Updating driver");
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
}