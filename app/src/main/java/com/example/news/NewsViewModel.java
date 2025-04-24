package com.example.news;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.news.database.NewsItem;
import com.example.news.database.NewsRepository;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    private final NewsRepository repository;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        repository = new NewsRepository(application);
    }

    // Insert a news item into the database
    public void insert(NewsItem newsItem) {
        repository.insert(newsItem);
    }

    // Delete a news item from the database
    public void deleteNews(NewsItem newsItem) {
        repository.delete(newsItem);
    }

    // Get all saved news from the database
    public LiveData<List<NewsItem>> getSavedNews() {
        return repository.getAllNews();
    }
}
