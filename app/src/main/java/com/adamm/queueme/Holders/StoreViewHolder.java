package com.adamm.queueme.Holders;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adamm.queueme.MainActivity;
import com.adamm.queueme.R;
import com.adamm.queueme.StoreQueueActivity;
import com.adamm.queueme.entities.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private CollectionReference favCollection = FirebaseFirestore.getInstance().collection("users").document(MainActivity.currUser.getUid()).collection("Favorites");
    private final TextView mStoreNameField;
    private final TextView mStoreOwnerField;
    private final ImageView mFavImage;
    private final ImageView mStateImage;
    private String documentID;
    private boolean isFavorite;
    private Store store;

    public StoreViewHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.item_store, parent, false));
        mStoreNameField = itemView.findViewById(R.id.txtStoreName);
        mStoreOwnerField = itemView.findViewById(R.id.txtStoreOwner);
        mFavImage = itemView.findViewById(R.id.imgStar);
        mStateImage = itemView.findViewById(R.id.imgState);
        itemView.setOnClickListener(this);
    }

    public void bind(final Store storeO, boolean isFavored, final String documentID) {
        this.documentID = documentID;
        this.store = storeO;//Store data
        this.isFavorite = isFavored;//If store is favored
        mStoreNameField.setText(store.getStoreName());
        mStoreOwnerField.setText(store.getStoreOwner());

        if (isFavorite)//If store is favored display appropriate img
            mFavImage.setImageDrawable(new IconicsDrawable(MainActivity.appContext).icon(GoogleMaterial.Icon.gmd_favorite).color(Color.rgb(203, 84, 39)));
        else
            mFavImage.setImageDrawable(new IconicsDrawable(MainActivity.appContext).icon(GoogleMaterial.Icon.gmd_favorite_border).color(Color.rgb(203, 84, 39)));


        if (store.isOpen())
            mStateImage.setImageResource(R.drawable.ic_open);
        else
            mStateImage.setImageResource(R.drawable.ic_closed);

        mFavImage.setOnClickListener(mFavImageListener);
    }

    @Override
    public void onClick(View view) {
        if (store.isOpen()) {
            Intent intent = StoreQueueActivity.createIntent(view.getContext(), documentID);
            intent.putExtra("EXTRA_STORE_NAME", store.getStoreName());//fix
            view.getContext().startActivity(intent);
        } else
            Snackbar.make(view, "Store is closed at this moment", Snackbar.LENGTH_SHORT).show();
    }

    private View.OnClickListener mFavImageListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            mFavImage.setEnabled(false);
            if (isFavorite) {//Remove from favorites
                isFavorite = false;
                favCollection.document(documentID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mFavImage.setImageDrawable(new IconicsDrawable(MainActivity.appContext).icon(GoogleMaterial.Icon.gmd_favorite_border).color(Color.rgb(203, 84, 39)));
                        mFavImage.setEnabled(true);
                        Toast.makeText(view.getContext(), store.getStoreName() + " was removed from favorites", Toast.LENGTH_LONG).show();
                    }
                });
            } else {//Add to favorites
                isFavorite = true;
                favCollection.document(documentID).set(store).addOnCompleteListener(new OnCompleteListener<Void>() {//Add to favorites and change icon on finish
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mFavImage.setImageDrawable(new IconicsDrawable(MainActivity.appContext).icon(GoogleMaterial.Icon.gmd_favorite).color(Color.rgb(203, 84, 39)));
                        mFavImage.setEnabled(true);
                        Toast.makeText(view.getContext(), store.getStoreName() + " was added to favorites", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
