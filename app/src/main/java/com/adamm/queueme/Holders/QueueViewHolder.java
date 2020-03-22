package com.adamm.queueme.Holders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.adamm.queueme.R;
import com.adamm.queueme.entities.Queue;

public class QueueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //public static final CollectionReference queueCollection = FirebaseFirestore.getInstance().collection("stores").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Favorites");
    private final TextView mTimestampField;
    private final TextView mCustomerNameField;
    private String documentID;
    private Queue queue;

        public QueueViewHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.item_queue, parent, false));
        mCustomerNameField = itemView.findViewById(R.id.txtCustomerName);
        mTimestampField = itemView.findViewById(R.id.txtQueueTime);
        itemView.setOnClickListener(this);
    }

    public void bind(final Queue queue, final String documentID) {
        this.documentID = documentID;
        this.queue = queue;//Queue data
        mCustomerNameField.setText(queue.getUserID());
        mTimestampField.setText(queue.getTimestamp().toString());
    }

    @Override
    public void onClick(View view) {
        // Intent intent = StoreQueueActivity.createIntent(view.getContext(), documentID);
        //view.getContext().startActivity(intent);
        Toast.makeText(view.getContext(), "Clicked on Queue ID: " + documentID, Toast.LENGTH_LONG).show();
    }
}