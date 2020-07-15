package com.freakydevs.kolkatalocal.resources;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.freakydevs.kolkatalocal.activity.TrainRouteFragActivity;
import com.freakydevs.kolkatalocal.interfaces.MainActivityInterface;
import com.freakydevs.kolkatalocal.models.Station;
import com.freakydevs.kolkatalocal.models.Train;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PURUSHOTAM on 11/3/2017.
 */

public class SearchTrainRouteFrag extends AsyncTask<Void, Void, List<Station>> {

    private Context context;
    private String trainNo;
    private String trainName;
    private MainActivityInterface mainActivityInterface;
    private Train train;

    public SearchTrainRouteFrag(Context context, String trainNo, String trainName) {
        this.context = context;
        this.trainNo = trainNo;
        this.trainName = trainName;
        mainActivityInterface = (MainActivityInterface) context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivityInterface.showLoader();
    }

    @Override
    protected List<Station> doInBackground(Void... voids) {
        MyDataBaseHandler myDataBaseHandler = new MyDataBaseHandler(context);
        train = myDataBaseHandler.getTrainDays(trainNo);
        return myDataBaseHandler.getRoutes(trainNo);
    }


    @Override
    protected void onPostExecute(List<Station> stations) {
        super.onPostExecute(stations);
        mainActivityInterface.hideLoader();
        Intent intent = new Intent(context.getApplicationContext(), TrainRouteFragActivity.class);
        intent.putExtra("trainName", trainName);
        intent.putExtra("trainNo", trainNo);
        intent.putExtra("list", (Serializable) stations);
        intent.putExtra("train", (Serializable) train);
        context.startActivity(intent);
    }
}
