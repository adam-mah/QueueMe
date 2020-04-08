package com.adamm.queueme.pagers;

import com.adamm.queueme.MainActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyQueuePager extends QueuePagerAbstract {
    CollectionReference userQueueCollection = FirebaseFirestore.getInstance().collection("users").document(MainActivity.currUser.getUid()).collection("Queue");
    public MyQueuePager() {}

    @Override
    public Query getQuery() {
        return userQueueCollection.orderBy("timestamp");
    }
}
