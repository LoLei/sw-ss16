package com.sw_ss16.lc_app.ui.tests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestSuite;

public class AllTests extends ActivityInstrumentationTestCase2<Activity> {

    public AllTests(Class<Activity> activityClass) {
        super(activityClass);
    }

    /**
     * Add test classes in order of execution here
     */
    public static TestSuite suite() {
        TestSuite t = new TestSuite();
        t.addTestSuite(ListActivityTest.class);
        t.addTestSuite(SettingsActivityTest.class);
        t.addTestSuite(StudyRoomDetailTest.class);
        t.addTestSuite(StatisticsTest.class);

        return t;
    }

    /**
     * Individual test classes call these methods
     */
    @Override
    public void setUp() throws Exception {
    }

    /**
     * Individual test classes call these methods
     */
    @Override
    public void tearDown() throws Exception {
    }
}
