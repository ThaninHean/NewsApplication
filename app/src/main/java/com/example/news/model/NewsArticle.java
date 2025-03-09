package com.example.news.model;

//Create a model class to represent the structure of a news article.

import com.google.gson.annotations.SerializedName;


//Model class for a news article
public class NewsArticle {
    // Serialized name for the JSON field
    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("publishedAt")
    private String publishedAt;

    @SerializedName("url")
    private String url;

    @SerializedName("urlToImage")
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

