package com.freakydevs.kolkatalocal.resources;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.freakydevs.kolkatalocal.activity.LastActivity;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

/**
 * Created by Purushotam on 25 August 2018
 **/
public class CustomAdListener extends AdListener {

    private Context context;
    private AdView bannerAdView;
    private boolean isInterstitialAdFromMainActivty;

    public CustomAdListener(Context context, AdView bannerAdView) {
        this.context = context;
        this.bannerAdView = bannerAdView;
    }

    public CustomAdListener(Context context) {
        this.context = context;
    }

    public CustomAdListener(Context context, boolean isInterstitialAdFromMainActivty) {
        this.context = context;
        this.isInterstitialAdFromMainActivty = isInterstitialAdFromMainActivty;
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        if (bannerAdView != null)
            bannerAdView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAdClicked() {
        super.onAdClicked();
    }

    @Override
    public void onAdFailedToLoad(int i) {
        super.onAdFailedToLoad(i);
        if (bannerAdView != null)
            bannerAdView.setVisibility(View.GONE);
    }

    @Override
    public void onAdOpened() {
        super.onAdOpened();
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();
        if (isInterstitialAdFromMainActivty) {
            SharedPreferenceManager.setFromAdClosed(context, true);
            context.startActivity(new Intent(context, LastActivity.class));
            ((Activity) context).finish();
        }
    }
}
