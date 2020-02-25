package com.example.buber.Model;


import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Rider extends User {

    @DocumentId
    private String docID;

    @ServerTimestamp
    public Date timestamp;

    public Rider() {
        setType(TYPE.Riders);
    }

    public Rider(String username, Account account) {
        super(username, account);
        setType(TYPE.Riders);
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
