package com.sw_ss16.lc_app.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gets and retrieves Data from Database easily
 *
 */
public class LearningCenterContent {


    // -------------------------------
    // Members
    // -------------------------------
    /**
     * Current Map of all
     */
    public static final List<LearningCenter> ITEMS = new ArrayList<>();

    /**
     * A map of sample items. Key: sample ID; Value: Item. TODO copypasta
     */
    public static final Map<String, LearningCenter> ITEM_MAP = new HashMap<>(5);


    // -------------------------------
    // Methods
    // -------------------------------
    public Map<String, LearningCenter> getMapOfAllLearningCenters(){

        return null;
    }

    public boolean setLearningCeterToFavorite(int lc_id){

        return false;

    }

    public boolean setLearningCeterToNoFavorite(int lc_id){

        return false;
    }

    public Map<String, LearningCenter> getMapOfAllFavoriteLearningCenters(){


        return null;
    }

    public static void addItem(LearningCenter item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
