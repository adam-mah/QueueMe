package com.adamm.queueme;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class NewQueueActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mTimePickerBtn;
    private int mHour, mMinute;
    TextInputEditText mTxtTime;
    TextInputLayout mTxtTimeLayout;


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
        mTxtTimeLayout = findViewById(R.id.txtTimeInput);
        mTimePickerBtn.setOnClickListener(this);
        //mTxtTime.setOnClickListener(this);
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
}
