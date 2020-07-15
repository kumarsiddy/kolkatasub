package com.freakydevs.kolkatalocal.resources;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.freakydevs.kolkatalocal.activity.TrainRouteActivity;
import com.freakydevs.kolkatalocal.interfaces.TrainListInterface;
import com.freakydevs.kolkatalocal.models.HistoryFromTo;
import com.freakydevs.kolkatalocal.models.Station;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PURUSHOTAM on 11/3/2017.
 */

public class SearchTrainRoute extends AsyncTask<Void, Void, List<Station>> {

    private Context context;
    private String trainNo;
    private String trainName;
    private TrainListInterface trainListInterface;
    private HistoryFromTo fromTo;

    public SearchTrainRoute(Context context, String trainNo, String trainName, HistoryFromTo fromTo) {
        this.context = context;
        this.trainNo = trainNo;
        this.trainName = trainName;
        trainListInterface = (TrainListInterface) context;
        this.fromTo = fromTo;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        trainListInterface.showLoader();
    }

    @Override
    protected List<Station> doInBackground(Void... voids) {
        MyDataBaseHandler myDataBaseHandler = new MyDataBaseHandler(context);
        return myDataBaseHandler.getRoutes(trainNo);
    }


    @Override
    protected void onPostExecute(List<Station> stations) {
        super.onPostExecute(stations);
        trainListInterface.hideLoader();
        Intent intent = new Intent(context.getApplicationContext(), TrainRouteActivity.class);
        intent.putExtra("trainName", trainName);
        intent.putExtra("trainNo", trainNo);
        intent.putExtra("fromTo", (Serializable) fromTo);
        intent.putExtra("list", (Serializable) stations);
        context.startActivity(intent);
    }
}
