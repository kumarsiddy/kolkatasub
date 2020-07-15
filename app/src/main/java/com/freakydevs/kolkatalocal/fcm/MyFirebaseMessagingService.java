package com.freakydevs.kolkatalocal.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.freakydevs.kolkatalocal.R;
import com.freakydevs.kolkatalocal.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by PURUSHOTAM on 11/30/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        try {
            Map<String, String> params = remoteMessage.getData();

            String title = params.get("title");
            String message = params.get("message");
            String largeMessage = params.get("lmessage");
            String summaryTitle = params.get("stitle");

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.notif_icon)
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(largeMessage).setSummaryText(summaryTitle));

            NotificationManager notificationManager =
                    (NotificationManager)
                            getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, builder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
