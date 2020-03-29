package com.adamm.queueme.fragments;

import androidx.fragment.app.Fragment;

import com.adamm.queueme.MainActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyQueuesFragment extends QueuesFragment {
    CollectionReference userQueueCollection = FirebaseFirestore.getInstance().collection("users").document(MainActivity.currUser.getUid()).collection("Queue");
    public MyQueuesFragment() {}

    @Override
    public Query getQuery() {
        return userQueueCollection.orderBy("timestamp");
    }
}
