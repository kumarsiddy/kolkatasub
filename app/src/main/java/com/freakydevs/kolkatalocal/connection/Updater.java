package com.freakydevs.kolkatalocal.connection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.freakydevs.kolkatalocal.BuildConfig;
import com.freakydevs.kolkatalocal.enums.UpdateType;
import com.freakydevs.kolkatalocal.interfaces.MainActivityInterface;
import com.freakydevs.kolkatalocal.resources.MyDataBaseHandler;
import com.freakydevs.kolkatalocal.utils.Logger;
import com.freakydevs.kolkatalocal.utils.SharedPreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by PURUSHOTAM on 11/23/2017.
 */

public class Updater {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private boolean isProgress;
    private UpdateType updateType;
    private JSONObject jsonObject;
    private Context context;
    private MainActivityInterface mainActivityInterface;

    public Updater(Context context, boolean isProgress, UpdateType updateType) {
        this.context = context;
        this.isProgress = isProgress;
        this.updateType = updateType;
        mainActivityInterface = (MainActivityInterface) context;
        if (isProgress) {
            mainActivityInterface.showUpdateLoader();
        }
        getJSON();
    }

    private void getJSON() {

        StorageReference dbRef = storage.getReference().child("kolkata_sub/update.json");
        dbRef.getBytes(1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {


                try {
                    InputStream inputStream = new ByteArrayInputStream(bytes);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    StringBuilder responseStrBuilder = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        responseStrBuilder.append(line);
                    }
                    inputStream.close();

                    Logger.d(responseStrBuilder.toString());

                    jsonObject = new JSONObject(responseStrBuilder.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                checkForUpdate(jsonObject);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                if (isProgress) {
                    mainActivityInterface.hideUpdateLoader();
                }
            }
        });
    }

    private void checkForUpdate(JSONObject jsonObject) {

        boolean isDBUpdateav, isAppUpdateav;
        if (isProgress) {
            mainActivityInterface.hideUpdateLoader();
        }
        try {
            int newAppVersion = jsonObject.getInt("a");
            int newDBVersion = jsonObject.getInt("d");
            int isRemovedAd = jsonObject.getInt("r");
            if (isRemovedAd == 0) {
                SharedPreferenceManager.setRemoveAd(context.getApplicationContext(), false);
            }

            int oldDBVersion = new MyDataBaseHandler(context).getDbVersion();
            int oldAppVersion = BuildConfig.VERSION_CODE;

            isDBUpdateav = oldDBVersion < newDBVersion;
            isAppUpdateav = oldAppVersion < newAppVersion;

            if (updateType == UpdateType.APP && isAppUpdateav) {
                mainActivityInterface.showAppUpdate();
            } else if (updateType == UpdateType.APP) {
                mainActivityInterface.noAppUpdate();
            }

            if (updateType == UpdateType.DATABASE && isDBUpdateav) {
                mainActivityInterface.showDatabaseUpdate();
            } else if (updateType == UpdateType.DATABASE) {
                mainActivityInterface.noDbUpdate();
            }

            if (updateType == UpdateType.BOTH && isAppUpdateav && isDBUpdateav) {
                mainActivityInterface.showAppUpdate();
                mainActivityInterface.showDatabaseUpdate();
            } else if (updateType == UpdateType.BOTH && isAppUpdateav) {
                mainActivityInterface.showAppUpdate();
            } else if (updateType == UpdateType.BOTH && isDBUpdateav) {
                mainActivityInterface.showDatabaseUpdate();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
