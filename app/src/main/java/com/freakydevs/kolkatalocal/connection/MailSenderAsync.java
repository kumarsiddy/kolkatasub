package com.freakydevs.kolkatalocal.connection;

import android.content.Context;
import android.os.AsyncTask;

import com.freakydevs.kolkatalocal.interfaces.ContactusInterface;

/**
 * Created by PURUSHOTAM on 11/5/2017.
 */

public class MailSenderAsync extends AsyncTask<String, Void, Void> {

    private Context context;
    private ContactusInterface contactusInterface;

    public MailSenderAsync(Context context) {
        this.context = context;
        contactusInterface = (ContactusInterface) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        contactusInterface.beforeSend();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        contactusInterface.afterSend();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String message = strings[2];
        MailSender mailSender = new MailSender();
        String toArr[] = {"help@freakydevs.in"};
        mailSender.set_to(toArr);
        mailSender.set_subject(strings[1]);
        mailSender.set_body(strings[0] + " writes " + message);
        try {
            mailSender.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
