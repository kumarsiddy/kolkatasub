package com.freakydevs.kolkatalocal.models;

import java.io.Serializable;

/**
 * Created by PURUSHOTAM on 3/11/2017.
 */

public class HistoryFromTo implements Serializable {

    private String fromCode;
    private String fromStation;
    private String toCode;
    private String toStation;

    public HistoryFromTo(String fromCode, String fromStation, String toCode, String toStation) {
        this.fromCode = fromCode;
        this.fromStation = fromStation;
        this.toCode = toCode;
        this.toStation = toStation;
    }

    public String getFromCode() {
        return fromCode;
    }

    public void setFromCode(String fromCode) {
        this.fromCode = fromCode;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToCode() {
        return toCode;
    }

    public void setToCode(String toCode) {
        this.toCode = toCode;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }
}
