package com.example.buber.DB;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.buber.App;
import com.example.buber.Controllers.EventCompletionListener;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
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

    /**constructs DBManager*/
    public DBManager(String driverCollectionName,
                     String riderCollectionName,
                     String tripCollectionName) {
        database = FirebaseFirestore.getInstance();
        collectionDriver = database.collection(driverCollectionName);
        collectionRider = database.collection(riderCollectionName);
        collectionTrip = database.collection(tripCollectionName);
    }

    /* CREATE */
    /**
     * Create a rider document in Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will get the rider object.
     * NOTE: The docID passed in is the same as the one used when a Firebase Authentication account was created
     * @param docID the doc id of the user
     * @param rider an object of type rider
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
    public void createRider(String docID, Rider rider, EventCompletionListener listener) {
        collectionRider.document(docID).set(rider)
                .addOnSuccessListener(aVoid -> {
                    HashMap<String, Rider> toReturn = new HashMap<>();
                    toReturn.put("user", rider);
                    listener.onCompletion(toReturn, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    listener.onCompletion(null, new Error("Login failed. Please try again," +
                            "if the issue persists, close and restart the app."));
                });
    }
    /**
     * Create a driver document in Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will pass get driver object
     * NOTE: The docID passed in is the same as the one used when a Firebase Authentication account was created
     * @param docID the doc id of the user
     * @param driver an object of type driver
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
    public void createDriver(String docID, Driver driver, EventCompletionListener listener) {
        collectionDriver.document(docID).set(driver)
                .addOnSuccessListener(documentReference -> {
                    HashMap<String, Driver> toReturn = new HashMap<>();
                    toReturn.put("user", driver);
                    listener.onCompletion(toReturn, null);
                }).addOnFailureListener((@NonNull Exception e) -> {
            Log.d(TAG, e.getMessage());
            listener.onCompletion(null, new Error("Login failed. Please try again," +
                    "if the issue persists, close and restart the app."));
        });
    }

    /**
     * Create a Trip document in Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will get the updates
     * @param tripRequest the Trip object
     * @param listener  the listener that waits for the asynchronous Firebase call to finish
     * @param listenForUpdates  adds a snapshot listener if true
     */
    public void createTrip(Trip tripRequest, EventCompletionListener listener, boolean listenForUpdates) {
        collectionTrip
                .document(tripRequest.getRiderID())
                .set(tripRequest)
                .addOnSuccessListener(documentReference -> {
                    HashMap<String, Trip> toReturn = new HashMap<>();
                    toReturn.put("trip", tripRequest);
                    listener.onCompletion(toReturn, null);
                    if (listenForUpdates) {
                        ListenerRegistration lr =
                        collectionTrip
                                .document(tripRequest.getRiderID())
                                .addSnapshotListener((documentSnapshot, e) -> {
                                    if (documentSnapshot != null) {
                                        Trip newTrip = documentSnapshot.toObject(Trip.class);

                                        if (newTrip == null) {
                                            return;
                                        }
                                        Trip.STATUS newStatus = newTrip.getStatus();

                                        if (tripRequest.nextStatusValid(newStatus)) {
                                            Log.d(TAG, newTrip.getStatus().toString());

                                        }
                                        Trip sessionTrip = App.getModel().getSessionTrip();
                                        if (sessionTrip != null) {
                                            sessionTrip.setStatus(newStatus);
                                            App.getModel().setSessionTrip(sessionTrip);
                                        }
                                    }});
                       App.getModel().setTripListener(lr);
                    }
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    listener.onCompletion(null, new Error("Could not submit trip request."));
                });
    }


    /* GET */
    /**
     * Get a rider object from Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will pass the rider object from Firebase to the listener.
     * NOTE: The docID passed in is the same as the one used when a Firebase Authentication account was created
     * @param docID the doc id of the user
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
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
    /**
     * Get a driver object from Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will pass the driver object from Firebase to the listener.
     * NOTE: The docID passed in is the same as the one used when a Firebase Authentication account was created
     * @param docID the doc id of the user
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
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
    /**
     * Get a Trip object from Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will pass the trip object from Firebase to the listener.
     * @param docID the doc id of the user
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
    public void getTrip(String docID, EventCompletionListener listener, boolean listenForUpdates) {
        collectionTrip.document(docID)
                .get().addOnSuccessListener(documentSnapshot -> {
            HashMap<String, Trip> toReturn = new HashMap<>();
            Trip t = documentSnapshot.toObject(Trip.class);
            toReturn.put("trip", t);

            if (listenForUpdates && t != null) {
                ListenerRegistration lr =
                collectionTrip
                        .document(t.getRiderID())
                        .addSnapshotListener((documentSnapshot1, e) ->
                            App.getModel().handleTripStatusChanges(t.getRiderID(), documentSnapshot1)
                        );
                App.getModel().setTripListener(lr);
            }

            listener.onCompletion(toReturn, null);
        }).addOnFailureListener((@NonNull Exception e) -> {
            Log.d(TAG, e.getMessage());
            listener.onCompletion(null, new Error("Login failed. Please try again," +
                    "if the issue persists, close and restart the app."));
        });
    }
    /**
     * Get all trip objects from Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will pass all the objects from Firebase to the listener in the form of a list.
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
    public void getTrips(EventCompletionListener listener) {
        collectionTrip.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> data = queryDocumentSnapshots.getDocuments();
            HashMap<String, List<Trip>> toReturn = new HashMap<>();
            List<Trip> tripData = new LinkedList<>();
            for (DocumentSnapshot snapshot : data) {
                tripData.add(snapshot.toObject(Trip.class));
            }
            toReturn.put("all-trips", tripData);

            collectionTrip.addSnapshotListener(
                (queryDocumentSnapshots1, e) -> {
                    if (queryDocumentSnapshots1 != null) {
                        tripData.clear();
                        toReturn.clear();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots1) {
                            tripData.add(snapshot.toObject(Trip.class));
                        }
                        toReturn.put("all-trips", tripData);
                        listener.onCompletion(toReturn, null);
                    }
                });

            listener.onCompletion(toReturn, null);
        }).addOnFailureListener(e -> {
            listener.onCompletion(null, new Error(e.getMessage()));
        });
    }

    /* UPDATE */
    /**
     * Update a rider object in Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will return null once Firebase call ends
     * @param docID the document id of the rider object that is being updated
     * @param updatedRider the rider object that will update the existing rider object in Firebase
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
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
    /**
     * Update a driver object in Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will return null once Firebase call ends
     * @param docID the document id of the driver object that is being updated
     * @param updatedDriver driver object that will update the existing driver object in Firebase
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
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
    /**
     * Update a trip object in Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will get the updates
     * @param docID the document id of the trip object that is being updated
     * @param updatedTrip object that will update the existing trip object in Firebase
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     * @param listenForUpdates adds a snapshot listener if true
     */
    public void updateTrip(String docID, Trip updatedTrip, EventCompletionListener listener, boolean listenForUpdates) {
        collectionTrip.document(docID)
                .set(updatedTrip, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    if (listenForUpdates) {
                        ListenerRegistration lr =
                        collectionTrip
                                .document(updatedTrip.getRiderID())
                                .addSnapshotListener((documentSnapshot1, e) ->
                                        App.getModel().handleTripStatusChanges(updatedTrip.getRiderID(), documentSnapshot1)
                                );
                        App.getModel().setTripListener(lr);
                    }
                    listener.onCompletion(null, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    Error err = new Error("Failed to update trip");
                    listener.onCompletion(null, err);
                });
    }

    /* DELETE */
    /**
     * Delete a rider object in Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will return null once Firebase call ends
     * @param docID the document id of the rider object that is being deleted
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
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
    /**
     * Delete a driver object in Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will return null once Firebase call ends
     * @param docID the document id of the driver object that is being deleted
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
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
    /**
     * Delete a trip object in Firebase. If it was not successful the listener passed in will handel the exception.
     * If it was successful, the listener passed in will return null once Firebase call ends
     * @param docID the document id of the trip object that is being deleted
     * @param listener the listener that waits for the asynchronous Firebase call to finish
     */
    public void deleteTrip(String docID, EventCompletionListener listener) {
        collectionTrip.document(docID)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listener.onCompletion(null, null);
                })
                .addOnFailureListener((@NonNull Exception e) -> {
                    Log.d(TAG, e.getMessage());
                    Error err = new Error("Failed to delete trip");
                    listener.onCompletion(null, err);
                });
    }

}