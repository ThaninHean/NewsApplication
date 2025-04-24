package com.example.news.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.news.database.NewsDao;
import com.example.news.database.NewsItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NewsItem.class}, version = 1)
public abstract class NewsDatabase extends RoomDatabase {

    private static NewsDatabase INSTANCE;

    public abstract NewsDao newsDao();
    // Executor for background DB operations
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);


    public static synchronized NewsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewsDatabase.class, "news_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}