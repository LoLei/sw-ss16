package com.sw_ss16.lc_app.content;

/**
 * Created by mrb on 08/04/16.
 */

public class LearningCenter {

    // -------------------------------
    // Members
    // -------------------------------
    public final String id;
    public final String image_in_url;
    public final String image_out_url;
    public final String title;
    public final String description;
    public final String address;
    public boolean is_fav_lc;

    // -------------------------------
    // Methods
    // -------------------------------
    public LearningCenter(String id, String image_in_url, String image_out_url, String title,
                          String description, String address, boolean is_fav_lc) {
        this.id = id;
        this.image_in_url = image_in_url;
        this.image_out_url = image_out_url;
        this.title = title;
        this.description = description;
        this.address = address;
        this.is_fav_lc = is_fav_lc;
    }
}
