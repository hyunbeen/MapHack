package com.example.kosta.maphack.model;

/**
 * Created by kosta on 2017-10-25.
 */

public class AlarmList {
    String title;
    String description;
    String image;

    public AlarmList(){


    }
    public AlarmList(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
