package com.adamm.queueme;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adamm.queueme.entities.Queue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewQueueActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mTimePickerBtn, mQueueMeBtn;
    private int mHour, mMinute;
    private String documentID;
    private TextInputEditText mTxtTime, mTxtName;
    private TextInputLayout mTxtTimeLayout;
    private TextInputEditText mTxtDate;
    private CollectionReference userQueueCollection = FirebaseFirestore.getInstance().collection("users").document(MainActivity.currUser.getUid()).collection("Queue");
    private CollectionReference storeQueueCollection;

    public static Intent createIntent(@NonNull Context context, String storeID) {
        return new Intent().setClass(context, NewQueueActivity.class)
                .putExtra("EXTRA_STORE", storeID);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_queue);
        mTimePickerBtn = findViewById(R.id.btnSelectTime);
        mTxtTime = findViewById(R.id.txtTimeText);
        mTxtDate = findViewById(R.id.txtDateText);
        mTxtTimeLayout = findViewById(R.id.txtTimeInput);
        mTxtName = findViewById(R.id.txtNameText);
        mQueueMeBtn = findViewById(R.id.btnQueueMe);

        mTimePickerBtn.setOnClickListener(this);
        mTxtTime.setOnClickListener(this);
        mTxtTimeLayout.setOnClickListener(this);
        mQueueMeBtn.setOnClickListener(queueMeClick);

        mTxtName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        documentID = getIntent().getStringExtra("EXTRA_STORE");
        storeQueueCollection = FirebaseFirestore.getInstance().collection("stores").document(documentID).collection("Queue");

        //Get current date
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        mTxtDate.setText(formattedDate);
    }

    @Override
    public void onClick(View view) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        mTxtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();
    }


    private View.OnClickListener queueMeClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Queue newQueue = new Queue(mTxtName.getText().toString());
            storeQueueCollection.add(newQueue).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    finish();
                }
            });
            userQueueCollection.document(documentID).set(newQueue);//Add queue to user Queue Collection using storeID key
            mQueueMeBtn.setEnabled(false);
        }
    };
}
