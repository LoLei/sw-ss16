package com.sw_ss16.lc_app.content;

/**
 * Created by mrb on 08/04/16.
 */

public class LearningCenter {

    public final String id;
    public final String name;
    public final String description;
    public final String address;
    public final String image_in_url;
    public final String image_out_url;
    public final String capacity;


    public LearningCenter(String id, String name, String description, String address, String image_in_url,
                          String image_out_url, String capacity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.image_in_url = image_in_url;
        this.image_out_url = image_out_url;
        this.capacity = capacity;
    }
}
