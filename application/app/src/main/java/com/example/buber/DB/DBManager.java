package com.example.buber.DB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DBManager {

    private AuthDBManager login;

    private static final String TAG = "In Database Manager";

    private FirebaseFirestore database;      // Database connection

    private CollectionReference collectionDriver, collectionRider, collectionTrip;

    public DBManager() {

        login = new AuthDBManager();

        database = FirebaseFirestore.getInstance();
        collectionDriver = database.collection("Drivers");
        collectionRider = database.collection("Riders");
        collectionTrip = database.collection("Trips");
    }

    /* CREATE */
    public void createRider() {
        // todo
    }

    public void createDriver() {
        // todo
    }

    public void createTrip(String riderID, String driverID) {
        // todo
    }


    /* READ */
    public ArrayList getAll(String rootCollection) {
        // todo
        return new ArrayList();
    }

    public void getRider(String docID) {
        // todo
    }

    public void getDriver(String docID) {
        // todo
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