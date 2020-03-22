package com.adamm.queueme.Holders;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adamm.queueme.R;
import com.adamm.queueme.StoreQueueActivity;
import com.adamm.queueme.entities.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static final CollectionReference favCollection = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Favorites");
    private final TextView mStoreNameField;
    private final TextView mStoreOwnerField;
    private final ImageView mFavImage;
    private String documentID;
    private boolean isFavorite;
    private Store store;

    public StoreViewHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.item_store, parent, false));
        mStoreNameField = itemView.findViewById(R.id.txtStoreName);
        mStoreOwnerField = itemView.findViewById(R.id.txtStoreOwner);
        mFavImage = itemView.findViewById(R.id.imgStar);
        itemView.setOnClickListener(this);
    }

    public void bind(final Store storeO, boolean isFavored, final String documentID) {
        this.documentID = documentID;
        this.store = storeO;//Store data
        this.isFavorite = isFavored;//If store is favored
        mStoreNameField.setText(store.getStoreName());
        mStoreOwnerField.setText(store.getStoreOwner());

        if (isFavorite)//If store is favored display appropriate img
            mFavImage.setImageResource(R.drawable.ic_star);
        else
            mFavImage.setImageResource(R.drawable.ic_star_outline);

        mFavImage.setOnClickListener(new View.OnClickListener() {//Managing clicks to favorite each store add/remove from favorites
            @Override
            public void onClick(final View view) {
                if (isFavorite) {//Remove from favorites
                    isFavorite = false;
                    favCollection.document(documentID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mFavImage.setImageResource(R.drawable.ic_star_outline);
                            Toast.makeText(view.getContext(), store.getStoreName() + " was removed from favorites", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {//Add to favorites
                    isFavorite = true;
                    favCollection.document(documentID).set(store).addOnCompleteListener(new OnCompleteListener<Void>() {//Add to favorites and change icon on finish
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mFavImage.setImageResource(R.drawable.ic_star);
                            Toast.makeText(view.getContext(), store.getStoreName() + " was added to favorites", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = StoreQueueActivity.createIntent(view.getContext(), documentID);
        intent.putExtra("storeName", store.getStoreName());//fix
        view.getContext().startActivity(intent);
        Toast.makeText(view.getContext(), "Clicked on " + store.getStoreName() + " ID" + documentID, Toast.LENGTH_LONG).show();
    }
}
