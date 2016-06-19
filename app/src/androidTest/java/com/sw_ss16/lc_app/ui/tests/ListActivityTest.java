package com.sw_ss16.lc_app.ui.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.ui.learning_center_list.ListActivity;

/**
 * Tests that start from the ListActivity
 */
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

        boolean text_found = mySolo.searchText(getActivity().getString(R.string.navigation_favorite_study_rooms));
        assertEquals("Required text 1 not found", true, text_found);
    }

    public void testStudyRoomDetailOpen() {
        mySolo.sleep(500);
        // If there are any items in the list
        // The second argument of searchText means it searches only for visible text (not hidden)
        if (!mySolo.searchText(getActivity().getString(R.string.no_fav), true)) {
            // Click on first list item
            mySolo.clickInList(1);

            // Look for text in study room detail activity
            mySolo.waitForActivity("StudyRoomDetailActivity");

            TextView view = (TextView) mySolo.getView(R.id.lc_statistics_title);

            System.out.println(view.getText());

            boolean text_found = mySolo.searchText(view.getText().toString());
            //System.out.println(getActivity().getString(R.string.lc_statistics_title));
            assertEquals("Required text 2 not found", true, text_found);
        }
    }
}