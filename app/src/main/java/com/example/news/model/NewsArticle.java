package com.example.news.model;

//Create a model class to represent the structure of a news article.

//Model class for a news article
public class NewsArticle {
    private String title;

    private String description;

    private String publishedAt;

    private String url;

    private String urlToImage;

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }
}

