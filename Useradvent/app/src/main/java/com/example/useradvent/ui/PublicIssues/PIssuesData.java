package com.example.useradvent.ui.PublicIssues;

public class PIssuesData {
    String date, detail, image, key,link, time, title;

    public PIssuesData() {
    }

    public PIssuesData(String date, String detail, String image, String key, String link, String time, String title) {
        this.date = date;
        this.detail = detail;
        this.image = image;
        this.key = key;
        this.link = link;
        this.time = time;
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
