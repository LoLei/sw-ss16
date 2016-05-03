package com.sw_ss16.lc_app.ui.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.ui.learning_center_list.ListActivity;

public class ListActivityTest extends ActivityInstrumentationTestCase2<ListActivity> {

    private Solo mySolo;

    public ListActivityTest() {
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

    public void testNavigationDrawerOpen() {
        mySolo.sleep(500);
        mySolo.clickOnImageButton(0);
        mySolo.sleep(500);

        boolean text_found = mySolo.searchText(getActivity().getString(R.string.navigation_title));
        assertEquals("Required text not found", true, text_found);
    }

    public void testStudyRoomDetailOpen() {
        mySolo.sleep(500);
        if (!mySolo.searchText(getActivity().getString(R.string.no_fav), true)) {
            mySolo.clickInList(1);

            mySolo.waitForActivity("StudyRoomDetailActivity");
            boolean text_found = mySolo.searchText(getActivity().getString(R.string.lc_statistics_title));
            assertEquals("Required text not found", true, text_found);
        }
    }
}