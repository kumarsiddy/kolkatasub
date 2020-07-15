package com.freakydevs.kolkatalocal.models;

/**
 * Created by PURUSHOTAM on 11/6/2017.
 */

public class MyURL {
    private String data;
    private String update;

    public MyURL() {
    }

    public MyURL(String data, String update) {
        this.data = data;
        this.update = update;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
