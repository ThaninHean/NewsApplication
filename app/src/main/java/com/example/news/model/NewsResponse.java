package com.example.news.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//Model class for the response from the News API
public class NewsResponse {

    private List<NewsArticle> articles;

    public List<NewsArticle> getArticles() {
        return articles;
    }
}

