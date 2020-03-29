package com.adamm.queueme.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adamm.queueme.MainActivity;
import com.adamm.queueme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.regex.Pattern;

import static com.firebase.ui.auth.AuthUI.EMAIL_LINK_PROVIDER;

public class ProfileFragment extends Fragment {
    private EditText mName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private Button mBtnUpdateProfile;
    private Button mBtnUpdatePassword;
    private FirebaseUser user = MainActivity.currUser;
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
        mBtnUpdateProfile = rootView.findViewById(R.id.btnUpdateProfile);
        mBtnUpdatePassword = rootView.findViewById(R.id.btnUpdatePassword);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mName.setText(user.getDisplayName());
        mName.addTextChangedListener(fieldWatcher);
        mEmail.setText(user.getEmail());
        mEmail.addTextChangedListener(fieldWatcher);
        mPhone.setText(user.getPhoneNumber());
        mPhone.addTextChangedListener(fieldWatcher);
        mBtnUpdateProfile.setOnClickListener(updateProfileListener);
        mBtnUpdateProfile.setEnabled(false);
        mBtnUpdatePassword.setOnClickListener(updatePasswordListener);
        mBtnUpdatePassword.setEnabled(false);
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TextUtils.isEmpty(mPassword.getText().toString()))
                    mBtnUpdatePassword.setEnabled(false);
                else
                    mBtnUpdatePassword.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        for (UserInfo profile : user.getProviderData()) {
            // Id of the provider (ex: google.com)
            String providerId = profile.getProviderId();
            switch(providerId) {
                case EmailAuthProvider.PROVIDER_ID:
                    Snackbar.make(getView(), "Email", Snackbar.LENGTH_LONG).show();
                    break;
                case PhoneAuthProvider.PROVIDER_ID:
                    mPhone.setEnabled(false);
                    break;
            }
        }
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
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName.getText().toString()).build();
        user.updateProfile(profileChangeRequest);
        user.updateEmail(mEmail.getText().toString());
        if(!user.isEmailVerified()) {
            Snackbar.make(getView(), "Profile was updated successfully Verification was sent to your email.", Snackbar.LENGTH_LONG).show();
            user.sendEmailVerification();
        }
        Snackbar.make(getView(), "Profile was updated successfully", Snackbar.LENGTH_LONG).show();
        //PHONE NOT IMPLEMENTED YET
        mBtnUpdateProfile.setEnabled(false);
    }

    private TextWatcher fieldWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TextUtils.isEmpty(mName.getText().toString())|| TextUtils.isEmpty(mEmail.getText().toString()) || TextUtils.isEmpty(mPhone.getText().toString()))
                    mBtnUpdateProfile.setEnabled(false);
                else
                    mBtnUpdateProfile.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

    private View.OnClickListener updatePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mPassword.getText().toString().length() < 6)
                mPassword.setError(getString(R.string.invalid_password));
            else {
                user.updatePassword(mPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(getView(), "Your password was successfully updated!", Snackbar.LENGTH_LONG).show();
                        mPassword.getText().clear();
                        MainActivity.hideSoftKeyboard(getActivity());
                    }
                });
            }
        }
    };

    private View.OnClickListener updateProfileListener = new View.OnClickListener() {
        int flag;
        @Override
        public void onClick(View view) {
            flag = 0;
            if(TextUtils.isEmpty(mName.getText().toString()))
            {
                mName.setError(REQUIRED);
                flag = 1;
            }
            if(TextUtils.isEmpty(mEmail.getText().toString()))
            {
                mEmail.setHint(R.string.fui_email_hint);
                mEmail.setError(REQUIRED);
                flag = 1;
            }
            else if(!isValidEmail(mEmail.getText().toString()))
            {
                mEmail.setError(getString(R.string.invalid_email_format));
                flag = 1;
            }
            if(TextUtils.isEmpty(mPhone.getText().toString()))
            {
                mPhone.setError(REQUIRED);
                flag = 1;
            }
            else if(!isPhoneNumberValid(mPhone.getText().toString()))
            {
                mPhone.setError(getString(R.string.invalid_phone));
                flag = 1;
            }
            else if(mPhone.getText().toString().length() < 10) {
                mPhone.setError("Invalid phone number");
                flag = 1;
            }
            if(flag == 0)
                updateProfile();
        }
    };

    public static boolean isPhoneNumberValid(String phoneNumber)
    {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try
        {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, "IL");
            return phoneUtil.isValidNumber(numberProto);
        }
        catch (NumberParseException e)
        {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

        return false;
    }

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
