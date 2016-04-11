package com.sw_ss16.lc_app.ui.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.ui.learning_center_list.ListActivity;

/**
 * Starts from the ListActivity, but opens the StudyRoomDetailActivity
 */
public class StatisticsTest extends ActivityInstrumentationTestCase2<ListActivity> {

    private Solo mySolo;

    public StatisticsTest() {
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

    public void testStudyRoomDetailStatisticsOutput() {
        mySolo.sleep(500);
        // If there are any items in the list
        // The second argument of searchText means it searches only for visible text (not hidden)
        if (!mySolo.searchText(getActivity().getString(R.string.no_fav), true)) {
            // Click on first list item
            mySolo.clickInList(1);

            // Look for text in study room detail activity
            mySolo.waitForActivity("StudyRoomDetailActivity");
            boolean text_found = mySolo.searchText(getActivity().getString(R.string.fullness_full)) ||
                    mySolo.searchText(getActivity().getString(R.string.fullness_halffull)) ||
                    mySolo.searchText(getActivity().getString(R.string.fullness_empty));
            assertEquals("Required text not found", true, text_found);
        }
    }
}