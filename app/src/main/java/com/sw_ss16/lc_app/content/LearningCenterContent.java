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
     * An array of sample items. TODO copypasta
     */
    public static final List<LearningCenter> ITEMS = new ArrayList<>();

    /**
     * A map of sample items. Key: sample ID; Value: Item. TODO copypasta
     */
    public static final Map<String, LearningCenter> ITEM_MAP = new HashMap<>(5);


    // -------------------------------
    // Methods
    // -------------------------------

    public static final Map<String, LearningCenter> getMapOfAllLearningCenters(){

        return null;
    }

    public static final Map<String, LearningCenter> getMapOfAllFavoriteLearningCenters(){


        return null;
    }


    public static void addItem(LearningCenter item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class LearningCenter {
        public final String id;
        public final String imageInUrl;
        public final String imageOutUrl;
        public final String title;
        public final String lc_description;
        public final String lc_address;
        public boolean isFavLc;

        public LearningCenter(String id, String imageInUrl, String imageOutUrl, String title,
                              String lc_description, String lc_address, boolean isFavLc) {
            this.id = id;
            this.imageInUrl = imageInUrl;
            this.imageOutUrl = imageOutUrl;
            this.title = title;
            this.lc_description = lc_description;
            this.lc_address = lc_address;
            this.isFavLc = isFavLc;
        }
    }
}
