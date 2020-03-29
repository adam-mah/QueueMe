package com.adamm.queueme.entities;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

//Queue is a document stored inside Stores/store-name/Queue/ Sub-collection
public class Queue {
    //private String userID;
    private String userName;
    private @ServerTimestamp
    Date timestamp;
    private boolean isValid;

    public Queue() {
    }

    public Queue(/*String userID,*/ String userName/*, String storeID*/) {
        //this.userID = userID;
        this.userName = userName;
        //this.storeID = storeID;
        isValid = true;
    }

   /* public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }*/

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
}
