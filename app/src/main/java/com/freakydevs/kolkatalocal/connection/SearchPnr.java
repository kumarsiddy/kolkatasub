package com.freakydevs.kolkatalocal.connection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.activity.PnrStatusActivity;
import com.freakydevs.kolkatalocal.interfaces.MainActivityInterface;
import com.freakydevs.kolkatalocal.interfaces.PnrStatusInterface;
import com.freakydevs.kolkatalocal.models.PnrDetails;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by PURUSHOTAM on 11/6/2017.
 */

public class SearchPnr extends AsyncTask<Void, Void, JSONObject> {

    private Context context;
    private String pnrNo;
    private MainActivityInterface mainActivityInterface;
    private PnrStatusInterface pnrStatusInterface;
    private String stringURL;

    public SearchPnr(Context context, String pnrNo, Fragment pnrStatusFragment) {
        this.context = context;
        this.pnrNo = pnrNo;
        mainActivityInterface = (MainActivityInterface) context;
        pnrStatusInterface = (PnrStatusInterface) pnrStatusFragment;
        stringURL = context.getApplicationContext().getString(R.string.string1)
                + pnrNo
                + context.getApplicationContext().getString(R.string.string2);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivityInterface.showUpdateLoader();
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        try {
            URL url = new URL(stringURL);
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuilder responseStrBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                responseStrBuilder.append(line);
            }
            inputStream.close();
            return new JSONObject(responseStrBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        mainActivityInterface.hideUpdateLoader();
        try {
            String status = jsonObject.getString("status").trim();
            PnrDetails pnrDetails = new PnrDetails();
            if (status.equals("OK")) {

                pnrDetails.setFromTo(jsonObject.getString("reserved_from") + " - " + jsonObject.getString("reserved_upto"));
                pnrDetails.setPnrNo(pnrNo);
                pnrDetails.setTrainNoName(jsonObject.getString("train_number") + "  " + jsonObject.getString("train_name"));
                pnrStatusInterface.savePnr(pnrDetails);

                Intent intent = new Intent(context.getApplicationContext(), PnrStatusActivity.class);
                intent.putExtra("json", jsonObject.toString());
                intent.putExtra("pnr", pnrNo);
                context.startActivity(intent);
            } else {
                mainActivityInterface.invalidPnr();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
