package com.sw_ss16.lc_app.ui.tests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestSuite;

public class AllTests extends ActivityInstrumentationTestCase2<Activity> {

    public AllTests(Class<Activity> activityClass) {
        super(activityClass);
    }

    public static TestSuite suite() {
        TestSuite t = new TestSuite();
        t.addTestSuite(ListActivityTest.class);
        t.addTestSuite(SettingsActivityTest.class);
        t.addTestSuite(StudyRoomDetailTest.class);
        t.addTestSuite(StatisticsTest.class);

        return t;
    }

    @Override
    public void setUp() throws Exception {
    }

    @Override
    public void tearDown() throws Exception {
    }
}
