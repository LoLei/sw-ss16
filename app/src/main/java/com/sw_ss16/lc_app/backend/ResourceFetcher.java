package com.sw_ss16.lc_app.backend;


import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sw_ss16.lc_app.ui.learning_center_list.ListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mrb on 08/04/16.
 */
public class ResourceFetcher {

    public void syncAllRemoteIntoSQLiteDB(RequestQueue queue, final RawMaterialFreezer database, Context context) {
        Calendar calendar = Calendar.getInstance();
        String date_last_update = PreferenceManager.getDefaultSharedPreferences(context).getString("date_last_update", "");
        Date this_time = calendar.getTime();

        SimpleDateFormat dateFormat_now = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        SimpleDateFormat dateFormat_last_update = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        Date convertedDate_now = new Date();
        Date convertedDate_last_update = new Date();
        try {
                convertedDate_now = dateFormat_now.parse(dateFormat_now.format(this_time));
                convertedDate_last_update = dateFormat_last_update.parse(date_last_update);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = convertedDate_now.getTime() - convertedDate_last_update.getTime();
        diff = diff /1000/60;
        System.out.println("Date_now: " + convertedDate_now + " Date_lastUpdate: " + date_last_update + "DIFF: " + diff);


        if(diff >= 60){
            syncStudyRoomsIntoSQLiteDB(queue, database, context);
            syncStatisticsIntoSQLiteDB(queue, database);
            syncCurrentDataIntoSQLiteDB(queue, database);


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            String formattedDate = simpleDateFormat.format(convertedDate_now);
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("date_last_update", formattedDate).commit();

            Toast.makeText(context, "New Update, Restarting now", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, ListActivity.class);

            context.startActivity(intent);

        }
      database.close();
    }

    public void syncAllRemoteIntoSQLiteDBNOW(RequestQueue queue, final RawMaterialFreezer database, Context context) {

        syncStudyRoomsIntoSQLiteDB(queue, database, context);
        syncStatisticsIntoSQLiteDB(queue, database);
        syncCurrentDataIntoSQLiteDB(queue, database);

        Calendar calendar = Calendar.getInstance();
        Date this_time = calendar.getTime();

        SimpleDateFormat dateFormat_now = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        Date convertedDate_now = new Date();
        try {
            convertedDate_now = dateFormat_now.parse(dateFormat_now.format(this_time));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        String formattedDate = simpleDateFormat.format(convertedDate_now);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("date_last_update", formattedDate).commit();

        database.close();
    }



    public void syncStudyRoomsIntoSQLiteDB(final RequestQueue queue, final RawMaterialFreezer database, final Context context) {
        String url = "http://danielgpoint.at/predict.php?what=lc&how_much=all";
        String url2 = "http://danielgpoint.at/predict.php?what=last_updated";

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String description = jsonObject.getString("description");
                                String address = jsonObject.getString("address");
                                String capacity = jsonObject.getString("capacity");
                                String image_in = jsonObject.getString("image_in");
                                String image_out = jsonObject.getString("image_out");
                                System.out.println(id + " " + name + " " + address + " " + image_in + " " + image_out);

                                database.insertInDatabase("INSERT INTO studyrooms (ID, NAME, DESCRIPTION, ADDRESS, IMAGE_IN, IMAGE_OUT, CAPACITY) " +
                                        "SELECT " +
                                        id + "," +
                                        "'" + name + "', " +
                                        "'" + description + "', " +
                                        "'" + address + "', " +
                                        "'" + image_in + "', " +
                                        "'" + image_out + "', " +
                                        capacity + " " +
                                        "WHERE NOT EXISTS (SELECT 1 FROM studyrooms WHERE ID = " + id + ");");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Data retrieval failed");
            }
        });

/*
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Here was an Error");

            }
        });

*/
        queue.add(jsonArrayRequest);
    }

    public void syncStatisticsIntoSQLiteDB(RequestQueue queue, final RawMaterialFreezer database) {
        String url = "http://danielgpoint.at/predict.php?what=stat&how_much=all";

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String lc_id = jsonObject.getString("lc_id");
                                String weekday = jsonObject.getString("weekday");
                                String hour = jsonObject.getString("hour");
                                String fullness = jsonObject.getString("fullness");
                                database.insertInDatabase("INSERT INTO statistics (ID, LC_ID, WEEKDAY, HOUR, FULLNESS ) " +
                                        "SELECT " +
                                        id + "," +
                                        "" + lc_id + ", " +
                                        "" + weekday + ", " +
                                        "" + hour + ", " +
                                        "" + fullness + " " +
                                        "WHERE NOT EXISTS (SELECT 1 FROM statistics WHERE ID = " + id + ");");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Data retrieval failed");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);

    }

    public void syncCurrentDataIntoSQLiteDB(RequestQueue queue, final RawMaterialFreezer database) {
        String url = "http://danielgpoint.at/predict.php?what=curr&how_much=all";

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String lc_id = jsonObject.getString("lc_id");
                                String date = jsonObject.getString("date");
                                String hour = jsonObject.getString("hour");
                                String fullness = jsonObject.getString("fullness");
                                System.out.println(id + " " + lc_id + " " + date);
                                database.insertInDatabase("INSERT INTO current_data (ID, LC_ID, HOUR, FULLNESS, DATE) " +
                                        "SELECT " +
                                        id + "," +
                                        "" + lc_id + ", " +
                                        "" + hour + ", " +
                                        "" + fullness + ", " +
                                        "'" + date + "' " +
                                        "WHERE NOT EXISTS (SELECT 1 FROM current_data WHERE ID = " + id + ");");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Data retrieval failed");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);

    }
}
