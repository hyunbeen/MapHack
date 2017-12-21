package com.example.kosta.maphack.model;

/**
 * Created by kosta on 2017-12-07.
 */

public class Travel {
    private String travel_image;
    private String travel_title;
    private String travel_id;

    public Travel(String travel_image, String travel_title, String travel_id) {
        this.travel_image = travel_image;
        this.travel_title = travel_title;
        this.travel_id = travel_id;
    }

    public String getTravel_image() {
        return travel_image;
    }

    public void setTravel_image(String travel_image) {
        this.travel_image = travel_image;
    }

    public String getTravel_title() {
        return travel_title;
    }

    public void setTravel_title(String travel_title) {
        this.travel_title = travel_title;
    }

    public String getTravel_id() {
        return travel_id;
    }

    public void setTravel_id(String travel_id) {
        this.travel_id = travel_id;
    }
}