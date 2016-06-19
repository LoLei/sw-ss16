package com.sw_ss16.lc_app.ui.learning_center_list;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.content.LearningCenterDefroster;
import com.sw_ss16.lc_app.ui.base.BaseActivity;
import com.sw_ss16.lc_app.ui.learning_center_one.StudyRoomDetailActivity;
import com.sw_ss16.lc_app.ui.learning_center_one.StudyRoomDetailFragment;
import com.sw_ss16.lc_app.util.LogUtil;

public class ListActivity extends BaseActivity implements StudyRoomListFragment.Callback {
    /**
     * Whether or not the activity is running on a device with a large screen
     */
    private boolean twoPaneMode;

    private LearningCenterDefroster lc_contentmanager = new LearningCenterDefroster();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        PreferenceManager.setDefaultValues(this, R.xml.settings_prefs, true);

        lc_contentmanager.setApplicationContext(getApplicationContext());
        if (lc_contentmanager.getNumberOfFavorites() == 0) {
            findViewById(R.id.textView_no_fav).setVisibility(View.VISIBLE);
        }

        //Check if no favorites
        lc_contentmanager.setApplicationContext(getApplicationContext());
        if(lc_contentmanager.getNumberOfFavorites() == 0){
            findViewById(R.id.textView_no_fav).setVisibility(View.VISIBLE);
        }

        setupToolbar();

        if (isTwoPaneLayoutUsed()) {
            twoPaneMode = true;
            LogUtil.logD("TEST", "TWO POANE TASDFES");
            enableActiveItemState();
        }

        if (savedInstanceState == null && twoPaneMode) {
            setupDetailFragment();
        }
    }

    /**
     * Called when an item has been selected
     *
     * @param id the selected quote ID
     */
    @Override
    public void onItemSelected(String id) {
        if (twoPaneMode) {
            StudyRoomDetailFragment fragment = StudyRoomDetailFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
        }
        else {
            Intent detailIntent = new Intent(this, StudyRoomDetailActivity.class);
            detailIntent.putExtra(StudyRoomDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupDetailFragment() {

        lc_contentmanager.setApplicationContext(getApplicationContext());
        StudyRoomDetailFragment fragment = StudyRoomDetailFragment.newInstance(lc_contentmanager.getListOfFavLcIds().get(0));

        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }

    /**
     * Enables the functionality that selected items are automatically highlighted.
     */
    private void enableActiveItemState() {
        StudyRoomListFragment fragmentById = (StudyRoomListFragment) getFragmentManager().findFragmentById(R.id.article_list);
        fragmentById.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /**
     * Is the container present? If so, we are using the two-pane layout.
     *
     * @return true if the two pane layout is used.
     */
    private boolean isTwoPaneLayoutUsed() {
        return findViewById(R.id.article_detail_container) != null;
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
        return R.id.nav_quotes;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }
}
