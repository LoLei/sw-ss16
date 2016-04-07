package com.sw_ss16.studyroompopulationpredicter.Startup;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sw_ss16.studyroompopulationpredicter.R;
import com.sw_ss16.studyroompopulationpredicter.Synchronisation.Sync;
import com.sw_ss16.studyroompopulationpredicter.backend.Database;
import com.sw_ss16.studyroompopulationpredicter.content.FavoriteStudyRoomsContent;
import com.sw_ss16.studyroompopulationpredicter.ui.studyroom.ListActivity;

/**
 * Created by daniel on 07.04.16.
 */
public class Startup extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //DO Sync Here

        final Database db = new Database(getApplicationContext());

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Pull updated data from the remote database, put into the local database
        // TODO: Do this not on every BaseActivity onCreate(), but like every two hours,
        // update current data more often than StudyRooms data
        Sync.insertStudyRoomsIntoSQLiteDB(queue, db);
        Sync.insertStatisticsIntoSQLiteDB(queue, db);
        Sync.insertCurrentDataIntoSQLiteDB(queue, db);
        // Favorite Study Rooms can be stored in the local database only
        Sync. insertFavoriteStudyRoomsIntoSQLiteDB(db);


        //SETUP LIST

        if(FavoriteStudyRoomsContent.ITEM_MAP.isEmpty()) {
            System.out.println("Fill List");
           
            SQLiteDatabase sqldb = db.getReadableDatabase();

            String[] columns = new String[]{"ID", "NAME", "DESCRIPTION", "ADDRESS", "IMAGE_IN", "IMAGE_OUT", "CAPACITY"};

            Cursor c = sqldb.query("studyrooms", columns, null, null, null, null, null);

            //c.getCount();
            c.moveToFirst();
            for (int i = 1; i <= c.getCount(); i++) {

                FavoriteStudyRoomsContent.addItem(new FavoriteStudyRoomsContent.DummyItem(c.getString(c.getColumnIndex("ID")), R.drawable.p1,
                        c.getString(c.getColumnIndex("NAME")),
                        c.getString(c.getColumnIndex("DESCRIPTION")),
                        c.getString(c.getColumnIndex("ADDRESS"))));
                c.moveToNext();
            }
        }




        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

}
