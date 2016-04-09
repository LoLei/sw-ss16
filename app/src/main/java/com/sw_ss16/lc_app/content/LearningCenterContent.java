package com.sw_ss16.lc_app.content;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sw_ss16.lc_app.backend.Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gets and retrieves Data from Database easily
 *
 */
public class LearningCenterContent {


    // -------------------------------
    // Members
    // -------------------------------
    private Context app_context; //TODO give this at constructor, so it can be used in methots


    // -------------------------------
    // Methods
    // -------------------------------

    //
    public void setApplicationContext(Context app_context){
        this.app_context = app_context;
    }

    public void setLearningCeterToFavorite(int lc_id){

    }

    public void setLearningCeterToNoFavorite(int lc_id){

    }

    public List<String> getListOfFavLcIds(){
        ArrayList<String> all_lc_ids = new ArrayList<>();

        Database db = new Database(app_context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        String[] columns = new String[]{"ID"};

        Cursor c = sqldb.query("favstudyrooms", columns, null, null, null, null, null);

        c.moveToFirst();

        for (int i = 1; i <= c.getCount(); i++) {

            all_lc_ids.add(c.getString(c.getColumnIndex("ID")));
            c.moveToNext();
        }

        return all_lc_ids;
    }

    public List<String> getListOfLcIds(){
        ArrayList<String> all_lc_ids = new ArrayList<>();

        Database db = new Database(app_context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        String[] columns = new String[]{"ID"};

        Cursor c = sqldb.query("studyrooms", columns, null, null, null, null, null);

        c.moveToFirst();

        for (int i = 1; i <= c.getCount(); i++) {

            all_lc_ids.add(c.getString(c.getColumnIndex("ID")));
            c.moveToNext();
        }

        return all_lc_ids;
    }

    public LearningCenter getLcObject(String id){

        Database db = new Database(app_context);
        SQLiteDatabase sqldb = db.getReadableDatabase();

        String[] columns = new String[]{"ID", "NAME", "DESCRIPTION", "ADDRESS", "IMAGE_IN", "IMAGE_OUT", "CAPACITY"};
        String[] favcolumns = new String[]{"ID", "IS_FAV"};

        String query_string = "ID = " + id;

        Cursor c = sqldb.query("studyrooms", columns, query_string, null, null, null, "ID", "1");
        Cursor isfav = sqldb.query("favstudyrooms", favcolumns, query_string, null, null, null, "ID", "1");

        c.moveToFirst();
        isfav.moveToFirst();

        return new LearningCenter(c.getString(c.getColumnIndex("ID")),
                c.getString(c.getColumnIndex("IMAGE_IN")),
                c.getString(c.getColumnIndex("IMAGE_OUT")),
                c.getString(c.getColumnIndex("NAME")),
                c.getString(c.getColumnIndex("DESCRIPTION")),
                c.getString(c.getColumnIndex("ADDRESS")),
                (isfav.getInt(isfav.getColumnIndex("IS_FAV")) == 1));
    }

}
