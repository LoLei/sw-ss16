package com.sw_ss16.studyroompopulationpredicter.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sw_ss16.studyroompopulationpredicter.R;
import com.sw_ss16.studyroompopulationpredicter.backend.Database;
import com.sw_ss16.studyroompopulationpredicter.ui.SettingsActivity;
import com.sw_ss16.studyroompopulationpredicter.ui.studyroom.ListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.sw_ss16.studyroompopulationpredicter.util.LogUtil.logD;
import static com.sw_ss16.studyroompopulationpredicter.util.LogUtil.makeLogTag;


/**
 * The base class for all Activity classes.
 * This class creates and provides the navigation drawer and toolbar.
 * The navigation logic is handled in {@link BaseActivity#goToNavDrawerItem(int)}
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = makeLogTag(BaseActivity.class);

    protected static final int NAV_DRAWER_ITEM_INVALID = -1;

    private DrawerLayout drawerLayout;
    private Toolbar actionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
        final Database db = new Database(getApplicationContext());

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Pull updated data from the remote database, put into the local database
        // TODO: Do this not on every BaseActivity onCreate(), but like every two hours,
        // update current data more often than StudyRooms data
        insertStudyRoomsIntoSQLiteDB(queue, db);
        insertStatisticsIntoSQLiteDB(queue, db);
        insertCurrentDataIntoSQLiteDB(queue, db);
        // Favorite Study Rooms can be stored in the local database only
        insertFavoriteStudyRoomsIntoSQLiteDB(db);
    }

    private void insertStudyRoomsIntoSQLiteDB(RequestQueue queue, final Database db) {
        String url = "http://danielgpoint.at/predict.php?what=lc&how_much=all";

        // Request a string response from the provided URL.
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
                                //TODO: Image IN
                                //TODO: Image OUT
                                System.out.println(id + " " + name + " " + address);
                                db.insertInDatabase("INSERT INTO studyrooms (ID, NAME, DESCRIPTION, ADDRESS, IMAGE_IN, IMAGE_OUT, CAPACITY) " +
                                        "SELECT " +
                                        id + "," +
                                        "'" + name + "'," +
                                        "'" + description + "'," +
                                        "'" + address + "'," +
                                        "null," + // TODO: image
                                        "null," + // TODO: image
                                        capacity + " " +
                                        "WHERE NOT EXISTS (SELECT 1 FROM studyrooms WHERE ID = " + id +");");


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

    private void insertStatisticsIntoSQLiteDB(RequestQueue queue, final Database db) {
        String url = "http://danielgpoint.at/predict.php?what=stat&how_much=all";

        // Request a string response from the provided URL.
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

    private void insertCurrentDataIntoSQLiteDB(RequestQueue queue, Database db) {

    }

    private void insertFavoriteStudyRoomsIntoSQLiteDB(Database db) {

    }

    /**
     * Sets up the navigation drawer.
     */
    private void setupNavDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout == null) {
            // current activity does not have a drawer.
            return;
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerSelectListener(navigationView);
            setSelectedItem(navigationView);
        }

        logD(TAG, "navigation drawer setup finished");
    }

    /**
     * Updated the checked item in the navigation drawer
     * @param navigationView the navigation view
     */
    private void setSelectedItem(NavigationView navigationView) {
        // Which navigation item should be selected?
        int selectedItem = getSelfNavDrawerItem(); // subclass has to override this method
        navigationView.setCheckedItem(selectedItem);
    }

    /**
     * Creates the item click listener.
     * @param navigationView the navigation view
     */
    private void setupDrawerSelectListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        drawerLayout.closeDrawers();
                        onNavigationItemClicked(menuItem.getItemId());
                        return true;
                    }
                });
    }

    /**
     * Handles the navigation item click.
     * @param itemId the clicked item
     */
    private void onNavigationItemClicked(final int itemId) {
        if(itemId == getSelfNavDrawerItem()) {
            // Already selected
            closeDrawer();
            return;
        }

        goToNavDrawerItem(itemId);
    }

    /**
     * Handles the navigation item click and starts the corresponding activity.
     * @param item the selected navigation item
     */
    private void goToNavDrawerItem(int item) {
        switch (item) {
            case R.id.nav_quotes:
                startActivity(new Intent(this, ListActivity.class));
                finish();
                break;
            /* case R.id.nav_samples:
                startActivity(new Intent(this, ViewSamplesActivity.class));
                break;*/
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }

    /**
     * Provides the action bar instance.
     * @return the action bar.
     */
    protected ActionBar getActionBarToolbar() {
        if (actionBarToolbar == null) {
            actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (actionBarToolbar != null) {
                setSupportActionBar(actionBarToolbar);
            }
        }
        return getSupportActionBar();
    }


    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * have to override this method.
     */
    protected int getSelfNavDrawerItem() {
        return NAV_DRAWER_ITEM_INVALID;
    }

    protected void openDrawer() {
        if(drawerLayout == null)
            return;

        drawerLayout.openDrawer(GravityCompat.START);
    }

    protected void closeDrawer() {
        if(drawerLayout == null)
            return;

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public abstract boolean providesActivityToolbar();

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
