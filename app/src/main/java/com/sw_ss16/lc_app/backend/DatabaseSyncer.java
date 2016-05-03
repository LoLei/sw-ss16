package com.sw_ss16.lc_app.backend;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
public class DatabaseSyncer {

    // -------------------------------
    // Members
    // -------------------------------


    // -------------------------------
    // Methods
    // -------------------------------
    public void syncAllRemoteIntoSQLiteDB(RequestQueue queue, final Database database, Context context) {
        syncStudyRoomsIntoSQLiteDB(queue, database, context);
        syncStatisticsIntoSQLiteDB(queue, database);
        syncCurrentDataIntoSQLiteDB(queue, database);
        database.close();
    }

    public void syncStudyRoomsIntoSQLiteDB(final RequestQueue queue, final Database database, final Context context) {
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
                        Calendar calendar = Calendar.getInstance();
                        System.out.println("Current time => " + calendar.getTime());

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
                        String formattedDate = simpleDateFormat.format(calendar.getTime());
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("date_last_update", formattedDate).commit();

                        Toast.makeText(context, "New Update, Restarting now", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context, ListActivity.class);

                        context.startActivity(intent);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Data retrieval failed");
            }
        });



        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("response" + response.toString());
                boolean nodate = false;
                try {
                    String date = response.getString("datetime");
                    String date2 = PreferenceManager.getDefaultSharedPreferences(context).getString("date_last_update", "");
                    System.out.println("Date: " + date + " Date2: " + date2);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
                    Date convertedDate = new Date();
                    Date convertedDate2 = new Date();
                    try {
                        convertedDate = dateFormat.parse(date);
                        if(date2.isEmpty())
                        {
                            nodate = true;
                        }
                        else {
                            convertedDate2 = dateFormat2.parse(date2);
                        }



                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if(nodate || convertedDate.after(convertedDate2)) {
                        System.out.println("Remote DB after internal database, updating now");
                        queue.add(jsonArrayRequest);

                    }


                } catch (JSONException err) {

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Here was an Error");

            }
        });



        // Add the request to the RequestQueue.

        queue.add(jsonObjectRequest);
    }

    public void syncStatisticsIntoSQLiteDB(RequestQueue queue, final Database database) {
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

    public void syncCurrentDataIntoSQLiteDB(RequestQueue queue, final Database database) {
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
