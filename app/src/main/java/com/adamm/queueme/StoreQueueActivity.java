package com.adamm.queueme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adamm.queueme.Holders.StoreViewHolder;
import com.adamm.queueme.entities.Store;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class StoreQueueActivity extends AppCompatActivity {
    private RecyclerView mQueueRecycler;
    public static Intent createIntent(@NonNull Context context, String storeName) {
        return new Intent().setClass(context, StoreQueueActivity.class).putExtra("EXTRA_STORE", storeName);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_queue);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mManager.setLayoutManager(mManager);

        attachRecyclerViewAdapter();//Initiate adapter
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                adapter.smoothScrollToPosition(0);
            }
        });

        mQueueRecycler.setAdapter(adapter);
    }

    @NonNull
    private RecyclerView.Adapter newAdapter() {
        Query query = queueCollection.orderBy("storeName");//Get favorite stores list and sort by name

        FirestoreRecyclerOptions<Store> options = new FirestoreRecyclerOptions.Builder<Store>()
                .setQuery(query, Store.class).setLifecycleOwner(this)
                .build();

        return new FirestoreRecyclerAdapter<Store, StoreViewHolder>(options) {
            @NonNull
            @Override
            public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new StoreViewHolder(inflater, parent);//Create a PostViewHolder for each post
            }

            @Override
            protected void onBindViewHolder(StoreViewHolder holder, int position, Store store) {
                holder.bind(store, true);//All queried results are favorites
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
                // mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
}
