package com.example.buber.Model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * A simple Driver class that holds the Driver properties and is the link to Fb "Diver" collection
 */
public class Driver extends User {

    private double rating;

    private boolean driverLoggedOn;

    @DocumentId
    private String docID;

    @ServerTimestamp
    public Date timestamp;

    /**Driver(...) methods are constructors for Driver class. It */
    public Driver() { setType(TYPE.DRIVER); driverLoggedOn=false;}

    public Driver(String username, Account account) {
        super(username, account);
        setType(TYPE.DRIVER);
        rating = 3.0;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getDriverLoggedOn() {
        return driverLoggedOn;
    }

    public void setLoggedOn(boolean loggedOn) {
        driverLoggedOn = loggedOn;
    }
}
