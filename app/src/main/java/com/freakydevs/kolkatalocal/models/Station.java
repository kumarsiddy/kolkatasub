package com.freakydevs.kolkatalocal.models;

import java.io.Serializable;

/**
 * Created by PURUSHOTAM on 10/31/2017.
 */

public class Station implements Serializable {
    private String stationName;
    private String stationCode;
    private String arrivalTime;
    private int _id;

    public Station() {
    }

    public Station(String stationName, String stationCode, int _id) {
        this.stationName = stationName;
        this.stationCode = stationCode;
        this._id = _id;
    }

    public Station(String stationName, String stationCode, int _id, String arrivalTime) {
        this.stationName = stationName;
        this.stationCode = stationCode;
        this._id = _id;
        this.arrivalTime = arrivalTime;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
