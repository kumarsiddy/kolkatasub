package com.freakydevs.kolkatalocal.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.adapter.TrainListAdapter;
import com.freakydevs.kolkatalocal.interfaces.TrainListInterface;
import com.freakydevs.kolkatalocal.models.HistoryFromTo;
import com.freakydevs.kolkatalocal.models.Train;
import com.freakydevs.kolkatalocal.resources.CustomAdListener;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

/**
 * Created by PURUSHOTAM on 11/3/2017.
 */

public class TrainListActivity extends AppCompatActivity implements TrainListInterface {

    private Context context;
    private RecyclerView recyclerTrainList;
    private AdView mAdView;
    private String fromCode, fromStation, toCode, toStation;
    private Toolbar toolbar;
    private ArrayList<Train> trains;
    private RecyclerView.LayoutManager mLayoutManager;
    private TrainListAdapter trainListAdapter;
    private HistoryFromTo fromTo;
    private KProgressHUD hud;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainlist);
        context = this;
        trains = (ArrayList<Train>) getIntent().getSerializableExtra("list");
        fromTo = (HistoryFromTo) getIntent().getSerializableExtra("fromTo");
        fromCode = fromTo.getFromCode();
        fromStation = fromTo.getFromStation();
        toCode = fromTo.getToCode();
        toStation = fromTo.getToStation();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAdView();
    }

    private void initView() {
        recyclerTrainList = findViewById(R.id.recycler_trainlist);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(fromCode + " To " + toCode);

        mAdView = findViewById(R.id.adView);

        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.6f);

        mLayoutManager = new LinearLayoutManager(context);
        recyclerTrainList.setLayoutManager(mLayoutManager);
        recyclerTrainList.setItemAnimator(new DefaultItemAnimator());
        trainListAdapter = new TrainListAdapter(context, trains, fromTo, recyclerTrainList);
        recyclerTrainList.setAdapter(trainListAdapter);
        trainListAdapter.notifyDataSetChanged();
    }

    private void initAdView() {
        if (SharedPreferenceManager.isShowAd(getApplicationContext())) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.setAdListener(new CustomAdListener(this, mAdView));
            mAdView.loadAd(adRequest);
        } else {
            mAdView.setVisibility(View.GONE);
        }
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
    public void showLoader() {
        hud.show();
    }

    @Override
    public void hideLoader() {
        if (hud.isShowing()) {
            hud.dismiss();
        }
    }
}
