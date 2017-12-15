package com.example.kosta.maphack.model;

/**
 * Created by kosta on 2017-12-07.
 */

public class After {
    private String aft_image;
    private String aft_title;
    private String aft_id;

    public String getAft_id() {
        return aft_id;
    }

    public void setAft_id(String aft_id) {
        this.aft_id = aft_id;
    }

    public String getAft_image() {
        return aft_image;
    }

    public void setAft_image(String aft_image) {
        this.aft_image = aft_image;
    }

    public String getAft_title() {
        return aft_title;
    }

    public void setAft_title(String aft_title) {
        this.aft_title = aft_title;
    }

    public After(String aft_image, String aft_title,String aft_id) {
        this.aft_image = aft_image;
        this.aft_title = aft_title;
        this.aft_id = aft_id;
    }
}