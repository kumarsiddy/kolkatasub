package com.freakydevs.kolkatalocal.fcm;

import com.freakydevs.kolkatalocal.utils.Logger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by PURUSHOTAM on 11/30/2016.
 */

public class MyFireBaseInstanceId extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        String recent_token_id = FirebaseInstanceId.getInstance().getToken();
        Logger.d("TOKEN  " + recent_token_id);
    }
}
