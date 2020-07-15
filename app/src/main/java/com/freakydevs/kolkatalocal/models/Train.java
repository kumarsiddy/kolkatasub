package com.freakydevs.kolkatalocal.models;

import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PURUSHOTAM on 11/1/2017.
 */

public class Train implements Serializable {
    private static transient ArrayList<Train> trains;
    private String trainNo;
    private String trainName;
    private String srcTime;
    private String reachTime;
    private boolean mon, tue, wed, thu, fri, sat, sun;

    public Train() {
    }

    public Train(String trainNo, String trainName, String srcTime, String reachTime, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun) {
        this.trainNo = trainNo;
        this.trainName = trainName;
        this.srcTime = srcTime;
        this.reachTime = reachTime;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    public static ArrayList<Train> fromCursor(final Cursor cursor) {
        trains = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Train train = new Train();
            train.setTrainNo(Integer.toString(cursor.getInt(cursor.getColumnIndex("trainNO"))));
            train.setTrainName(cursor.getString(cursor.getColumnIndex("trainName")));
            train.setSrcTime(cursor.getString(cursor.getColumnIndex("arrival")));
            train.setReachTime(cursor.getString(cursor.getColumnIndex("arrival:1")));
            train.setMon(cursor.getInt(cursor.getColumnIndex("mon")) > 0);
            train.setTue(cursor.getInt(cursor.getColumnIndex("tue")) > 0);
            train.setWed(cursor.getInt(cursor.getColumnIndex("wed")) > 0);
            train.setThu(cursor.getInt(cursor.getColumnIndex("thu")) > 0);
            train.setFri(cursor.getInt(cursor.getColumnIndex("fri")) > 0);
            train.setSat(cursor.getInt(cursor.getColumnIndex("sat")) > 0);
            train.setSun(cursor.getInt(cursor.getColumnIndex("sun")) > 0);
            trains.add(train);
            cursor.moveToNext();
        }
        cursor.close();
        return trains;
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

    public String getSrcTime() {
        return srcTime;
    }

    public void setSrcTime(String srcTime) {
        this.srcTime = srcTime;
    }

    public String getReachTime() {
        return reachTime;
    }

    public void setReachTime(String reachTime) {
        this.reachTime = reachTime;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

}
