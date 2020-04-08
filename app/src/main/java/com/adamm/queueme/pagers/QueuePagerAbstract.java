package com.adamm.queueme.pagers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adamm.queueme.Holders.QueueViewHolder;
import com.adamm.queueme.R;
import com.adamm.queueme.entities.Queue;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public abstract class QueuePagerAbstract extends Fragment{
    public abstract Query getQuery();

    private RecyclerView mMyQueuesRecycler;
    private TextView mEmptyListMessage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.layout_pager, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMyQueuesRecycler = view.findViewById(R.id.myQueuesRecycler);
        mEmptyListMessage = view.findViewById(R.id.txtEmptyMessage);
        LinearLayoutManager mManager = new LinearLayoutManager(view.getContext());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mMyQueuesRecycler.setLayoutManager(mManager);

        attachRecyclerViewAdapter();//Initiate adapter
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mMyQueuesRecycler.smoothScrollToPosition(0);
            }
        });

        mMyQueuesRecycler.setAdapter(adapter);
    }

    @NonNull
    private RecyclerView.Adapter newAdapter() {
        Query query = getQuery();

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
                 mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }
}
