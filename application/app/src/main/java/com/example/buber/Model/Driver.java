package com.example.buber.Model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple Driver class that holds the Driver properties and is the link to Fb "Diver" collection
 */
public class Driver extends User {

    private double rating;

    private double numThumbsUp;

    private double numThumbsDown;

    private boolean driverLoggedOn;

    private List<String> acceptedTripIds;

    @DocumentId
    private String docID;

    @ServerTimestamp
    public Date timestamp;

    /**
     * Empty constructor used for Firebase
     */
    public Driver() {
        setType(TYPE.DRIVER);
        driverLoggedOn=false;
    }
    /**
     * Driver constructor
     * @param username,account the drivers account and username
     */
    public Driver(String username, Account account) {
        super(username, account);
        setType(TYPE.DRIVER);
        rating = 0;
        numThumbsUp = 0;
        numThumbsDown = 0;
        acceptedTripIds = new LinkedList<>();
    }

    public double getRating() {
        return rating;
    }

    public double getNumThumbsUp(){return numThumbsUp; }
    public double getNumThumbsDown(){return numThumbsDown;}
    public void setNumThumbsUp(double up){this.numThumbsUp = up;}
    public void setNumThumbsDown(double down){this.numThumbsDown = down;}


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

    public List<String> getAcceptedTripIds() {
        return acceptedTripIds;
    }

    public void setAcceptedTripIds(List<String> acceptedTripIds) {
        this.acceptedTripIds = acceptedTripIds;
    }
}
