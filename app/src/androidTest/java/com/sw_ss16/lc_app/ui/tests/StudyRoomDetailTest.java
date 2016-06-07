package com.sw_ss16.lc_app.ui.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.ui.learning_center_list.ListActivity;

public class StudyRoomDetailTest extends ActivityInstrumentationTestCase2<ListActivity> {

    private Solo mySolo;

    public StudyRoomDetailTest() {
        super(ListActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mySolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {
        mySolo.finishOpenedActivities();
        super.tearDown();
    }

    public void testStudyRoomDetailFavorite() {
        mySolo.sleep(500);
        if (!mySolo.searchText(getActivity().getString(R.string.no_fav), true)) {
            mySolo.clickInList(1);

            mySolo.waitForActivity("StudyRoomDetailActivity");
            boolean text_found = mySolo.searchText("Statistics");
            assertEquals("Required text not found", true, text_found);

            boolean already_fav = mySolo.getCurrentActivity().getResources().getDrawable(R.drawable.ic_remove_white_24dp).isVisible();
            mySolo.clickOnView(mySolo.getView(R.id.fav_fab_btn));
            boolean image_shown = false;
            if (already_fav) {
                image_shown = mySolo.getCurrentActivity().getResources().getDrawable(R.drawable.ic_add_white_24dp).isVisible();
            }
            else {
                image_shown = mySolo.getCurrentActivity().getResources().getDrawable(R.drawable.ic_remove_white_24dp).isVisible();
            }

            assertEquals("Required image not found", true, image_shown);
        }
    }
}
