package com.example.buber.DB;
        import com.example.buber.Model.Driver;
        import com.example.buber.Model.Rider;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.FirebaseFirestore;

public class DBManager {

    private static final String TAG = "In Database Manager";

    private FirebaseFirestore database;      // Database connection

    private CollectionReference collectionDriver, collectionRider, collectionTrip;
//    private ArrayList<Driver> dataDriver; //Array of class objects of Driver
//    private ArrayList<Rider> dataRider; //Array of class objects of Rider
//    private ArrayList<Trip> dataTrip; //Array of class objects of Trips


    public  DBManager() {
        //_______________________Initialize Firebase adn collections _______________________________
        database = FirebaseFirestore.getInstance();
        collectionDriver = database.collection("Driver");
        collectionRider = database.collection("Rider");
        collectionTrip = database.collection("Trip");
        // TODO: Remove, return result of any query instead
//        dataDriver = new ArrayList<>();
//        dataRider = new ArrayList<>();
//        dataTrip = new ArrayList<>();
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

}