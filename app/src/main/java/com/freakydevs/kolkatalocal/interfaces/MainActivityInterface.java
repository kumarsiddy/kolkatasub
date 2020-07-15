package com.freakydevs.kolkatalocal.interfaces;

/**
 * Created by PURUSHOTAM on 11/1/2017.
 */

public interface MainActivityInterface {
    public void notifyDatabaseDownload();

    public void noTrainDialog();

    public void showLoader();

    public void hideLoader();

    public void showDatabaseUpdate();

    public void showAppUpdate();

    public void showUpdateLoader();

    public void hideUpdateLoader();

    public void noAppUpdate();

    public void noDbUpdate();

    public void invalidPnr();
}
