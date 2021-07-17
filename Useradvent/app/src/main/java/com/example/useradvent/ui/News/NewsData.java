package com.example.useradvent.ui.News;

public class NewsData {
    String image, title, date, detailNews;

    public NewsData() {
    }

    public NewsData(String image, String title, String date, String detailNews) {
        this.image = image;
        this.title = title;
        this.date = date;
        this.detailNews = detailNews;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetailNews() {
        return detailNews;
    }

    public void setDetailNews(String detailNews) {
        this.detailNews = detailNews;
    }
}
