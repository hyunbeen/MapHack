package com.example.kosta.maphack.model;

/**
 * Created by kosta on 2017-10-25.
 */

public class DetailAlarmList {
    String title;
    String description;
    String description1;
    String image;
    boolean check;

    public DetailAlarmList(){


    }
    public DetailAlarmList(String title, String description, String description1, String image, boolean check) {
        this.title = title;
        this.description = description;
        this.description1 = description1;
        this.image = image;
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
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
