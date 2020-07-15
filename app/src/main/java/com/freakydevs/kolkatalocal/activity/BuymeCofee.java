package com.freakydevs.kolkatalocal.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.customview.MyToast;
import com.freakydevs.kolkatalocal.utils.Internet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.kaopiz.kprogresshud.KProgressHUD;

public class BuymeCofee extends AppCompatActivity implements RewardedVideoAdListener {

    private Context context;
    private RewardedVideoAd mRewardedVideoAd;
    private Button showAd;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buymecofee);
        context = this;

        initView();
    }

    private void initView() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        showAd = findViewById(R.id.show_ad);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.text_buy_a_cofee));

        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.6f);

        if (Internet.isConnected(context)) {
            hud.setLabel(getString(R.string.text_please_wait));
            hud.show();
        }
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getApplicationContext().getString(R.string.video_reward_id),
                new AdRequest.Builder().addTestDevice("9B30B669E98FAB6DD0486571C99DD05E").build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

        if (hud != null && hud.isShowing()) {
            hud.dismiss();
        }

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        MyToast.showToast(context, getString(R.string.text_thanks_for_treat));
        finish();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        if (hud != null && hud.isShowing()) {
            hud.dismiss();
        }
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                } else if (Internet.isConnected(context)) {
                    loadRewardedVideoAd();
                    hud.setLabel(getString(R.string.text_please_wait));
                    hud.show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRewardedVideoAd.resume(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRewardedVideoAd.pause(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRewardedVideoAd.destroy(context);
        if (hud != null && hud.isShowing())
            hud.dismiss();
    }
}
