package com.sw_ss16.lc_app.ui.studyroom;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.sw_ss16.lc_app.content.FavoriteStudyRoomsContent;
import com.sw_ss16.lc_app.content.StudyRoomsContent;
import com.sw_ss16.lc_app.ui.base.BaseActivity;
import com.sw_ss16.lc_app.ui.base.BaseFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Shows the quote detail page.
 *
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
    private StudyRoomsContent.DummyItem dummyItem;

    @Bind(R.id.quote)
    TextView quote;

    @Bind(R.id.author)
    TextView author;

    @Bind(R.id.backdrop)
    ImageView backdropImg;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // load dummy item by using the passed item ID.
            dummyItem = StudyRoomsContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflateAndBind(inflater, container, R.layout.fragment_article_detail);

        if (!((BaseActivity) getActivity()).providesActivityToolbar()) {
            // No Toolbar present. Set include_toolbar:
            ((BaseActivity) getActivity()).setToolbar((Toolbar) rootView.findViewById(R.id.toolbar));
        }

        if (dummyItem != null) {
            loadBackdrop();
            collapsingToolbar.setTitle(dummyItem.title);
            author.setText(dummyItem.content);
            quote.setText(dummyItem.author);
        }

        return rootView;
    }

    private void loadBackdrop() {
        // Glide.with(this).load(dummyItem.photoId).centerCrop().into(backdropImg);
        Glide.with(this).load(dummyItem.imageInUrl).centerCrop().into(backdropImg);
    }

    @OnClick(R.id.fav_fab_btn)
    public void onFabClicked(View view) {
        if(dummyItem.isFavLc)
        {
            dummyItem.isFavLc = false;
            StudyRoomsContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID)).isFavLc = false;
            FavoriteStudyRoomsContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID)).isFavLc = false;
        }
        else{
            dummyItem.isFavLc = true;
            StudyRoomsContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID)).isFavLc = true;
            FavoriteStudyRoomsContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID)).isFavLc = true;

        }

        //Snackbar.make(view, "Hello Snackbar!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

    public StudyRoomDetailFragment() {}
}
