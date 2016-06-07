package com.sw_ss16.lc_app.ui.tests;

import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;

import com.robotium.solo.Solo;
import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.ui.learning_center_list.ListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
        if (!mySolo.searchText(getActivity().getString(R.string.no_fav), true)) {
            mySolo.clickInList(1);

            mySolo.waitForActivity("StudyRoomDetailActivity");

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            int fromY = height / 2 + height / 3;
            int toY = height / 2 - height / 3;
            mySolo.drag(0, 0, fromY, toY, 1);

            boolean text_found = mySolo.searchText(getActivity().getString(R.string.fullness_full)) ||
                    mySolo.searchText(getActivity().getString(R.string.fullness_halffull)) ||
                    mySolo.searchText(getActivity().getString(R.string.fullness_empty));
            assertEquals("Required text not found", true, text_found);
        }
    }

    public void testStudyRoomDetailStatisticsCurrentDate() {
        mySolo.sleep(500);
        if (!mySolo.searchText(getActivity().getString(R.string.no_fav), true)) {
            mySolo.clickInList(1);

            mySolo.waitForActivity("StudyRoomDetailActivity");
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            String display_day;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
            display_day = dayFormat.format(calendar.getTime());
            boolean text_found = mySolo.searchText("for " + display_day) &&
                    mySolo.searchText(Integer.toString(hour));
            assertEquals("Required text not found", true, text_found);
        }
    }
}
