package com.example.buber.Model;


import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Represents a User that is of type Rider. Linked to our Firebase "Drivers" collection
 */
public class Rider extends User {
    @DocumentId
    private String docID;

    @ServerTimestamp
    public Date timestamp;

    private boolean riderLoggedOn;
    public Rider() {
        setType(TYPE.RIDER);
        riderLoggedOn=false;
    }

    public Rider(String username, Account account) {
        super(username, account);
        setType(TYPE.RIDER);
        riderLoggedOn = false;
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

    public boolean getRiderLoggedOn() {
        return riderLoggedOn;
    }

    public void setRiderLoggedOn(boolean isriderLoggedOn) {
        riderLoggedOn = isriderLoggedOn;
    }
}
