package com.freakydevs.kolkatalocal.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.adapter.PassengerListAdapter;
import com.freakydevs.kolkatalocal.models.Passenger;
import com.freakydevs.kolkatalocal.resources.CustomAdListener;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PnrStatusActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RecyclerView pnrRecyclerView;
    private JSONObject jsonObject;
    private String stringPnrNo;
    private TextView trainNo, trainName, fromCode, fromStation, toCode, toStation, departTime, arrivalTime, chartPrepared, trainType;
    private ListView passengerListView;
    private ArrayList<Passenger> passengers;
    private PassengerListAdapter passengerListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnr_status);
        context = this;
        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        stringPnrNo = getIntent().getStringExtra("pnr");
        initView();
        initViewData();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        pnrRecyclerView = findViewById(R.id.recycler_trainlist);
        toolbar = findViewById(R.id.toolbar);
        trainNo = findViewById(R.id.train_no);
        trainName = findViewById(R.id.train_name);
        fromCode = findViewById(R.id.from_code);
        fromStation = findViewById(R.id.from_station);
        toCode = findViewById(R.id.to_code);
        toStation = findViewById(R.id.to_station);
        departTime = findViewById(R.id.depart_time);
        arrivalTime = findViewById(R.id.arrival_time);
        chartPrepared = findViewById(R.id.chart_prepared);
        passengerListView = findViewById(R.id.passenger_list);
        trainType = findViewById(R.id.train_type);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("PNR " + stringPnrNo);

        mAdView = findViewById(R.id.adView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAdView();
    }

    private void initViewData() {
        try {
            trainNo.setText(jsonObject.getString("train_number"));
            trainName.setText(jsonObject.getString("train_name"));
            fromCode.setText(jsonObject.getString("reserved_from"));
            fromStation.setText(jsonObject.getString("board_from_name"));
            toCode.setText(jsonObject.getString("reserved_upto"));
            toStation.setText(jsonObject.getString("board_to_name"));
            departTime.setText(formatDate(jsonObject.getString("arrival_time")));
            arrivalTime.setText(formatDate(jsonObject.getString("reached_time")));
            trainType.setText(jsonObject.getString("train_type"));

            if (jsonObject.getBoolean("chart_prepared")) {
                chartPrepared.setText("Chart Prepared");
            } else {
                chartPrepared.setText("Chart Not Prepared");
            }

            JSONArray jsonArray = jsonObject.getJSONArray("passenger");
            passengers = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Passenger passenger = new Passenger();
                passenger.setSeat_number(object.getString("seat_number"));
                passenger.setStatus(object.getString("status"));
                passengers.add(passenger);
            }

            passengerListAdapter = new PassengerListAdapter(context, passengers);
            passengerListView.setAdapter(passengerListAdapter);
            passengerListAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdView() {
        if (SharedPreferenceManager.isShowAd(getApplicationContext())) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.setAdListener(new CustomAdListener(this, mAdView));
            mAdView.loadAd(adRequest);
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
            mInterstitialAd.loadAd(adRequest);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String formatDate(String dateString) {
        String returnedDate = null;
        String[] strings = dateString.split("T");
        String[] timeStrings = strings[1].split("\\+");
        try {
            DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dFormatFinal = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = dFormat.parse(strings[0]);
            returnedDate = dFormatFinal.format(date);
            return timeStrings[0] + "\n" + returnedDate;
        } catch (Exception e) {
            e.printStackTrace();
            return returnedDate;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd != null && mInterstitialAd.isLoaded() && SharedPreferenceManager.isShowAd(getApplicationContext())) {
            mInterstitialAd.show();
        }
    }
}
