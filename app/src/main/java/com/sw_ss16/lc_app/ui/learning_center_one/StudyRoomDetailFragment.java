package com.sw_ss16.lc_app.ui.learning_center_one;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.backend.Database;
import com.sw_ss16.lc_app.content.LearningCenter;
import com.sw_ss16.lc_app.content.LearningCenterContent;
import com.sw_ss16.lc_app.ui.base.BaseActivity;
import com.sw_ss16.lc_app.ui.base.BaseFragment;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Shows the description detail page.
 * <p/>
 * Created by Andreas Schrade on 14.12.2015.
 */
public class StudyRoomDetailFragment extends BaseFragment {

    /**
     * The argument represents the dummy item ID of this fragment.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy lc_address of this fragment.
     */
    private LearningCenter current_learning_center;

    private LearningCenterContent lc_contentmanager = new LearningCenterContent();

    @Bind(R.id.lc_description)
    TextView description;

    @Bind(R.id.lc_statistics)
    TextView statistics;

    @Bind(R.id.lc_address)
    TextView address;

    @Bind(R.id.backdrop)
    ImageView backdropImg;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // load dummy item by using the passed item ID.

            lc_contentmanager.setApplicationContext(getActivity().getApplicationContext());
            current_learning_center = lc_contentmanager.getLcObject(getArguments().getString(ARG_ITEM_ID));
            //current_learning_center = StudyRoomsContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }

        setHasOptionsMenu(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflateAndBind(inflater, container, R.layout.fragment_article_detail);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fav_fab_btn);

        if (lc_contentmanager.getLearningCenterFavoriteStatus(Integer.parseInt(current_learning_center.id))) {
            fab.setImageResource(R.drawable.ic_remove_white_24dp);
        }
        else {
            fab.setImageResource(R.drawable.ic_add_white_24dp);
        }


        if (!((BaseActivity) getActivity()).providesActivityToolbar()) {
            // No Toolbar present. Set include_toolbar:
            ((BaseActivity) getActivity()).setToolbar((Toolbar) rootView.findViewById(R.id.toolbar));
        }

        if (current_learning_center != null) {
            loadBackdrop();
            collapsingToolbar.setTitle(current_learning_center.name);
            address.setText(current_learning_center.address);
            description.setText(current_learning_center.description);

            Database db = new Database(getActivity().getApplicationContext());
            SQLiteDatabase sqldb = db.getReadableDatabase();

            Calendar calendar = Calendar.getInstance();
            // int current_hour = ;

            //TODO:Use WHERE-Query
            String query_string = "ID = " + current_learning_center.id;
            String[] columns = new String[]{"ID", "LC_ID", "WEEKDAY", "HOUR", "FULLNESS"};

            Cursor c = sqldb.query("statistics", columns, query_string, null, null, null, null);

            c.moveToFirst();

            for (int i = 1; i <= c.getCount(); i++) {

                boolean statistic_ok = true;

                if (current_learning_center.id.equals(c.getString(c.getColumnIndex("LC_ID")))) {

                    String fullness = c.getString(c.getColumnIndex("FULLNESS"));
                    int full = Integer.parseInt(fullness);

                    String fullness_description = "";

                    if (full >= 75)
                    {
                        fullness_description = getActivity().getString(R.string.fullness_full);
                    }
                    else if (full >= 50)
                    {
                        fullness_description = getActivity().getString(R.string.fullness_halffull);
                    }
                    else if (full < 50)
                    {
                        fullness_description = getActivity().getString(R.string.fullness_empty);
                    }
                    else
                    {
                        statistic_ok = false;
                    }

                    if(statistic_ok)
                        statistics.setText(fullness_description + "\nPercentage: " + fullness + "%");
                    else
                        statistics.setText(R.string.default_fullness_description);
                }

                c.moveToNext();

            }

            db.close();

        }

        return rootView;
    }

    private void loadBackdrop() {
        // Glide.with(this).load(current_learning_center.photoId).centerCrop().into(backdropImg);
        Glide.with(this).load(current_learning_center.image_in_url).centerCrop().into(backdropImg);
    }

    @OnClick(R.id.fav_fab_btn)
    public void onFabClicked(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fav_fab_btn);

        if (lc_contentmanager.getLearningCenterFavoriteStatus(Integer.parseInt(current_learning_center.id))) {
            lc_contentmanager.setLearningCenterFavoriteStatus(Integer.parseInt(current_learning_center.id), false);
            fab.setImageResource(R.drawable.ic_add_white_24dp);
            Snackbar.make(view, R.string.fav_delete, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }
        else {
            lc_contentmanager.setLearningCenterFavoriteStatus(Integer.parseInt(current_learning_center.id), true);
            fab.setImageResource(R.drawable.ic_remove_white_24dp);
            Snackbar.make(view, R.string.fav_add, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sample_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // your logic
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static StudyRoomDetailFragment newInstance(String itemID) {
        StudyRoomDetailFragment fragment = new StudyRoomDetailFragment();
        Bundle args = new Bundle();
        args.putString(StudyRoomDetailFragment.ARG_ITEM_ID, itemID);
        fragment.setArguments(args);
        return fragment;
    }
}
