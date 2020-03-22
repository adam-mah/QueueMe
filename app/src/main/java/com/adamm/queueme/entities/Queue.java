package com.adamm.queueme.entities;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

//Queue is a document stored inside Stores/store-name/Queue/ Sub-collection
public class Queue {
    private String userID;
    //private String storeID;
    private Date timestamp;
    private boolean isValid;

    public Queue() {}

    public Queue(String userID/*, String storeID*/) {
        this.userID = userID;
        //this.storeID = storeID;
        isValid = true;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    /*public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }*/

    @ServerTimestamp
    @Nullable
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@Nullable Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
