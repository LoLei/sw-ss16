package com.sw_ss16.studyroompopulationpredicter.ui.studyroom;

import android.os.Bundle;

import com.sw_ss16.studyroompopulationpredicter.R;
import com.sw_ss16.studyroompopulationpredicter.ui.base.BaseActivity;

/**
 * Simple wrapper for {@link StudyRoomDetailFragment}
 * This wrapper is only used in single pan mode (= on smartphones)
 * Created by Andreas Schrade on 14.12.2015.
 */
public class StudyRoomDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        StudyRoomDetailFragment fragment =  StudyRoomDetailFragment.newInstance(getIntent().getStringExtra(StudyRoomDetailFragment.ARG_ITEM_ID));
        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }

    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
}
