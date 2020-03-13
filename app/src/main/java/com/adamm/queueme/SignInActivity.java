package com.adamm.queueme;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.FloatRange;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import java.util.Arrays;


public class SignInActivity extends IntroActivity {
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);

        addSlide(new SimpleSlide.Builder()
                .title("Tired of waiting?")
                .description("No need to wait anymore!")
                .image(R.drawable.ic_launcher_foreground)
                .background(R.color.colorAccent)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Test")
                .description("hety")
                .image(R.drawable.ic_launcher_foreground)
                .background(R.color.colorAccent)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());

        setButtonCtaVisible(true);
        setButtonCtaLabel("Login");
        setButtonCtaClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"HEEY", Toast.LENGTH_SHORT).show();
                startActivityForResult(
                        // Get an instance of AuthUI based on the default app
                        AuthUI.getInstance()
                                .createSignInIntentBuilder().setLogo(R.drawable.ic_qme2)
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.PhoneBuilder()
                                                .setDefaultCountryIso("il")
                                                .setWhitelistedCountries(Arrays.asList("il")).build()))
                                .build(), RC_SIGN_IN);
            }
        });
        setButtonCtaVisible(true);
       // if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            /*startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance()
                            .createSignInIntentBuilder().setLogo(R.drawable.ic_qme2)
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.PhoneBuilder()
                                            .setDefaultCountryIso("il")
                                            .setWhitelistedCountries(Arrays.asList("il")).build()))
                            .build(),
                    RC_SIGN_IN);*/
       // }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {

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
    }
}
