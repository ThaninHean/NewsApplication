package com.example.news.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

public class NewsRepository {
    private final NewsDao newsDao;

    public NewsRepository(Application application) {
        NewsDatabase db = NewsDatabase.getInstance(application);
        newsDao = db.newsDao();
    }

    public void insert(NewsItem newsItem) {
        Executors.newSingleThreadExecutor().execute(() -> newsDao.insertNews(newsItem));
    }


    public LiveData<List<NewsItem>> getAllNews() {
        return newsDao.getAllNews();
    }

    public void delete(NewsItem newsItem) {
        NewsDatabase.databaseWriteExecutor.execute(() -> {
            newsDao.delete(newsItem); // DAO handles actual DB delete
        });
    }
}

