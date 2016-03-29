package com.sw_ss16.studyroompopulationpredicter.ui.quote;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import junit.framework.TestCase;

public class ListActivityTest extends ActivityInstrumentationTestCase2<ListActivity> {

    private Solo mySolo;

    public ListActivityTest(){
        super(ListActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mySolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

}