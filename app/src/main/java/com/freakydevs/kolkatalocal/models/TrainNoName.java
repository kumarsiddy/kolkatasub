package com.freakydevs.kolkatalocal.models;

import java.io.Serializable;

/**
 * Created by PURUSHOTAM on 11/5/2017.
 */

public class TrainNoName implements Serializable {
    private String trainNo;
    private String trainName;

    public TrainNoName() {
    }

    public TrainNoName(String trainNo, String trainName) {
        this.trainNo = trainNo;
        this.trainName = trainName;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }
}
