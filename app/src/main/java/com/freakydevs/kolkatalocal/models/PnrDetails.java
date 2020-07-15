package com.freakydevs.kolkatalocal.models;

import java.io.Serializable;

/**
 * Created by PURUSHOTAM on 11/6/2017.
 */

public class PnrDetails implements Serializable {

    private String fromTo;
    private String pnrNo;
    private String trainNoName;

    public PnrDetails() {
    }

    public PnrDetails(String fromTo, String pnrNo, String trainNoName) {
        this.fromTo = fromTo;
        this.pnrNo = pnrNo;
        this.trainNoName = trainNoName;
    }

    public String getFromTo() {
        return fromTo;
    }

    public void setFromTo(String fromTo) {
        this.fromTo = fromTo;
    }

    public String getPnrNo() {
        return pnrNo;
    }

    public void setPnrNo(String pnrNo) {
        this.pnrNo = pnrNo;
    }

    public String getTrainNoName() {
        return trainNoName;
    }

    public void setTrainNoName(String trainNoName) {
        this.trainNoName = trainNoName;
    }
}
