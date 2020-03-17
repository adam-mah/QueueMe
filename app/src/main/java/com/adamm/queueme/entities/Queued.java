package com.adamm.queueme.entities;

import com.google.firebase.Timestamp;

//Queued is a document stored inside Stores/store-name/Queue/ Sub-collection
public class Queued {
    private String userID;
    private String storeID;
    private Timestamp timestamp;
    private boolean isValid;

    public Queued() {
    }

    public Queued(String userID, String storeID) {
        this.userID = userID;
        this.storeID = storeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
