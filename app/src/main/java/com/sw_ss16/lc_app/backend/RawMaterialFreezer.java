package com.sw_ss16.lc_app.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RawMaterialFreezer extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Predictr_Database";
    private static final String STUDYROOM_TABLE_NAME = "studyrooms";
    private static final String STUDYROOM_TABLE_CREATE =
            "CREATE TABLE " + STUDYROOM_TABLE_NAME + " (" +
                    "ID" + " INT, " +
                    "NAME" + " TEXT, " +
                    "DESCRIPTION" + " TEXT, " +
                    "ADDRESS" + " TEXT, " +
                    "IMAGE_IN" + " TEXT, " +
                    "IMAGE_OUT" + " TEXT, " +
                    "CAPACITY" + " INT " +
                    ");";


    private static final String STATISTICS_TABLE_NAME = "statistics";
    private static final String STATISTICS_TABLE_CREATE =
            "CREATE TABLE " + STATISTICS_TABLE_NAME + " (" +
                    "ID" + " INT, " +
                    "LC_ID" + " INT, " +
                    "WEEKDAY" + " INT, " +
                    "HOUR" + " INT, " +
                    "FULLNESS" + " INT " +
                    ");";

    private static final String CURRENTDATA_TABLE_NAME = "current_data";
    private static final String CURRENTDATA_TABLE_CREATE =
            "CREATE TABLE " + CURRENTDATA_TABLE_NAME + " (" +
                    "ID" + " INT, " +
                    "LC_ID" + " INT, " +
                    "HOUR" + " INT, " +
                    "FULLNESS" + " INT, " +
                    "DATE" + " DATE " +
                    ");";

    private static final String FAVSTUDYROOM_TABLE_NAME = "favstudyrooms";
    private static final String FAVSTUDYROOM_TABLE_CREATE =
            "CREATE TABLE " + FAVSTUDYROOM_TABLE_NAME + " (" +
                    "ID" + " INT " +
                    ");";

    private static final String LAST_UPDATED_TABLE_NAME = "lastupdated";
    private static final String LAST_UPDATED_TABLE_CREATE =
            "CREATE TABLE " + LAST_UPDATED_TABLE_NAME + " (" +
                    "DATETIME" + " DATETIME " +
                    ");";

    public RawMaterialFreezer(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

      database.execSQL(STUDYROOM_TABLE_CREATE);
      database.execSQL(STATISTICS_TABLE_CREATE);
      database.execSQL(CURRENTDATA_TABLE_CREATE);
      database.execSQL(FAVSTUDYROOM_TABLE_CREATE);
      database.execSQL(LAST_UPDATED_TABLE_CREATE);

      System.out.println("[Database] Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }

    public void insertInDatabase(String query) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
        database.close();
    }

}
