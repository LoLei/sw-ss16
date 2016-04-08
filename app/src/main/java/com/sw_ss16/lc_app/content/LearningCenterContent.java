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
    /**
     * Current Map of all
     */
    public static final List<LearningCenter> learning_centers_ = new ArrayList<>();

    /**
     * A map of sample items. Key: sample ID; Value: Item. TODO copypasta
     */
    public static final Map<String, LearningCenter> learning_centers_map_ = new HashMap<>(5);


    // -------------------------------
    // Methods
    // -------------------------------

    public static void addLearningCenter(LearningCenter item) {
        learning_centers_.add(item);
        learning_centers_map_.put(item.id, item);
    }

    /**
     * TODO this should be called on startup of application
     * @param application_context
     */
    public void generateMapsFromDB(Context application_context) {
        System.out.println("Fill List");
        Database db = new Database(application_context);
        SQLiteDatabase sqldb = db.getReadableDatabase();

        String[] columns = new String[]{"ID", "NAME", "DESCRIPTION", "ADDRESS", "IMAGE_IN", "IMAGE_OUT", "CAPACITY"};
        String[] favcolumns = new String[]{"ID", "IS_FAV"};


        Cursor c = sqldb.query("studyrooms", columns, null, null, null, null, null);
        Cursor isfav = sqldb.query("favstudyrooms", favcolumns, null, null, null, null, null);

        c.moveToFirst();
        isfav.moveToFirst();

        for (int i = 1; i <= c.getCount(); i++) {

            addLearningCenter(new LearningCenter(c.getString(c.getColumnIndex("ID")),
                    c.getString(c.getColumnIndex("IMAGE_IN")),
                    c.getString(c.getColumnIndex("IMAGE_OUT")),
                    c.getString(c.getColumnIndex("NAME")),
                    c.getString(c.getColumnIndex("DESCRIPTION")),
                    c.getString(c.getColumnIndex("ADDRESS")),
                    (isfav.getInt(isfav.getColumnIndex("IS_FAV")) == 1)));

            // TODO: Remove i == 1
            if (isfav.getInt(isfav.getColumnIndex("IS_FAV")) == 1 || i == 1) {
                addLearningCenter(new LearningCenter(c.getString(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("IMAGE_IN")),
                        c.getString(c.getColumnIndex("IMAGE_OUT")),
                        c.getString(c.getColumnIndex("NAME")),
                        c.getString(c.getColumnIndex("DESCRIPTION")),
                        c.getString(c.getColumnIndex("ADDRESS")),
                        (isfav.getInt(isfav.getColumnIndex("IS_FAV")) == 1)));
            }

            c.moveToNext();
            isfav.moveToNext();

        }


    }


    public Map<String, LearningCenter> getMapOfAllLearningCenters(){

        return null;
    }

    public void setLearningCeterToFavorite(int lc_id){

    }

    public void setLearningCeterToNoFavorite(int lc_id){

    }

    public List<String> getListOfLcIds(Context application_context){
        ArrayList<String> all_lc_ids = new ArrayList<>();

        Database db = new Database(application_context);
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

    public LearningCenter getLcObject(String id, Context application_context){

        Database db = new Database(application_context);
        SQLiteDatabase sqldb = db.getReadableDatabase();

        String[] columns = new String[]{"ID", "NAME", "DESCRIPTION", "ADDRESS", "IMAGE_IN", "IMAGE_OUT", "CAPACITY"};
        String[] favcolumns = new String[]{"ID", "IS_FAV"};

        String query_string = "ID equals " + id;

        Cursor c = sqldb.query("studyrooms", columns, query_string, null, null, null, "ID", "1");
        Cursor isfav = sqldb.query("favstudyrooms", favcolumns, query_string, null, null, "ID", "1");

        return new LearningCenter(c.getString(c.getColumnIndex("ID")),
                c.getString(c.getColumnIndex("IMAGE_IN")),
                c.getString(c.getColumnIndex("IMAGE_OUT")),
                c.getString(c.getColumnIndex("NAME")),
                c.getString(c.getColumnIndex("DESCRIPTION")),
                c.getString(c.getColumnIndex("ADDRESS")),
                (isfav.getInt(isfav.getColumnIndex("IS_FAV")) == 1));
    }

}
