package com.raven.model;

import java.awt.*;

public class ModelNoticeBoard {
    private String title;
    private String time;
    private String description;
    private Color titleColor;

    public ModelNoticeBoard(String title, String time, String description, Color titleColor) {
        this.title = title;
        this.time = time;
        this.description = description;
        this.titleColor = titleColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }



    // Constructor, getters, setters
}
