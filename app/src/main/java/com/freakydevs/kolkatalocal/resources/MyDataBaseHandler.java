package com.freakydevs.kolkatalocal.resources;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.freakydevs.kolkatalocal.interfaces.MainActivityInterface;
import com.freakydevs.kolkatalocal.models.Station;
import com.freakydevs.kolkatalocal.models.Train;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PURUSHOTAM on 10/31/2017.
 */

public class MyDataBaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database";
    public static String DATABASE_PATH = "";
    private final Context context;
    private SQLiteDatabase myDataBase;
    private MainActivityInterface mainActivityInterface;


    public MyDataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = this.context.getDatabasePath(DATABASE_NAME).toString();
        checkDataBase();
    }

    //Check database already exist or not
    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        Boolean bool = true;
        try {
            checkDB = this.getReadableDatabase();
            String query = "select dbver from db_information;";
            checkDB.rawQuery(query, null);
        } catch (Exception e) {
            //dbNotice();
            bool = false;
        } finally {
            if (checkDB != null) {
                checkDB.close();
            }
        }
        return bool;
    }

    //Open database
    public void openDatabase() throws SQLException {
        String myPath = DATABASE_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase() throws SQLException {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.");
        }
    }


    public List<Station> read(String searchTerm) {

        List<Station> recordsList = new ArrayList<Station>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            mainActivityInterface = (MainActivityInterface) context;
            String sql = "SELECT * FROM station_table WHERE stationName LIKE '%" + searchTerm + "%' OR stationCode LIKE '%" + searchTerm + "%' ORDER BY stationName DESC LIMIT 0,8;";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    String stationName = cursor.getString(cursor.getColumnIndex("stationName"));
                    String stationCode = cursor.getString(cursor.getColumnIndex("stationCode"));
                    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    recordsList.add(new Station(stationName, stationCode, _id));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            mainActivityInterface.notifyDatabaseDownload();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return recordsList;
    }


    public List<Station> getRoutes(String trainNo) {
        List<Station> recordsList = new ArrayList<Station>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            String query = "SELECT * FROM station_table AS s INNER JOIN(SELECT * FROM route_table AS r INNER JOIN(SELECT * FROM train_table WHERE trainNO=" + trainNo + ") t ON r.trainId=t._id) y ON s._id=y.stationId;";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Station station = new Station();
                station.setStationName(cursor.getString(cursor.getColumnIndex("stationName")));
                station.setStationCode(cursor.getString(cursor.getColumnIndex("stationCode")));
                station.setArrivalTime(cursor.getString(cursor.getColumnIndex("arrival")));
                recordsList.add(station);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            mainActivityInterface.notifyDatabaseDownload();
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return recordsList;
    }

    public int getDbVersion() {
        SQLiteDatabase checkDB = null;
        Cursor cursor = null;
        int dbVersion = 0;
        try {
            checkDB = this.getReadableDatabase();
            String query = "select dbver from db_information;";
            cursor = checkDB.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    dbVersion = cursor.getInt(cursor.getColumnIndex("dbver"));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (checkDB != null) {
                checkDB.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return dbVersion;
    }

    public List<String> readForTrains(String searchTerm) {

        List<String> recordsList = new ArrayList<String>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            String sql = "SELECT * FROM train_table WHERE trainNO LIKE '%" + searchTerm + "%' OR trainName LIKE '%" + searchTerm + "%' ORDER BY trainNO DESC LIMIT 0,10;";
            db = this.getWritableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    String trainNo = cursor.getString(cursor.getColumnIndex("trainNO"));
                    String trainName = cursor.getString(cursor.getColumnIndex("trainName"));
                    recordsList.add(trainNo + " ~ " + trainName);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return recordsList;
    }

    public Train getTrainDays(String trainNo) {

        Train train = new Train();

        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            String sql = "SELECT * FROM train_table WHERE trainNO=" + trainNo + ";";
            db = this.getWritableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    train.setSun(cursor.getInt(cursor.getColumnIndex("sun")) > 0);
                    train.setMon(cursor.getInt(cursor.getColumnIndex("mon")) > 0);
                    train.setTue(cursor.getInt(cursor.getColumnIndex("tue")) > 0);
                    train.setWed(cursor.getInt(cursor.getColumnIndex("wed")) > 0);
                    train.setThu(cursor.getInt(cursor.getColumnIndex("thu")) > 0);
                    train.setFri(cursor.getInt(cursor.getColumnIndex("fri")) > 0);
                    train.setSat(cursor.getInt(cursor.getColumnIndex("sat")) > 0);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return train;
    }

}
