package com.sw_ss16.lc_app.content;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.sw_ss16.lc_app.backend.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Gets and retrieves Data from Database easily
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
    public void setApplicationContext(Context app_context) {
        this.app_context = app_context;
    }

    public long getNumberOfFavorites() {
        Database database = new Database(app_context);
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();

        long number_of_favorites = DatabaseUtils.queryNumEntries(sqLiteDatabase, "favstudyrooms");
        database.close();
        return number_of_favorites;
    }

    public boolean getLearningCenterFavoriteStatus(int lc_id) {

        Database database = new Database(app_context);
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();

        boolean is_fav = false;

        if ((DatabaseUtils.queryNumEntries(sqLiteDatabase, "favstudyrooms", "ID = " + lc_id)) > 0) {
            is_fav = true;
        }

        database.close();

        return is_fav;

    }

    public void setLearningCenterFavoriteStatus(int lc_id, boolean set_is_favorite) {

        if ((getLearningCenterFavoriteStatus(lc_id) && set_is_favorite) || (!getLearningCenterFavoriteStatus(lc_id) && !set_is_favorite)) {
            return;
        }
        else if (getLearningCenterFavoriteStatus(lc_id)) {
            Database database = new Database(app_context);
            database.insertInDatabase("DELETE FROM favstudyrooms WHERE ID = " + lc_id + ";");
            database.close();
        }
        else {
            Database database = new Database(app_context);
            database.insertInDatabase("INSERT INTO favstudyrooms VALUES (" + lc_id + ");");
            database.close();
        }
    }

    public List<String> getListOfFavLcIds() {
        ArrayList<String> all_lc_ids = new ArrayList<>();

        Database database = new Database(app_context);
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
        String[] columns = new String[]{"ID"};

        Cursor cursor = sqLiteDatabase.query("favstudyrooms", columns, null, null, null, null, null);

        cursor.moveToFirst();

        for (int i = 1; i <= cursor.getCount(); i++) {

            all_lc_ids.add(cursor.getString(cursor.getColumnIndex("ID")));
            cursor.moveToNext();
        }

        database.close();

        return all_lc_ids;
    }

    public List<String> getListOfLcIds() {
        ArrayList<String> all_lc_ids = new ArrayList<>();

        Database database = new Database(app_context);
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
        String[] columns = new String[]{"ID"};

        Cursor cursor = sqLiteDatabase.query("studyrooms", columns, null, null, null, null, null);

        cursor.moveToFirst();

        for (int i = 1; i <= cursor.getCount(); i++) {

            all_lc_ids.add(cursor.getString(cursor.getColumnIndex("ID")));
            cursor.moveToNext();
        }

        database.close();

        return all_lc_ids;
    }

    public LearningCenter getLcObject(String id) {

        Database database = new Database(app_context);
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();

        String[] columns = new String[]{"ID", "NAME", "DESCRIPTION", "ADDRESS", "IMAGE_IN", "IMAGE_OUT", "CAPACITY"};
        String[] favcolumns = new String[]{"ID"};

        String query_string = "ID = " + id;

        Cursor cursor = sqLiteDatabase.query("studyrooms", columns, query_string, null, null, null, "ID", "1");
        Cursor isfav = sqLiteDatabase.query("favstudyrooms", favcolumns, query_string, null, null, null, "ID", "1");

        cursor.moveToFirst();
        isfav.moveToFirst();

        LearningCenter learning_center = new LearningCenter(
                cursor.getString(cursor.getColumnIndex("ID")),
                cursor.getString(cursor.getColumnIndex("NAME")),
                cursor.getString(cursor.getColumnIndex("DESCRIPTION")),
                cursor.getString(cursor.getColumnIndex("ADDRESS")),
                cursor.getString(cursor.getColumnIndex("IMAGE_IN")),
                cursor.getString(cursor.getColumnIndex("IMAGE_OUT")),
                cursor.getString(cursor.getColumnIndex("CAPACITY"))
        );

        database.close();
        return learning_center;
    }


}
