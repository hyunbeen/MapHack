package com.example.kosta.maphack.model;

/**
 * Created by kosta on 2017-12-07.
 */

public class After {
    private String aft_num;
    private String aft_title;


    private String aft_image;
    private String aft_content;
    private String aft_html;
    private String aft_date;
    private AfterLike aft_like[];
    private AfterReview aft_review[];
    private String aft_mid;

    public String getAft_num() {
        return aft_num;
    }

    public void setAft_num(String aft_num) {
        this.aft_num = aft_num;
    }

    public String getAft_title() {
        return aft_title;
    }

    public void setAft_title(String aft_title) {
        this.aft_title = aft_title;
    }

    public String getAft_content() {
        return aft_content;
    }

    public void setAft_content(String aft_content) {
        this.aft_content = aft_content;
    }

    public String getAft_html() {
        return aft_html;
    }

    public void setAft_html(String aft_html) {
        this.aft_html = aft_html;
    }

    public String getAft_date() {
        return aft_date;
    }

    public void setAft_date(String aft_date) {
        this.aft_date = aft_date;
    }

    public AfterLike[] getAft_like() {
        return aft_like;
    }

    public void setAft_like(AfterLike[] aft_like) {
        this.aft_like = aft_like;
    }

    public AfterReview[] getAft_review() {
        return aft_review;
    }

    public void setAft_review(AfterReview[] aft_review) {
        this.aft_review = aft_review;
    }

    public String getAft_mid() {
        return aft_mid;
    }

    public void setAft_mid(String aft_mid) {
        this.aft_mid = aft_mid;
    }

    public String getAft_image() {
        return aft_image;
    }

    public void setAft_image(String aft_image) {
        this.aft_image = aft_image;
    }

}
