package com.adamm.queueme.entities;

import java.util.List;

//User is a document inside collection users
public class User {
    private String userID;
    private String Name;
    private String Phone;
    private String Email;
    //private boolean isOwner;
    private List storesList;

    public User(){}

    public User(String userID, String name, String phone, String email) {
        this.userID = userID;
        Name = name;
        Phone = phone;
        Email = email;
        //isOwner = false;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    // public boolean isOwner() {
    //   return isOwner;
    // }

    // public void setOwner(boolean owner) {
    //     isOwner = owner;
    //  }

    public List getStoresList() {
        return storesList;
    }

    public void setStoresList(List storesList) {
        this.storesList = storesList;
    }
}


