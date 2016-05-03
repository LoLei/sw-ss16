package com.sw_ss16.lc_app.ui.tests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestSuite;

public class AllTests extends ActivityInstrumentationTestCase2<Activity> {

    public AllTests(Class<Activity> activityClass) {
        super(activityClass);
    }

    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTestSuite(ListActivityTest.class);
        testSuite.addTestSuite(SettingsActivityTest.class);
        testSuite.addTestSuite(StudyRoomDetailTest.class);
        testSuite.addTestSuite(StatisticsTest.class);

        return testSuite;
    }

    @Override
    public void setUp() throws Exception {
    }

    @Override
    public void tearDown() throws Exception {
    }
}
