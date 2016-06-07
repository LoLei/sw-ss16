package com.sw_ss16.lc_app.ui.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.backend.Database;
import com.sw_ss16.lc_app.backend.DatabaseSyncer;
import com.sw_ss16.lc_app.content.LearningCenter;
import com.sw_ss16.lc_app.content.LearningCenterContent;
import com.sw_ss16.lc_app.ui.learning_center_list.ListActivity;
import com.sw_ss16.lc_app.ui.learning_center_one.StudyRoomDetailActivity;
import com.sw_ss16.lc_app.ui.learning_center_one.StudyRoomDetailFragment;
import com.sw_ss16.lc_app.ui.other.SettingsActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static com.sw_ss16.lc_app.util.LogUtil.logD;
import static com.sw_ss16.lc_app.util.LogUtil.makeLogTag;


/**
 * The base class for all Activity classes.
 * This class creates and provides the navigation drawer and toolbar.
 * The navigation logic is handled in {@linkk BaseActivity#goToNavDrawerItem(int)}
 * <p/>
 * Created by Andreas Schrade on 14.12.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    // -------------------------------
    // Members
    // -------------------------------
    private static final String TAG = makeLogTag(BaseActivity.class);

    protected static final int NAV_DRAWER_ITEM_INVALID = -1;

    private DrawerLayout drawerLayout;
    private Toolbar actionBarToolbar;

    private DatabaseSyncer database_syncer = new DatabaseSyncer();

    private LearningCenterContent lc_contentmanager = new LearningCenterContent();


    // -------------------------------
    // Methods
    // -------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // TODO: Check if first startup, if yes

        final Database database = new Database(getApplicationContext());

        // Volley DB Queue
        RequestQueue queue = Volley.newRequestQueue(this);


        // Pull updated data from the remote database, put into the local database
        // TODO: Do this not on every BaseActivity onCreate(), but like every two hours,
        // update current data more often than StudyRooms data



        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean auto_update = sharedPref.getBoolean("pref_settings_1", false);

        
         if(firstrun || auto_update) {
            System.out.println("This App first started or has auto update activated -> full update");
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();
            database_syncer.syncAllRemoteIntoSQLiteDB(queue, database, this);
        }
         if(firstrun) {
             System.out.println("First start");
             Toast.makeText(this, "Please wait for Update", Toast.LENGTH_LONG).show();
         }

        else {
            System.out.println("Auto Update is deactivated");
        }




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
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
            // Add all study rooms to navdrawer
            Menu m = navigationView.getMenu();
            SubMenu all_study_rooms = m.getItem(2).getSubMenu();

            lc_contentmanager.setApplicationContext(getApplicationContext());
            List<String> lc_ids = lc_contentmanager.getListOfLcIds();

            for (int i = 0; i < lc_ids.size(); i++) {
                LearningCenter curr_lc = lc_contentmanager.getLcObject(lc_ids.get(i));
                all_study_rooms.add(curr_lc.name);
                all_study_rooms.getItem(i).setIcon(R.drawable.ic_school_white_24dp);
                all_study_rooms.getItem(i).setNumericShortcut((char) i);
            }
        }

        setupDrawerSelectListener(navigationView);
        setSelectedItem(navigationView);

        logD(TAG, "navigation drawer setup finished");
    }

    /**
     * Updated the checked item in the navigation drawer
     *
     * @param navigationView the navigation view
     */
    private void setSelectedItem(NavigationView navigationView) {
        // Which navigation item should be selected?
        int selectedItem = getSelfNavDrawerItem(); // subclass has to override this method
        navigationView.setCheckedItem(selectedItem);
    }

    /**
     * Creates the item click listener.
     *
     * @param navigationView the navigation view
     */
    private void setupDrawerSelectListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        drawerLayout.closeDrawers();
                        onNavigationItemClicked(menuItem);
                        return true;
                    }
                });
    }

    /**
     * Handles the navigation item click.
     *
     * @paramm itemId the clicked item
     */
    private void onNavigationItemClicked(final MenuItem menuItem) {
        if (menuItem.getItemId() == getSelfNavDrawerItem()) {
            // Already selected
            closeDrawer();
            return;
        }

        goToNavDrawerItem(menuItem);
    }

    /**
     * Handles the navigation item click and starts the corresponding activity.
     *
     * @paramm item the selected navigation item
     */
    private void goToNavDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
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

            default:
                Intent detailIntent = new Intent(this, StudyRoomDetailActivity.class);
                detailIntent.putExtra(StudyRoomDetailFragment.ARG_ITEM_ID, Integer.toString(((int) menuItem.getNumericShortcut()) + 1));
                startActivity(detailIntent);
                break;
        }
        /*
        // TODO: Check for twopane mode -> old unused code, remove if not needed anymore
        if (false) {
            // Show the quote detail information by replacing the DetailFragment via transaction.
            StudyRoomDetailFragment fragment = StudyRoomDetailFragment.newInstance(Character.toString(menuItem.getNumericShortcut()));
            getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
        }

         else {

        }
        */
    }

    /**
     * Provides the action bar instance.
     *
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
        if (drawerLayout == null)
            return;

        drawerLayout.openDrawer(GravityCompat.START);
    }

    protected void closeDrawer() {
        if (drawerLayout == null)
            return;

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public abstract boolean providesActivityToolbar();

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    // -------------------------------
    // Methods that happened to not be used
    // -------------------------------

    /**
     * http://stackoverflow.com/a/7331698/4129221
     *
     * @param url the location of the image on the server
     * @return the byte array generated from the URL
     */
    private byte[] getImage(String url) {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            // ByteArrayBuffer deprecated, use ByteArrayOutputStream instead
            // ByteArrayBuffer baf = new ByteArrayBuffer(500);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[500];
            int current = 0;
            while ((current = bis.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, current);
            }

            return buffer.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }
}
