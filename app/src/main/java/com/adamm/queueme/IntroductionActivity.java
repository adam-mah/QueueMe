package com.adamm.queueme;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import java.util.Arrays;

public class IntroductionActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                .title("Book yourself")
                .description("Now you can book yourself into your favorite store!")
                .image(R.drawable.ic_launcher_foreground)
                .background(R.color.colorAccent)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());

        setButtonCtaLabel("Login");
        setButtonCtaClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"HEEY", Toast.LENGTH_SHORT).show();
            }
        });
        setButtonCtaVisible(true);
    }
}
