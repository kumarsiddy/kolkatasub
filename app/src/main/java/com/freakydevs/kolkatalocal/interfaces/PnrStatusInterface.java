package com.freakydevs.kolkatalocal.interfaces;

import com.freakydevs.kolkatalocal.models.PnrDetails;

/**
 * Created by PURUSHOTAM on 11/6/2017.
 */

public interface PnrStatusInterface {
    public void savePnr(PnrDetails pnrDetails);

    public void checkPnr(PnrDetails pnrDetails);
}
