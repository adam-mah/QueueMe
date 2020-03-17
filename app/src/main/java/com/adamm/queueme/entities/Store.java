package com.adamm.queueme.entities;

//Stores documents can be found in stores and favorites collection of a user
public class Store {
    private String storeID;
    private String storeName;
    private String storeOwner;

    public Store(){}

    public Store(String storeID, String storeName, String storeOwner) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.storeOwner = storeOwner;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreOwner() {
        return storeOwner;
    }

    public void setStoreOwner(String storeOwner) {
        this.storeOwner = storeOwner;
    }
}
