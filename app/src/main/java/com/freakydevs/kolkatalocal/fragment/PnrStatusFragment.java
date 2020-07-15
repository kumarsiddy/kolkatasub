package com.freakydevs.kolkatalocal.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.adapter.PnrHistoryAdapter;
import com.freakydevs.kolkatalocal.connection.SearchPnr;
import com.freakydevs.kolkatalocal.customview.CustomAutoCompleteView;
import com.freakydevs.kolkatalocal.customview.MySnackBar;
import com.freakydevs.kolkatalocal.customview.PnrStatusAutoCompleteListener;
import com.freakydevs.kolkatalocal.interfaces.PnrStatusInterface;
import com.freakydevs.kolkatalocal.models.PnrDetails;
import com.freakydevs.kolkatalocal.resources.CustomAdListener;
import com.freakydevs.kolkatalocal.resources.ObjectSerializer;
import com.freakydevs.kolkatalocal.utils.Internet;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;

import static com.freakydevs.kolkatalocal.utils.Constants.PNR_HISTORY;

/**
 * Created by PURUSHOTAM on 11/3/2017.
 */

public class PnrStatusFragment extends Fragment implements View.OnClickListener, PnrStatusInterface {

    private View view;
    private AdView mAdView;
    private CustomAutoCompleteView pnrDetails;
    private Context context;
    private Button searchButton;
    private ConstraintLayout parentLayout;
    private ArrayList<PnrDetails> pnrDetailsList;
    private PnrHistoryAdapter pnrHistoryAdapter;
    private RecyclerView pnrHistoryRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        initPNRDetailsFromSharedPref();
    }

    private void initPNRDetailsFromSharedPref() {
        try {
            pnrDetailsList = (ArrayList<PnrDetails>) ObjectSerializer.deserialize(SharedPreferenceManager.getPnrSharedPrefs(context).getString(PNR_HISTORY, ObjectSerializer.serialize(new ArrayList<PnrDetails>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (pnrDetailsList == null)
            pnrDetailsList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pnrstatus, container, false);
        pnrDetails = view.findViewById(R.id.edit_pnrdetails);
        searchButton = view.findViewById(R.id.search_pnr);
        parentLayout = view.findViewById(R.id.parent_layout);
        pnrHistoryRecyclerView = view.findViewById(R.id.pnr_history_recycler);
        searchButton.setOnClickListener(this);
        pnrDetails.addTextChangedListener(new PnrStatusAutoCompleteListener(context, pnrDetails));

        mAdView = view.findViewById(R.id.adView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        pnrHistoryRecyclerView.setLayoutManager(mLayoutManager);
        pnrHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pnrHistoryAdapter = new PnrHistoryAdapter(context, pnrDetailsList, pnrHistoryRecyclerView, this);
        pnrHistoryRecyclerView.setAdapter(pnrHistoryAdapter);
        pnrHistoryAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdView();
    }

    private void initAdView() {
        if (SharedPreferenceManager.isShowAd(context.getApplicationContext())) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.setAdListener(new CustomAdListener(getContext(), mAdView));
            mAdView.loadAd(adRequest);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        String stringPnr = pnrDetails.getText().toString();
        if (stringPnr.isEmpty()) {
            MySnackBar.show(context.getApplicationContext(), parentLayout, "Please Enter PNR No.!!");
        } else if (stringPnr.length() < 10) {
            MySnackBar.show(context.getApplicationContext(), parentLayout, "Please Enter Valid PNR No.!!");
        } else {
            checkStatus(stringPnr);
        }
    }

    private void checkStatus(String stringPnr) {
        if (Internet.isConnected(context)) {
            new SearchPnr(context, stringPnr, this).execute();
        }

    }

    @Override
    public void savePnr(PnrDetails pnrDetails) {
        addDetailstoHistory(pnrDetails);
    }

    @Override
    public void checkPnr(PnrDetails pnrDetails) {
        checkStatus(pnrDetails.getPnrNo());
    }

    public void addDetailstoHistory(PnrDetails p) {
        ifDataExist(p);
        pnrDetailsList.add(0, p);
        pnrHistoryAdapter.notifyDataSetChanged();

        if (pnrDetailsList.size() > 20)
            pnrDetailsList.remove(pnrDetailsList.size() - 1);

        try {
            SharedPreferenceManager.getPnrSharedPrefsEditor(context).putString(PNR_HISTORY, ObjectSerializer.serialize(pnrDetailsList)).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ifDataExist(PnrDetails p) {

        for (PnrDetails pnrDetails : pnrDetailsList) {

            if (p.getPnrNo().equals(pnrDetails.getPnrNo())) {
                pnrDetailsList.remove(pnrDetails);
                break;
            }

        }
    }

}
