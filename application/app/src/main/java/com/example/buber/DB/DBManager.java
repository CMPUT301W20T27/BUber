package com.example.buber.DB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DBManager {

    private AuthDBManager login;

    private static final String TAG = "In Database Manager";

    private FirebaseFirestore database;      // Database connection

    private CollectionReference collectionDriver, collectionRider, collectionTrip;

    public  DBManager() {

        login = new AuthDBManager();

        database = FirebaseFirestore.getInstance();
        collectionDriver = database.collection("Drivers");
        collectionRider = database.collection("Riders");
        collectionTrip = database.collection("Trips");
    }

            // TODO: Implement Create, Read, ReadAll, Update and Delete for any document in the Driver,
            // TODO: Remove populate -> replace w/ getAll
            //ToDo: List of all trips
            //ToDo: List of all trips
            //ToDo: List of all trips

    public void createRider() {
        //ToDo: Create a rider in db
    }
    public void createDriver() {
        //ToDo: Create a Driver in db
    }
    public void createTrip() {
        //ToDo: Create a trip  in db where @param: UserID driver, UserID rider
    }
    public void updateRider() {
        //ToDo: Create a rider in db
    }
    public void  updateDriver() {
        //ToDo: Create a Driver in db
    }
    public void  updateTrip() {
        //ToDo: Create a trip  in db where @param: UserID driver, UserID rider
    }


}