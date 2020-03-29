package com.adamm.queueme;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.FloatRange;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import java.util.Arrays;


public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "SignInActivity";
    private Button mSignInBtn;
    private Button mLearnMoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(MainActivity.createIntent(this, null));//Signed Intent
            //Intent SignedIntent = new Intent(this, MainActivity.class);
                //startActivity(SignedIntent);
                finish();
        }

        mSignInBtn = findViewById(R.id.SignInBtn);
        mLearnMoreBtn = findViewById(R.id.LearnMoreBtn);

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(buildSignInIntent(), RC_SIGN_IN);
            }
        });

        mLearnMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent introIntent = new Intent(getApplicationContext(), IntroductionActivity.class);
                startActivityForResult(introIntent, 2);
            }
        });
    }

    protected Intent buildSignInIntent()
    {
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.custom_signin_layout)
                .setPhoneButtonId(R.id.custom_phone_signin_button)
                .setEmailButtonId(R.id.custom_email_signin_button)
                .build();
        // Get an instance of AuthUI based on the default app
        AuthUI.SignInIntentBuilder builder = AuthUI.getInstance()
                .createSignInIntentBuilder().setAuthMethodPickerLayout(customLayout).setTheme(R.style.CustomMaterialThemeNoActionBar)
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder()
                                .setDefaultCountryIso("il")
                                .setWhitelistedCountries(Arrays.asList("il")).build()));
        return builder.build();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                startActivity(MainActivity.createIntent(this, response));
                finish();
                Toast.makeText(this, "Hurray! Signed In!", Toast.LENGTH_SHORT).show();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Sign In cancelled", Toast.LENGTH_SHORT).show();;
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, R.string.signin_failed, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
        else if(requestCode == 2)//Intro
            Toast.makeText(this, "Sign in requested", Toast.LENGTH_SHORT).show();
    }
}
