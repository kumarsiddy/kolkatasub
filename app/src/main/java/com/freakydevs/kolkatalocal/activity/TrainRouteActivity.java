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

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.adapter.RouteViewAdapter;
import com.freakydevs.kolkatalocal.models.HistoryFromTo;
import com.freakydevs.kolkatalocal.models.Station;
import com.freakydevs.kolkatalocal.resources.CustomAdListener;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class TrainRouteActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private HistoryFromTo fromTo;
    private String trainNo, trainName;
    //    private TextView textTrainNo, textTrainName;
    private RecyclerView recyclerRouteView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RouteViewAdapter routeViewAdapter;
    private ArrayList<Station> stations;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainroute);
        fromTo = (HistoryFromTo) getIntent().getSerializableExtra("fromTo");
        trainNo = getIntent().getStringExtra("trainNo");
        trainName = getIntent().getStringExtra("trainName");
        stations = (ArrayList<Station>) getIntent().getSerializableExtra("list");
        context = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAdView();
    }

    private void initView() {

        toolbar = findViewById(R.id.toolbar);
        /*textTrainNo = findViewById(R.id.train_no);
        textTrainName = findViewById(R.id.train_name);*/
        recyclerRouteView = findViewById(R.id.recycelrrouteview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(trainName);
//        textTrainName.setText(trainName);
//        textTrainNo.setText(trainNo);

        mAdView = findViewById(R.id.adView);

        mLayoutManager = new LinearLayoutManager(context);
        recyclerRouteView.setLayoutManager(mLayoutManager);
        recyclerRouteView.setItemAnimator(new DefaultItemAnimator());
        routeViewAdapter = new RouteViewAdapter(context, fromTo, stations);
        recyclerRouteView.setAdapter(routeViewAdapter);
        routeViewAdapter.notifyDataSetChanged();
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
}
