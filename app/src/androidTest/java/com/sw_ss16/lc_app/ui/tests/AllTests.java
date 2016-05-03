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
        TestSuite testSuite = new TestSuite();
        testSuite.addTestSuite(ListActivityTest.class);
        testSuite.addTestSuite(SettingsActivityTest.class);
        testSuite.addTestSuite(StudyRoomDetailTest.class);
        testSuite.addTestSuite(StatisticsTest.class);

        return testSuite;
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
