package com.sw_ss16.studyroompopulationpredicter.ui.studyroom;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.sw_ss16.studyroompopulationpredicter.R;
import com.sw_ss16.studyroompopulationpredicter.backend.Database;
import com.sw_ss16.studyroompopulationpredicter.content.FavoriteStudyRoomsContent;
import com.sw_ss16.studyroompopulationpredicter.ui.base.BaseActivity;
import com.sw_ss16.studyroompopulationpredicter.util.LogUtil;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ListActivity extends BaseActivity implements StudyRoomListFragment.Callback {
    /**
     * Whether or not the activity is running on a device with a large screen
     */
    private boolean twoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if(FavoriteStudyRoomsContent.ITEM_MAP.isEmpty()) {
            System.out.println("Fill List");
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sqldb = db.getReadableDatabase();

            String[] columns = new String[]{"ID", "NAME", "DESCRIPTION", "ADDRESS", "IMAGE_IN", "IMAGE_OUT", "CAPACITY"};

            Cursor c = sqldb.query("studyrooms", columns, null, null, null, null, null);
            Cursor isfav = sqldb.query("favstudyrooms", columns, null, null, null, null, null);

            //c.getCount();
            c.moveToFirst();
            isfav.moveToFirst();
            for (int i = 1; i <= c.getCount(); i++) {

                FavoriteStudyRoomsContent.addItem(new FavoriteStudyRoomsContent.DummyItem(c.getString(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("IMAGE_IN")),
                        c.getString(c.getColumnIndex("IMAGE_OUT")),
                        c.getString(c.getColumnIndex("NAME")),
                        c.getString(c.getColumnIndex("DESCRIPTION")),
                        c.getString(c.getColumnIndex("ADDRESS")),
                        (isfav.getInt(isfav.getColumnIndex("IS_FAV")) == 1)));
                c.moveToNext();
                isfav.moveToNext();
            }
        }

        setupToolbar();

        if (isTwoPaneLayoutUsed()) {
            twoPaneMode = true;
            LogUtil.logD("TEST","TWO POANE TASDFES");
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
            // Show the quote detail information by replacing the DetailFragment via transaction.
            StudyRoomDetailFragment fragment = StudyRoomDetailFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
        } else {
            // Start the detail activity in single pane mode.
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
        StudyRoomDetailFragment fragment =  StudyRoomDetailFragment.newInstance(FavoriteStudyRoomsContent.ITEMS.get(0).id);
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
