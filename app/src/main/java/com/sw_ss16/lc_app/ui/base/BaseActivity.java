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

import java.util.List;

import static com.sw_ss16.lc_app.util.LogUtil.logD;
import static com.sw_ss16.lc_app.util.LogUtil.makeLogTag;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = makeLogTag(BaseActivity.class);

    protected static final int NAV_DRAWER_ITEM_INVALID = -1;

    private DrawerLayout drawerLayout;
    private Toolbar actionBarToolbar;

    private DatabaseSyncer database_syncer = new DatabaseSyncer();

    private LearningCenterContent lc_contentmanager = new LearningCenterContent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final Database database = new Database(getApplicationContext());

        RequestQueue queue = Volley.newRequestQueue(this);


        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean auto_update = sharedPref.getBoolean("pref_settings_1", false);


        if (firstrun || auto_update) {
            System.out.println("This App first started or has auto update activated -> full update");
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();
            database_syncer.syncAllRemoteIntoSQLiteDB(queue, database, this);
        }
        if (firstrun) {
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

    private void setupNavDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout == null) {
            return;
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
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

    private void setSelectedItem(NavigationView navigationView) {
        int selectedItem = getSelfNavDrawerItem(); // subclass has to override this method
        navigationView.setCheckedItem(selectedItem);
    }

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

    private void onNavigationItemClicked(final MenuItem menuItem) {
        if (menuItem.getItemId() == getSelfNavDrawerItem()) {
            closeDrawer();
            return;
        }

        goToNavDrawerItem(menuItem);
    }

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
    }

    protected ActionBar getActionBarToolbar() {
        if (actionBarToolbar == null) {
            actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (actionBarToolbar != null) {
                setSupportActionBar(actionBarToolbar);
            }
        }
        return getSupportActionBar();
    }

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
}
