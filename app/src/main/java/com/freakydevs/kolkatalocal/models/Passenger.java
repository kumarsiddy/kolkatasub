package com.freakydevs.kolkatalocal.models;

import java.io.Serializable;

/**
 * Created by PURUSHOTAM on 11/6/2017.
 */

public class Passenger implements Serializable {
    private String seat_number;
    private String status;

    public Passenger() {
    }

    public Passenger(String seat_number, String status) {
        this.seat_number = seat_number;
        this.status = status;
    }

    public String getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(String seat_number) {
        this.seat_number = seat_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
