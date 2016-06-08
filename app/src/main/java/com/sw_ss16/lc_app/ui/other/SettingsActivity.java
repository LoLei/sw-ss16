package com.sw_ss16.lc_app.ui.other;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.backend.RawMaterialFreezer;
import com.sw_ss16.lc_app.backend.ResourceFetcher;
import com.sw_ss16.lc_app.ui.base.BaseActivity;

/**
 * This Activity provides several settings. Activity contains {@link PreferenceFragment} as inner class.
 * <p/>
 * Created by Andreas Schrade on 14.12.2015.
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbar();
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_settings;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

    public static class SettingsFragment extends PreferenceFragment {
        public SettingsFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_prefs);
            addUpdateNowListener();
        }

        private void addUpdateNowListener() {
            Preference reset = (Preference) findPreference("pref_settings_2");
            reset.setOnPreferenceClickListener(new OnPreferenceClickListener()
            {
                public boolean onPreferenceClick(Preference pref)
                {
                    System.out.println("Updating SQLite database now");
                    RawMaterialFreezer database = new RawMaterialFreezer(getActivity().getApplicationContext());
                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    ResourceFetcher database_syncer = new ResourceFetcher();

                    database_syncer.syncAllRemoteIntoSQLiteDBNOW(queue, database, getActivity().getApplicationContext());
                    return true;
                }
            });
        }


    }

}
