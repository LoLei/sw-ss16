package com.sw_ss16.studyroompopulationpredicter.backend;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mrb on 08/04/16.
 */
public class DatabaseSyncer {

    public void syncAllRemoteIntoSQLiteDB(RequestQueue queue, final Database db){
        syncStudyRoomsIntoSQLiteDB(queue, db);
        syncStatisticsIntoSQLiteDB(queue, db);
        syncCurrentDataIntoSQLiteDB(queue, db);
    }

    public void syncStudyRoomsIntoSQLiteDB(RequestQueue queue, final Database db) {
        String url = "http://danielgpoint.at/predict.php?what=lc&how_much=all";

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url , null,
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

                                db.insertInDatabase("INSERT INTO studyrooms (ID, NAME, DESCRIPTION, ADDRESS, IMAGE_IN, IMAGE_OUT, CAPACITY) " +
                                        "SELECT " +
                                        id + "," +
                                        "'" + name + "', " +
                                        "'" + description + "', " +
                                        "'" + address + "', " +
                                        "'" + image_in + "', " +
                                        "'" + image_out + "', " +
                                        capacity + " " +
                                        "WHERE NOT EXISTS (SELECT 1 FROM studyrooms WHERE ID = " + id + ");");

                                db.insertInDatabase("INSERT INTO favstudyrooms (ID, IS_FAV) " +
                                        "SELECT " +
                                        id + "," +
                                        "0 " +
                                        "WHERE NOT EXISTS (SELECT 1 FROM favstudyrooms WHERE ID = " + id +");");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    public void syncStatisticsIntoSQLiteDB(RequestQueue queue, final Database db) {
        String url = "http://danielgpoint.at/predict.php?what=stat&how_much=all";

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url , null,
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
                                System.out.println(id + " " + lc_id + " " + weekday);
                                db.insertInDatabase("INSERT INTO statistics (ID, LC_ID, WEEKDAY, HOUR, FULLNESS ) " +
                                        "SELECT " +
                                        id + "," +
                                        "" + lc_id + ", " +
                                        "" + weekday + ", " +
                                        "" + hour + ", " +
                                        "" + fullness + " " +
                                        "WHERE NOT EXISTS (SELECT 1 FROM statistics WHERE ID = " + id +");");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);

    }

    public void syncCurrentDataIntoSQLiteDB(RequestQueue queue, final Database db) {
        String url = "http://danielgpoint.at/predict.php?what=curr&how_much=all";

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url , null,
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
                                db.insertInDatabase("INSERT INTO current_data (ID, LC_ID, HOUR, FULLNESS, DATE) " +
                                        "SELECT " +
                                        id + "," +
                                        "" + lc_id + ", " +
                                        "" + hour + ", " +
                                        "" + fullness + ", " +
                                        "'" + date + "' " +
                                        "WHERE NOT EXISTS (SELECT 1 FROM current_data WHERE ID = " + id +");");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);

    }
}
