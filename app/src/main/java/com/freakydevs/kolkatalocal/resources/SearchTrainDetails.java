package com.freakydevs.kolkatalocal.resources;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.freakydevs.kolkatalocal.activity.TrainListActivity;
import com.freakydevs.kolkatalocal.interfaces.MainActivityInterface;
import com.freakydevs.kolkatalocal.interfaces.SearchInterface;
import com.freakydevs.kolkatalocal.models.HistoryFromTo;
import com.freakydevs.kolkatalocal.models.Train;
import com.freakydevs.kolkatalocal.utils.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by PURUSHOTAM on 11/1/2017.
 */

public class SearchTrainDetails extends AsyncTask<Void, Void, ArrayList<Train>> {

    private String from, to, sday, sday1, sday2, sday3;
    private Context context;
    private SearchInterface searchInterface;
    private MainActivityInterface mainActivityInterface;
    private HistoryFromTo fromTo;
    private int from_id, to_id;
    private boolean isChecked = false;

    public SearchTrainDetails(Context context, Fragment fragment, String dateString, HistoryFromTo fromTo, int from_id, int to_id, boolean isChecked) {
        this.context = context;
        searchInterface = (SearchInterface) fragment;
        mainActivityInterface = (MainActivityInterface) context;
        this.fromTo = fromTo;
        this.from_id = from_id;
        this.to_id = to_id;
        this.isChecked = isChecked;
        this.isChecked = isChecked;

        String[] whichDate = dateString.split("-");
        int day = Integer.parseInt(whichDate[0].trim());
        int month = Integer.parseInt(whichDate[1].trim());
        int year = Integer.parseInt(whichDate[2].trim());

        sday = knowDay(day, month - 1, year);

        this.from = fromTo.getFromStation();
        this.to = fromTo.getToStation();

    }


    @Override
    protected void onPreExecute() {

        searchInterface.disableFindButton();
        mainActivityInterface.showLoader();
        switch (sday) {

            case "mon":
                sday1 = "sun";
                sday2 = "sat";
                sday3 = "fri";
                break;
            case "tue":
                sday1 = "mon";
                sday2 = "sun";
                sday3 = "sat";
                break;
            case "wed":
                sday1 = "tue";
                sday2 = "mon";
                sday3 = "sun";
                break;
            case "thu":
                sday1 = "wed";
                sday2 = "tue";
                sday3 = "mon";
                break;
            case "fri":
                sday1 = "thu";
                sday2 = "wed";
                sday3 = "tue";
                break;
            case "sat":
                sday1 = "fri";
                sday2 = "thu";
                sday3 = "wed";
                break;
            case "sun":
                sday1 = "sat";
                sday2 = "fri";
                sday3 = "thu";
                break;
        }


    }


    @Override
    protected ArrayList<Train> doInBackground(Void... params) {

        String s;
        Cursor c3 = null;
        ArrayList<Train> trains = null;
        MyDataBaseHandler dataBaseHandler = new MyDataBaseHandler(context);
        SQLiteDatabase SQ = dataBaseHandler.getReadableDatabase();
        if (isChecked) {
            s = "select * from(select * from train_table where " + sday + "=1)t inner join(select * from(select * from route_table where stationId=" + from_id + " AND datePlus=0 AND (arrival > time(strftime('%s','now'), 'unixepoch', 'localtime')))r1 inner join(select * from route_table where stationId=" + to_id + ")r2 on r1.trainId=r2.trainId and r1._id < r2._id order by arrival)r on r.trainId=t._id" +
                    " union " +
                    "select * from(select * from train_table where " + sday1 + "=1)t inner join(select * from(select * from route_table where stationId=" + from_id + " AND datePlus=1 AND (arrival > time(strftime('%s','now'), 'unixepoch', 'localtime')))r1 inner join(select * from route_table where stationId=" + to_id + ")r2 on r1.trainId=r2.trainId and r1._id < r2._id order by arrival)r on r.trainId=t._id " +
                    "union " +
                    "select * from(select * from train_table where " + sday2 + "=1)t inner join(select * from(select * from route_table where stationId=" + from_id + " AND datePlus=2 AND (arrival > time(strftime('%s','now'), 'unixepoch', 'localtime')))r1 inner join(select * from route_table where stationId=" + to_id + ")r2 on r1.trainId=r2.trainId and r1._id < r2._id order by arrival)r on r.trainId=t._id" +
                    " union " +
                    "select * from(select * from train_table where " + sday3 + "=1)t inner join(select * from(select * from route_table where stationId=" + from_id + " AND datePlus=3 AND (arrival > time(strftime('%s','now'), 'unixepoch', 'localtime')))r1 inner join(select * from route_table where stationId=" + to_id + ")r2 on r1.trainId=r2.trainId and r1._id < r2._id order by arrival)r on r.trainId=t._id order by arrival;";
        } else {
            s = "select * from(select * from train_table where " + sday + "=1)t inner join(select * from(select * from route_table where stationId=" + from_id + " AND datePlus=0)r1 inner join(select * from route_table where stationId=" + to_id + ")r2 on r1.trainId=r2.trainId and r1._id < r2._id order by arrival)r on r.trainId=t._id \n" +
                    "union\n" +
                    "select * from(select * from train_table where   " + sday1 + "=1)t inner join(select * from(select * from route_table where stationId=" + from_id + " AND datePlus=1)r1 inner join(select * from route_table where stationId=" + to_id + ")r2 on r1.trainId=r2.trainId and r1._id < r2._id order by arrival)r on r.trainId=t._id  union\n" +
                    "select * from(select * from train_table where   " + sday2 + "=1)t inner join(select * from(select * from route_table where stationId=" + from_id + " AND datePlus=2)r1 inner join(select * from route_table where stationId=" + to_id + ")r2 on r1.trainId=r2.trainId and r1._id < r2._id order by arrival)r on r.trainId=t._id \n" +
                    "union\n" +
                    "select * from(select * from train_table where   " + sday3 + "=1)t inner join(select * from(select * from route_table where stationId=" + from_id + " AND datePlus=3)r1 inner join(select * from route_table where stationId=" + to_id + ")r2 on r1.trainId=r2.trainId and r1._id < r2._id order by arrival)r on r.trainId=t._id order by arrival;";

        }
        c3 = SQ.rawQuery(s, null);
        trains = Train.fromCursor(c3);
        Logger.d("Size is: " + trains.size());
        return trains;
    }

    @Override
    protected void onPostExecute(ArrayList<Train> trains) {

        if (trains.isEmpty()) {
            mainActivityInterface.noTrainDialog();
        } else {


            Intent intent = new Intent(context.getApplicationContext(), TrainListActivity.class);
            intent.putExtra("list", (Serializable) trains);
            intent.putExtra("fromTo", (Serializable) fromTo);
            context.startActivity(intent);

        }
        searchInterface.enableFindButton();
        mainActivityInterface.hideLoader();
    }

    public String knowDay(int aDay, int bMonth, int cYear) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, aDay); //Set Day of the Month, 1..31
        cal.set(Calendar.MONTH, bMonth); //Set month, starts with JANUARY = 0
        cal.set(Calendar.YEAR, cYear); //Set year
        int result = cal.get(Calendar.DAY_OF_WEEK);
        String day = null;
        switch (result) {

            case 1:
                day = "sun";
                break;

            case 2:
                day = "mon";
                break;

            case 3:
                day = "tue";
                break;

            case 4:
                day = "wed";
                break;

            case 5:
                day = "thu";
                break;

            case 6:
                day = "fri";
                break;

            case 7:
                day = "sat";
                break;
        }

        return day;

    }


}
