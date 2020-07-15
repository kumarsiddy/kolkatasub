package com.freakydevs.kolkatalocal.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.utils.Constants;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;

public class LastActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        if (SharedPreferenceManager.isFromAdClosed(this)) {
            showContentAndFinish();
        } else {
            startMainActivity();
        }
    }

    private void showContentAndFinish() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, Constants.TIME_TO_SHOW_LAST_SCREEN);
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferenceManager.setFromAdClosed(this, false);
    }
}
