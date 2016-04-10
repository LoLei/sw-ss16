package com.sw_ss16.lc_app.ui.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.ui.learning_center_list.ListActivity;

/**
 * Starts from the ListActivity, but opens the SettingsActivity
 */
public class SettingsActivityTest extends ActivityInstrumentationTestCase2<ListActivity> {

    private Solo mySolo;

    public SettingsActivityTest() {
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

    public void testSettingsOpen() {
        mySolo.sleep(500);
        mySolo.clickOnImageButton(0);
        mySolo.sleep(500);
        mySolo.clickOnText(getActivity().getString(R.string.settings));

        mySolo.waitForActivity("SettingsActivity");
        boolean text_found = mySolo.searchText(getActivity().getString(R.string.pref_settings_auto_update_title));
        assertEquals("Required text not found", true, text_found);
    }
}