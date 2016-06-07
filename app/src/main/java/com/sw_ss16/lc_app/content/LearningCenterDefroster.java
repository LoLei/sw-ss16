package com.sw_ss16.lc_app.content;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.sw_ss16.lc_app.backend.RawMaterialFreezer;

import java.util.ArrayList;
import java.util.List;

/**
 * Gets and retrieves Data from RawMaterialFreezer easily
 */
public class LearningCenterDefroster {


    // -------------------------------
    // Members
    // -------------------------------
    private Context app_context; //TODO give this at constructor, so it can be used in methots


    // -------------------------------
    // Methods
    // -------------------------------

    //
    public void setApplicationContext(Context app_context) {
        this.app_context = app_context;
    }

    public long getNumberOfFavorites() {
        RawMaterialFreezer db = new RawMaterialFreezer(app_context);
        SQLiteDatabase sqldb = db.getReadableDatabase();

        long number_of_favorites = DatabaseUtils.queryNumEntries(sqldb, "favstudyrooms");
        db.close();
        return number_of_favorites;
    }

    public boolean getLearningCenterFavoriteStatus(int lc_id) {

        RawMaterialFreezer db = new RawMaterialFreezer(app_context);
        SQLiteDatabase sqldb = db.getReadableDatabase();

        boolean is_fav = false;

        if ((DatabaseUtils.queryNumEntries(sqldb, "favstudyrooms", "ID = " + lc_id)) > 0) {
            is_fav = true;
        }

        db.close();

        return is_fav;

    }

    public void setLearningCenterFavoriteStatus(int lc_id, boolean set_is_favorite) {

        if ((getLearningCenterFavoriteStatus(lc_id) && set_is_favorite) || (!getLearningCenterFavoriteStatus(lc_id) && !set_is_favorite)) {
            return;
        }
        else if (getLearningCenterFavoriteStatus(lc_id)) {
            RawMaterialFreezer db = new RawMaterialFreezer(app_context);
            db.insertInDatabase("DELETE FROM favstudyrooms WHERE ID = " + lc_id + ";");
            db.close();
        }
        else {
            RawMaterialFreezer db = new RawMaterialFreezer(app_context);
            db.insertInDatabase("INSERT INTO favstudyrooms VALUES (" + lc_id + ");");
            db.close();
        }
    }

    public List<String> getListOfFavLcIds() {
        ArrayList<String> all_lc_ids = new ArrayList<>();

        RawMaterialFreezer db = new RawMaterialFreezer(app_context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        String[] columns = new String[]{"ID"};

        Cursor c = sqldb.query("favstudyrooms", columns, null, null, null, null, null);

        c.moveToFirst();

        for (int i = 1; i <= c.getCount(); i++) {

            all_lc_ids.add(c.getString(c.getColumnIndex("ID")));
            c.moveToNext();
        }

        db.close();

        return all_lc_ids;
    }

    public List<String> getListOfLcIds() {
        ArrayList<String> all_lc_ids = new ArrayList<>();

        RawMaterialFreezer db = new RawMaterialFreezer(app_context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        String[] columns = new String[]{"ID"};

        Cursor c = sqldb.query("studyrooms", columns, null, null, null, null, null);

        c.moveToFirst();

        for (int i = 1; i <= c.getCount(); i++) {

            all_lc_ids.add(c.getString(c.getColumnIndex("ID")));
            c.moveToNext();
        }

        db.close();

        return all_lc_ids;
    }

    public LearningCenter getLcObject(String id) {

        RawMaterialFreezer db = new RawMaterialFreezer(app_context);
        SQLiteDatabase sqldb = db.getReadableDatabase();

        String[] columns = new String[]{"ID", "NAME", "DESCRIPTION", "ADDRESS", "IMAGE_IN", "IMAGE_OUT", "CAPACITY"};
        String[] favcolumns = new String[]{"ID"};

        String query_string = "ID = " + id;

        Cursor c = sqldb.query("studyrooms", columns, query_string, null, null, null, "ID", "1");
        Cursor isfav = sqldb.query("favstudyrooms", favcolumns, query_string, null, null, null, "ID", "1");

        c.moveToFirst();
        isfav.moveToFirst();

        LearningCenter learning_center = new LearningCenter(
                c.getString(c.getColumnIndex("ID")),
                c.getString(c.getColumnIndex("NAME")),
                c.getString(c.getColumnIndex("DESCRIPTION")),
                c.getString(c.getColumnIndex("ADDRESS")),
                c.getString(c.getColumnIndex("IMAGE_IN")),
                c.getString(c.getColumnIndex("IMAGE_OUT")),
                c.getString(c.getColumnIndex("CAPACITY"))
        );

        db.close();
        return learning_center;
    }


}
