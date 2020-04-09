package com.adamm.queueme.entities;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

//Queue is a document stored inside Stores/store-name/Queue/ Sub-collection
public class Queue {
    private String userName;
    private String storeID;
    private String userID;
    private @ServerTimestamp
    Date timestamp;
    private boolean isValid;

    public Queue() {
    }

    public Queue(String userName, String userID, String storeID, Date timestamp) {
        this.userName = userName;
        this.timestamp = timestamp;
        this.userID = userID;
        this.storeID = storeID;
        isValid = true;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
