package com.example.news.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(NewsItem newsItem);
    @Query("SELECT * FROM news_table")
    LiveData<List<NewsItem>> getAllNews();
    @Delete
    void delete(NewsItem newsItem);

}
