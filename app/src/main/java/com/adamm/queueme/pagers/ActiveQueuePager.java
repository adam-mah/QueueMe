package com.adamm.queueme.pagers;

import com.adamm.queueme.MainActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ActiveQueuePager extends QueuePagerAbstract {
    private CollectionReference userQueueCollection = FirebaseFirestore.getInstance().collection("users").document(MainActivity.currUser.getUid()).collection("Queue");
    public ActiveQueuePager() {}

    @Override
    public Query getQuery() {
        return userQueueCollection.whereEqualTo("valid", true).orderBy("timestamp");
    }
}
