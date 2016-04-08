package com.sw_ss16.studyroompopulationpredicter.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Just dummy content. Nothing special.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class FavoriteStudyRoomsContent {

    /**
     * An array of sample items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample items. Key: sample ID; Value: Item.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<>(5);


    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class DummyItem {
        public final String id;
        public final String imageInUrl;
        public final String imageOutUrl;
        public final String title;
        // TODO: Rename member variables
        public final String author;
        public final String content;
        public final boolean isFavLc;

        public DummyItem(String id, String imageInUrl, String imageOutUrl, String title,
                         String author, String content, boolean isFavLc) {
            this.id = id;
            this.imageInUrl = imageInUrl;
            this.imageOutUrl = imageOutUrl;
            this.title = title;
            this.author = author;
            this.content = content;
            this.isFavLc = isFavLc;
        }
    }
}
