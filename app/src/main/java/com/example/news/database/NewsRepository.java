package com.example.news.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsRepository {
    private final NewsDao newsDao;
    private final ExecutorService executorService;

    // Constructor to initialize the DAO and executor service
    public NewsRepository(Application application) {
        NewsDatabase db = NewsDatabase.getInstance(application);
        newsDao = db.newsDao();
        executorService = Executors.newSingleThreadExecutor(); // Single thread executor for database operations
    }

    // Insert a news item into the database
    public void insert(NewsItem newsItem) {
        executorService.execute(() -> newsDao.insertNews(newsItem));
    }

    // Get all news from the database (LiveData for observing changes)
    public LiveData<List<NewsItem>> getAllNews() {
        return newsDao.getAllNews();
    }

    // Delete a news item from the database
    public void delete(NewsItem newsItem) {
        NewsDatabase.databaseWriteExecutor.execute(() -> newsDao.delete(newsItem));
    }
}
