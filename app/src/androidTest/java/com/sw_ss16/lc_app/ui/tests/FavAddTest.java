package com.sw_ss16.lc_app.ui.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.content.LearningCenter;
import com.sw_ss16.lc_app.content.LearningCenterDefroster;
import com.sw_ss16.lc_app.ui.learning_center_list.ListActivity;

import java.util.List;
import java.util.Random;


/**
 * Tests that start from the ListActivity
 */
public class FavAddTest extends ActivityInstrumentationTestCase2<ListActivity> {

    private Solo mySolo;

    public FavAddTest() {
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

    public void testFavAdd() {
        mySolo.clickOnImageButton(0);

        LearningCenterDefroster lc_contentmanager = new LearningCenterDefroster();
        lc_contentmanager.setApplicationContext(getActivity().getApplicationContext());
        List<String> listOfLcIds = lc_contentmanager.getListOfLcIds();

        Random rand = new Random();
        int randNumb = rand.nextInt(3 - 1) + 1;

        LearningCenter LC = lc_contentmanager.getLcObject(listOfLcIds.get(randNumb));

        System.out.println("Click on: " + LC.name);

        mySolo.sleep(500);
        //mySolo.clickOnMenuItem(LC.name.toString());
        //mySolo.clickOnButton(LC.name.toString());
        mySolo.clickOnText(LC.name.toString());

        mySolo.sleep(500);



        boolean already_fav = mySolo.getCurrentActivity().getResources().getDrawable(R.drawable.ic_add_white_24dp).isVisible();


        //assertEquals("Required image not found", true, al);

        System.out.println(already_fav);

        if(already_fav) {
            mySolo.clickOnView(mySolo.getView(R.id.fav_fab_btn));
        }
        mySolo.sleep(1000);

        mySolo.clickOnImageButton(0);


        mySolo.sleep(1000);

        boolean text_found = mySolo.searchText(LC.name.toString());

        assertEquals("Favorite not Found", true, text_found);


    }

}


