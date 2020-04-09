package com.adamm.queueme.Holders;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.adamm.queueme.MainActivity;
import com.adamm.queueme.R;
import com.adamm.queueme.entities.Queue;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;

public class QueueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView mTimestampField;
    private final TextView mCustomerNameField;
    private final ImageView mImgCancel;
    private final String TAG = "QueueViewHolder";
    private String documentID;
    private Queue queue;

    public QueueViewHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.item_queue, parent, false));
        mCustomerNameField = itemView.findViewById(R.id.txtCustomerName);
        mTimestampField = itemView.findViewById(R.id.txtQueueTime);
        mImgCancel = itemView.findViewById(R.id.imgCancel);
        itemView.setOnClickListener(this);
        mImgCancel.setOnClickListener(imgCancelListener);
    }

    public void bind(final Queue queue, final String documentID) {
        this.documentID = documentID;
        this.queue = queue;//Queue data
        mCustomerNameField.setText(queue.getUserName());

        if (queue.isValid())
            mImgCancel.setVisibility(View.VISIBLE);
        else
            mImgCancel.setVisibility(View.INVISIBLE);

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        // timeFormat.format(queue.getTimestamp());
        String[] str = timeFormat.format(queue.getTimestamp()).split(" ");
        mTimestampField.setText("Date: " + str[0] + " Time: " + str[1]);
    }

    public void bind(final Queue queue, final String documentID, final boolean isActivity) {//A simple workaround binder to hide cancel button from StoreQueueActivity
        this.documentID = documentID;//Queue document ID
        this.queue = queue;//Queue data
        mCustomerNameField.setText(queue.getUserName());

        mImgCancel.setVisibility(View.INVISIBLE);

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        // timeFormat.format(queue.getTimestamp());
        String[] str = timeFormat.format(queue.getTimestamp()).split(" ");
        mTimestampField.setText("Date: " + str[0] + " Time: " + str[1]);
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked on Queue ID: " + documentID, Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener imgCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder builder = new MaterialAlertDialogBuilder(MainActivity.appContext)
                    .setTitle("Cancel Queue")
                    .setMessage("Are you sure you want to cancel this queue?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CollectionReference storeQueueCollection = FirebaseFirestore.getInstance().collection("stores").document(queue.getStoreID()).collection("Queue");
                            CollectionReference userQueueCollection = FirebaseFirestore.getInstance().collection("users").document(MainActivity.currUser.getUid()).collection("Queue");

                            FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    //DocumentSnapshot snapshot = transaction.get(userQueueCollection.document(documentID));
                                    storeQueueCollection.document(documentID).delete();
                                    transaction.update(userQueueCollection.document(documentID), "valid", false);

                                    // Success
                                    return null;
                                }
                            }).addOnSuccessListener(aVoid -> Log.d(TAG, "Transaction success!"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Transaction failure.", e));
                        }
                    });
            builder.show();
        }
    };
}