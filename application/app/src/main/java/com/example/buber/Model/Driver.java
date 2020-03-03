package com.example.buber.Model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * A simple Driver class that holds the Driver properties and is the link to Fb "Diver" collection
 */
public class Driver extends User {

    private int rating = 5;

    @DocumentId
    private String docID;

    @ServerTimestamp
    public Date timestamp;

    public Driver() {}

    public Driver(String username, Account account) {
        super(username, account);
        setType(TYPE.DRIVER);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
}
