package com.sw_ss16.studyroompopulationpredicter.ui.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.sw_ss16.studyroompopulationpredicter.ui.studyroom.ListActivity;

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
        mySolo.sleep(3000);
        mySolo.clickOnImageButton(0);

        // Other way to target images
        // mySolo.clickOnButton(R.drawable.ic_menu);
        mySolo.sleep(3000);
    }
}