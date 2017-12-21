package com.example.kosta.maphack.model;

/**
 * Created by kosta on 2017-12-07.
 */

public class Favor {
    private String favor_image;
    private String favor_title;
    private String favor_id;

    public Favor(String favor_image, String favor_title, String favor_id) {
        this.favor_image = favor_image;
        this.favor_title = favor_title;
        this.favor_id = favor_id;
    }

    public String getFavor_image() {
        return favor_image;
    }

    public void setFavor_image(String favor_image) {
        this.favor_image = favor_image;
    }

    public String getFavor_title() {
        return favor_title;
    }

    public void setFavor_title(String favor_title) {
        this.favor_title = favor_title;
    }

    public String getFavor_id() {
        return favor_id;
    }

    public void setFavor_id(String favor_id) {
        this.favor_id = favor_id;
    }
}