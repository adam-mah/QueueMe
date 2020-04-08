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
import com.adamm.queueme.MainActivity;
import com.adamm.queueme.R;
import com.adamm.queueme.entities.Store;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StoresFragment extends Fragment {

    private RecyclerView mStoresRecycler;
    private CollectionReference storesCollection =FirebaseFirestore.getInstance().collection("stores");
    private CollectionReference favCollection = FirebaseFirestore.getInstance().collection("users").document(MainActivity.currUser.getUid()).collection("Favorites");
    private List<Store> favoriteList;
    private TextView mEmptyListMessage;

    public static StoresFragment newInstance()
    {
        StoresFragment f = new StoresFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_stores, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStoresRecycler = view.findViewById(R.id.storesRecycler);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mStoresRecycler.setLayoutManager(mManager);
        mEmptyListMessage = view.findViewById(R.id.txtEmptyMessage);
        favoriteList= new ArrayList<>();//Favorite stores list
        favCollection.get() //get user favorites
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                favoriteList.add(0, document.toObject(Store.class));
                                Log.d("Favorites", document.getId() + " => " + document.getData());
                            }
                            attachRecyclerViewAdapter();//Initiate adapter after favorites list completion
                        } else {
                            Log.d("Favorites", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mStoresRecycler.smoothScrollToPosition(0);
            }
        });

        mStoresRecycler.setAdapter(adapter);
    }

    private void onAddStore(@NonNull Store store) {
        storesCollection.add(store);
    }

    @NonNull
    private RecyclerView.Adapter newAdapter() {
        Query query = storesCollection.orderBy("storeName");//Get stores list and sort by name

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
            protected void onBindViewHolder( StoreViewHolder holder, int position, Store store) {
                boolean isFavorite = false;
                for(Store fav : favoriteList)
                    if(fav.getStoreID().equals(store.getStoreID())) {
                        isFavorite = true;
                        Log.d("FoundFav", fav.getStoreID()+" ");
                        break;
                    }
                holder.bind(store, isFavorite, getSnapshots().getSnapshot(position).getId());
            }

            @Override
            public void onDataChanged() {
               mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }
}
