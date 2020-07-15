package com.freakydevs.kolkatalocal.interfaces;

import com.freakydevs.kolkatalocal.models.TrainNoName;

/**
 * Created by PURUSHOTAM on 11/5/2017.
 */

public interface TrainRouteFragInterface {
    public void searchRoute(TrainNoName trainNoName);

    public void fillData(String input);
}
