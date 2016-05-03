package com.sw_ss16.lc_app.ui.learning_center_one;

import android.os.Bundle;

import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.ui.base.BaseActivity;

public class StudyRoomDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        StudyRoomDetailFragment fragment = StudyRoomDetailFragment.newInstance(getIntent().getStringExtra(StudyRoomDetailFragment.ARG_ITEM_ID));
        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }

    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
}
