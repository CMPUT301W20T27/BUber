package com.example.buber.Model;


import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Rider extends User {

    public void setDocID(String docID) {
        this.docID = docID;
    }

    @DocumentId
    private String docID;

    @ServerTimestamp
    public Date timestamp;

    public Rider() {
        setType(TYPE.RIDER);
    }

    public Rider(String username, Account account) {
        super(username, account);
        setType(TYPE.RIDER);
    }

    public String getDocID() {
        return docID;
    }
}
