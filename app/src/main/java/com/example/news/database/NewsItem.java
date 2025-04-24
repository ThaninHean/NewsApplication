package com.example.news.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news_table")
public class NewsItem {

    @PrimaryKey
    @NonNull
    private String url; // unique identifier
    private String title;
    private String description;
    private String imageUrl;
    private String publishedAt;

    public NewsItem(@NonNull String url, String title, String description, String imageUrl, String publishedAt) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.publishedAt = publishedAt;

    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
