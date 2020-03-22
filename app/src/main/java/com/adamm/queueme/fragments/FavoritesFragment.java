package com.adamm.queueme.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adamm.queueme.Holders.StoreViewHolder;
import com.adamm.queueme.R;
import com.adamm.queueme.entities.Store;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FavoritesFragment extends Fragment {

    private RecyclerView mFavoritesRecycler;
    private TextView mEmptyListMessage;
    public static final CollectionReference favCollection = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Favorites");

    public static FavoritesFragment newInstance() {
        FavoritesFragment f = new FavoritesFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFavoritesRecycler = view.findViewById(R.id.favoritesRecycler);
        mEmptyListMessage = view.findViewById(R.id.txtEmptyMessage);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mFavoritesRecycler.setLayoutManager(mManager);

        attachRecyclerViewAdapter();//Initiate adapter
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mFavoritesRecycler.smoothScrollToPosition(0);
            }
        });

        mFavoritesRecycler.setAdapter(adapter);
    }

    @NonNull
    private RecyclerView.Adapter newAdapter() {
        Query query = favCollection.orderBy("storeName");//Get favorite stores list and sort by name

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
                holder.bind(store, true, getSnapshots().getSnapshot(position).getId());//All queried results are favorites
            }

            @Override
            public void onDataChanged() {
                // If there are no favored stores
                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }
}