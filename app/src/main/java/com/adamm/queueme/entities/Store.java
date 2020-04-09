package com.adamm.queueme.entities;

import java.util.List;

//Stores documents can be found in stores and favorites collection of a user
public class Store {
    private String storeID;
    private String storeName;
    private String storeOwner;
    private boolean isOpen;
    private List<String> ownerIdList;

    public Store(){}

    public Store(String storeID, String storeName, String storeOwner, String ownerID) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.storeOwner = storeOwner;
        this.ownerIdList.add(ownerID);
        isOpen = false;
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<String> getOwnerIdList() {
        return ownerIdList;
    }

    public void setOwnerIdList(List<String> ownerIdList) {
        this.ownerIdList = ownerIdList;
    }

    public void addOwnerID(String ownerID) {
        this.ownerIdList.add(ownerID);
    }
}
