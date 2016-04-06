package com.sw_ss16.studyroompopulationpredicter.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Predictr_Database";
    private static final String STUDYROOM_TABLE_NAME = "studyrooms";
    private static final String STUDYROOM_TABLE_CREATE =
            "CREATE TABLE " + STUDYROOM_TABLE_NAME + " (" +
                    "ID" + " INT, " +
                    "NAME" + " TEXT, " +
                    "DESCRIPTION" + " TEXT, " +
                    "ADDRESS" + " TEXT, " +
                    "IMAGE" + " BLOB, " +
                    "CAPACITY" + " INT, " +
                    ");";


    private static final String STATISTICS_TABLE_NAME = "statistics";
    private static final String STATISTICS_TABLE_CREATE =
            "CREATE TABLE " + STATISTICS_TABLE_NAME + " (" +
                    "ID" + " INT, " +
                    "LC_ID" + " INT, " +
                    "WEEKDAY" + " INT, " +
                    "HOUR" + " INT, " +
                    "FULLNESS" + " INT, " +
                    ");";

    private static final String CURRENTDATA_TABLE_NAME = "current_data";
    private static final String CURRENTDATA_TABLE_CREATE =
            "CREATE TABLE " + CURRENTDATA_TABLE_NAME + " (" +
                    "ID" + " INT, " +
                    "LC_ID" + " INT, " +
                    "HOUR" + " INT, " +
                    "FULLNESS" + " INT, " +
                    ");";

    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STUDYROOM_TABLE_CREATE);
        db.execSQL(STATISTICS_TABLE_CREATE);
        db.execSQL(CURRENTDATA_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}