package com.example.buber.DB;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.buber.Model.Driver;
import com.example.buber.Model.Rider;
import com.example.buber.Model.User;

import com.example.buber.Services.ApplicationServiceHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DBManager implements ApplicationServiceHelper {

    private AuthDBManager login;

    private static final String TAG = "In Database Manager";

    private FirebaseFirestore database;      // Database connection

    private CollectionReference collectionDriver, collectionRider, collectionTrip;

    public  DBManager() {

        login = new AuthDBManager();
        //_______________________Initialize Firebase adn collections _______________________________
        database = FirebaseFirestore.getInstance();
        collectionDriver = database.collection("Drivers");
        collectionRider = database.collection("Riders");
        collectionTrip = database.collection("Trips");
        // TODO: Remove, return result of any query instead

    }

    // TODO: Implement Create, Read, ReadAll, Update and Delete for any document in the Driver,
    // Rider and Trip Collection
// TODO: Remove populate -> replace w/ getAll
    //    public void populateAllTrips() {
//        //ToDo: List of all trips
//    }
//    public void populateAllDrivers() {
//        //ToDo: List of all trips
//    }
//    public void populateAllRiders() {
//        //ToDo: List of all trips
//    }
    public void createRider(String docid, Rider rider) {
        //ToDo: Create a rider in db
    }
    public void createDriver(String docid, Driver driver) {
        //ToDo: Create a Driver in db
    }
    public void createTrip() {
        //ToDo: Create a trip  in db where @param: UserID driver, UserID rider
    }
    public void updateRider(String docid) {
        //ToDo: Create a rider in db
    }
    public void  updateDriver(String docid) {
        //ToDo: Create a Driver in db
    }
    public void  updateTrip() {
        //ToDo: Create a trip  in db where @param: UserID driver, UserID rider
    }


    public void createAccount(){
        /*TODO: TODO: Get username and password from UI when user creates an account*/
        login.createAccount("makingriderteste@BUber.com", "123456" , this);    // If account is created aftersuccessfulCreataAccount() is called
    }


    @Override
    public User aftersuccessfulLoginofrider(FirebaseUser user) {
        return null;
    }

    @Override
    public User aftersuccessfulCreataAccount(FirebaseUser user) {

        collectionRider.document(user.getUid())

                .set(new Rider())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        return null;
    }

    @Override
    public User aftersuccessfulLoginofdriver(FirebaseUser user) {
        return null;
    }
}