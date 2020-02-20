package com.example.buber.Views.Activities.Models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class DatabaseManager {

    private static final String TAG = "In Database Manager";

    private FirebaseFirestore database;      // Database connection

    private CollectionReference collectionDriver, collectionRider, collectionTrip;
    private ArrayList<Driver> dataDriver; //Array of class objects of Driver
    private ArrayList<Rider> dataRider; //Array of class objects of Rider
    private ArrayList<Trip> dataTrip; //Array of class objects of Trips


    public DatabaseManager() {
        //_______________________Initialize Firebase adn collections _______________________________
        database = FirebaseFirestore.getInstance();
        collectionDriver = database.collection("Driver");
        collectionRider = database.collection("Rider");
        collectionTrip = database.collection("Trip");

        dataDriver = new ArrayList<>();
        dataRider = new ArrayList<>();
        dataTrip = new ArrayList<>();
    }

    public void populateAllTrips() {
        //ToDo: List of all trips
    }
    public void populateAllDrivers() {
        //ToDo: List of all trips
    }
    public void populateAllRiders() {
        //ToDo: List of all trips
    }
    public void createRider() {
        //ToDo: Create a rider in db
    }
    public void createDriver() {
        //ToDo: Create a Driver in db
    }
    public void createTrip() {
        //ToDo: Create a trip  in db where @param: UserID driver, UserID rider
    }


}