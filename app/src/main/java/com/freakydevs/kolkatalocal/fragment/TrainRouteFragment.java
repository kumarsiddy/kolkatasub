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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.adapter.TrainRouteHistoryAdapter;
import com.freakydevs.kolkatalocal.customview.CustomAutoCompleteView;
import com.freakydevs.kolkatalocal.customview.MySnackBar;
import com.freakydevs.kolkatalocal.customview.TrainAutoCompleteListener;
import com.freakydevs.kolkatalocal.interfaces.TrainRouteFragInterface;
import com.freakydevs.kolkatalocal.models.TrainNoName;
import com.freakydevs.kolkatalocal.resources.CustomAdListener;
import com.freakydevs.kolkatalocal.resources.MyDataBaseHandler;
import com.freakydevs.kolkatalocal.resources.ObjectSerializer;
import com.freakydevs.kolkatalocal.resources.SearchTrainRouteFrag;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.freakydevs.kolkatalocal.utils.Constants.TRAINHISTORY;

/**
 * Created by PURUSHOTAM on 10/30/2017.
 */

public class TrainRouteFragment extends Fragment implements TrainRouteFragInterface, View.OnClickListener {

    private CustomAutoCompleteView trainDetails;
    private Context context;
    private AdView mAdView;
    private RecyclerView historyTrainRouteRecyler;
    private TrainRouteHistoryAdapter historyAdapter;
    private ArrayList<TrainNoName> trainNoNames;
    private ArrayAdapter<String> customAdapter;
    private String[] items = new String[]{};
    private MyDataBaseHandler myDataBaseHandler;
    private Button findButton;
    private ConstraintLayout parentLaout;

    public TrainRouteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        myDataBaseHandler = new MyDataBaseHandler(context);
        initTrainListFromSharedPref();
    }

    private void initTrainListFromSharedPref() {
        try {
            trainNoNames = (ArrayList<TrainNoName>) ObjectSerializer.deserialize(SharedPreferenceManager.getTrainROuteSharedPrefs(context).getString(TRAINHISTORY, ObjectSerializer.serialize(new ArrayList<TrainNoName>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (trainNoNames == null) {
            trainNoNames = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_route, container, false);
        trainDetails = view.findViewById(R.id.edit_traindetails);

        findButton = view.findViewById(R.id.find_route);
        findButton.setOnClickListener(this);

        parentLaout = view.findViewById(R.id.parent_layout);

        trainDetails.addTextChangedListener(new TrainAutoCompleteListener(context, trainDetails, this));

        mAdView = view.findViewById(R.id.adView);

        historyTrainRouteRecyler = view.findViewById(R.id.history_trainroute_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        historyTrainRouteRecyler.setLayoutManager(mLayoutManager);
        historyTrainRouteRecyler.setItemAnimator(new DefaultItemAnimator());
        historyAdapter = new TrainRouteHistoryAdapter(context, trainNoNames, historyTrainRouteRecyler, this);
        historyTrainRouteRecyler.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();

        trainDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard();
                findRoute();
            }
        });
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
    public void searchRoute(TrainNoName trainNoName) {
        trainDetails.setText(trainNoName.getTrainNo().trim() + " ~ " + trainNoName.getTrainName().trim());
        findRoute();
    }

    @Override
    public void fillData(String input) {
        items = getItemsFromDb(input);
        customAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, items);
        customAdapter.notifyDataSetChanged();
        trainDetails.setAdapter(customAdapter);
    }

    public String[] getItemsFromDb(String searchTerm) {
        List<String> products = myDataBaseHandler.readForTrains(searchTerm);
        int rowCount = products.size();
        String[] item = new String[rowCount];
        int x = 0;
        for (String record : products) {
            item[x] = record;
            x++;
        }
        return item;
    }

    private void hideKeyboard() {
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(trainDetails.getApplicationWindowToken(), 0);
    }

    public void addDetailstoHistory(TrainNoName t) {
        ifDataExist(t);
        trainNoNames.add(0, t);
        historyAdapter.notifyDataSetChanged();

        if (trainNoNames.size() > 20)
            trainNoNames.remove(trainNoNames.size() - 1);

        try {
            SharedPreferenceManager.getTrainRouteSharedPrefsEditor(context).putString(TRAINHISTORY, ObjectSerializer.serialize(trainNoNames)).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ifDataExist(TrainNoName t) {

        for (TrainNoName trainNoName : trainNoNames) {

            if (t.getTrainNo().equals(trainNoName.getTrainNo())) {
                trainNoNames.remove(trainNoName);
                break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        findRoute();
    }

    public void findRoute() {
        String stringTrain = trainDetails.getText().toString();
        if (stringTrain.isEmpty()) {
            MySnackBar.show(context.getApplicationContext(), parentLaout, "Please Fill Train No./Name!!");
        } else if (stringTrain.length() < 5) {
            MySnackBar.show(context, parentLaout, "Please select from Drop Down!!");
        } else {
            try {
                String[] strings = stringTrain.split("~");
                addDetailstoHistory(new TrainNoName(strings[0].trim(), strings[1].trim()));
                new SearchTrainRouteFrag(context, strings[0].trim(), strings[1].trim()).execute();
            } catch (ArrayIndexOutOfBoundsException e) {
                MySnackBar.show(context.getApplicationContext(), parentLaout, "Please select from Drop Down!!");
            }

        }
    }

}
