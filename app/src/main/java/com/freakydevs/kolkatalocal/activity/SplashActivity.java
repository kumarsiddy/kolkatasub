package com.freakydevs.kolkatalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.customview.MyToast;
import com.freakydevs.kolkatalocal.utils.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.khirr.android.privacypolicy.PrivacyPolicyDialog;

import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    private static final long TIMEFORSPLASH = 800;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        handleSplashProperty();
    }

    private void handleSplashProperty() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, TIMEFORSPLASH);
    }
}
