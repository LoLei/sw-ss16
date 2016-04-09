package com.sw_ss16.lc_app.ui.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
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
        super.tearDown();
    }

    public void testNavigationDrawerOpen() {
        mySolo.sleep(1000);
        mySolo.clickOnImageButton(0);
        mySolo.sleep(1000);

        boolean actual = mySolo.searchText("Predictr Menu");
        assertEquals("Required text not found", true, actual);
    }

    public void testStudyRoomDetailOpen() {
        mySolo.sleep(1000);
        // Click on first list item
        mySolo.clickInList(1);

        // Look for text in study room detail activity
        mySolo.waitForActivity("StudyRoomDetailActivity");
        boolean actual = mySolo.searchText("More Info");
        assertEquals("Required text not found", true, actual);
    }
}