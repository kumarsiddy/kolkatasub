package com.freakydevs.kolkatalocal.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.adapter.RouteViewFragAdapter;
import com.freakydevs.kolkatalocal.customview.CircularTextView;
import com.freakydevs.kolkatalocal.models.Station;
import com.freakydevs.kolkatalocal.models.Train;
import com.freakydevs.kolkatalocal.resources.CustomAdListener;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class TrainRouteFragActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private String trainNo, trainName;
    private RecyclerView recyclerRouteView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RouteViewFragAdapter routeViewAdapter;
    private ArrayList<Station> stations;
    private AdView mAdView;
    private Train train;
    private TextView textTrainNo, textTrainName;
    private CircularTextView sun, mon, tue, wed, thu, fri, sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainroutefrag);
        context = this;
        stations = (ArrayList<Station>) getIntent().getSerializableExtra("list");
        trainName = getIntent().getStringExtra("trainName");
        trainNo = getIntent().getStringExtra("trainNo");
        train = (Train) getIntent().getSerializableExtra("train");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAdView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerRouteView = findViewById(R.id.recycelrrouteview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(trainName);

        mAdView = findViewById(R.id.adView);

        textTrainName = findViewById(R.id.train_name);
        textTrainNo = findViewById(R.id.train_no);
        sun = findViewById(R.id.sun);
        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        thu = findViewById(R.id.thu);
        fri = findViewById(R.id.fri);
        sat = findViewById(R.id.sat);

        textTrainNo.setText(trainNo);
        textTrainName.setText(trainName);
        setDaysColor();

        mLayoutManager = new LinearLayoutManager(context);
        recyclerRouteView.setLayoutManager(mLayoutManager);
        recyclerRouteView.setItemAnimator(new DefaultItemAnimator());
        routeViewAdapter = new RouteViewFragAdapter(context, stations);
        recyclerRouteView.setAdapter(routeViewAdapter);
        routeViewAdapter.notifyDataSetChanged();
    }

    private void initAdView() {
        if (SharedPreferenceManager.isShowAd(getApplicationContext())) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.setAdListener(new CustomAdListener(this, mAdView));
            mAdView.loadAd(adRequest);
            mAdView.setVisibility(View.VISIBLE);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    private void setDaysColor() {
        sun.setStrokeWidth(1);
        sun.setSolidColor("#ffffff");
        mon.setStrokeWidth(1);
        mon.setSolidColor("#ffffff");
        tue.setStrokeWidth(1);
        tue.setSolidColor("#ffffff");
        wed.setStrokeWidth(1);
        wed.setSolidColor("#ffffff");
        thu.setStrokeWidth(1);
        thu.setSolidColor("#ffffff");
        fri.setStrokeWidth(1);
        fri.setSolidColor("#ffffff");
        sat.setStrokeWidth(1);
        sat.setSolidColor("#ffffff");

        if (train.isSun()) {
            sun.setStrokeColor("#8ecf93");
        } else {
            sun.setStrokeColor("#ea6160");
        }

        if (train.isMon()) {
            mon.setStrokeColor("#8ecf93");
        } else {
            mon.setStrokeColor("#ea6160");
        }

        if (train.isTue()) {
            tue.setStrokeColor("#8ecf93");
        } else {
            tue.setStrokeColor("#ea6160");
        }

        if (train.isWed()) {
            wed.setStrokeColor("#8ecf93");
        } else {
            wed.setStrokeColor("#ea6160");
        }

        if (train.isThu()) {
            thu.setStrokeColor("#8ecf93");
        } else {
            thu.setStrokeColor("#ea6160");
        }

        if (train.isFri()) {
            fri.setStrokeColor("#8ecf93");
        } else {
            fri.setStrokeColor("#ea6160");
        }

        if (train.isSat()) {
            sat.setStrokeColor("#8ecf93");
        } else {
            sat.setStrokeColor("#ea6160");
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
}
