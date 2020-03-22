package com.adamm.queueme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adamm.queueme.Holders.QueueViewHolder;
import com.adamm.queueme.Holders.StoreViewHolder;
import com.adamm.queueme.entities.Queue;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StoreQueueActivity extends AppCompatActivity {
    private RecyclerView mQueueRecycler;
    private CollectionReference queueCollection;
    private Toolbar toolbar;
    private Query storeQuery;

    public static Intent createIntent(@NonNull Context context, String documentID) {
        return new Intent().setClass(context, StoreQueueActivity.class).putExtra("EXTRA_STORE", documentID/*store document ID*/);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_queue);
        toolbar = findViewById(R.id.toolbar);
        mQueueRecycler = findViewById(R.id.queueRecycler);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mQueueRecycler.setLayoutManager(mManager);
        queueCollection = FirebaseFirestore.getInstance().collection("stores").document(getIntent().getStringExtra("EXTRA_STORE")).collection("Queue");
        toolbar.setTitle(getIntent().getStringExtra("storeName") + " Store Queue");
        attachRecyclerViewAdapter();//Initiate adapter
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mQueueRecycler.smoothScrollToPosition(0);
            }
        });

        mQueueRecycler.setAdapter(adapter);
    }

    @NonNull
    private RecyclerView.Adapter newAdapter() {
        Query query = queueCollection.whereEqualTo("isValid", true).orderBy("timestamp");//Get store queue list and sort by timestamp

        FirestoreRecyclerOptions<Queue> options = new FirestoreRecyclerOptions.Builder<Queue>()
                .setQuery(query, Queue.class).setLifecycleOwner(this)
                .build();

        return new FirestoreRecyclerAdapter<Queue, QueueViewHolder>(options) {
            @NonNull
            @Override
            public QueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new QueueViewHolder(inflater, parent);//Create a PostViewHolder for each post
            }

            @Override
            protected void onBindViewHolder(QueueViewHolder holder, int position, Queue queue) {
                holder.bind(queue, getSnapshots().getSnapshot(position).getId());//All queried results are favorites
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
                // mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }
}
