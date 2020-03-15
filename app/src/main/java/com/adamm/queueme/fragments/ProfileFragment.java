package com.adamm.queueme.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.adamm.queueme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {
    private EditText mName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private Button mBtnUpdate;
    private static final String REQUIRED = "Required";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mName = rootView.findViewById(R.id.editTextName);
        mEmail = rootView.findViewById(R.id.editTextEmail);
        mPhone = rootView.findViewById(R.id.editTextPhone);
        mPassword = rootView.findViewById(R.id.editTextPassword);
        mBtnUpdate = rootView.findViewById(R.id.btnUpdateProfile);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mName.setText(user.getDisplayName());
        mEmail.setText(user.getEmail());
        mPhone.setText(user.getPhoneNumber());
        mBtnUpdate.setOnClickListener(updateProfileListener);
    }

    public ProfileFragment()
    {
    }

    public static ProfileFragment newInstance()
    {
        ProfileFragment f = new ProfileFragment();
        return f;
    }

    private void updateProfile()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName.getText().toString()).build();
        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Profile update", "Profile updated successfully");
            }
        });

        //user.updateEmail(mEmail.getText().toString());
    }

    private View.OnClickListener updateProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(TextUtils.isEmpty(mName.getText().toString()))
            {
                mName.setError(REQUIRED);
                return;
            }
            if(TextUtils.isEmpty(mEmail.getText().toString()))
            {
                mEmail.setError(REQUIRED);
                return;
            }
            else if(!isValidEmail(mEmail.getText().toString()))
            {
                mEmail.setError("Invalid email format");
                return;
            }
            if(TextUtils.isEmpty(mPhone.getText().toString()))
            {
                mPhone.setError(REQUIRED);
                return;
            }
            else if(mPhone.getText().toString().length() < 10) {
                mPhone.setError("Invalid phone number");
                return;
            }
            updateProfile();
        }
    };

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
