package com.freakydevs.kolkatalocal.interfaces;

import com.freakydevs.kolkatalocal.models.HistoryFromTo;

/**
 * Created by PURUSHOTAM on 11/1/2017.
 */

public interface SearchInterface {
    public void historyClicked(HistoryFromTo fromTo);

    public void fillData(String userInput);

    public void disableFindButton();

    public void enableFindButton();
}
