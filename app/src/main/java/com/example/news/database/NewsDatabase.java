package com.example.news.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NewsItem.class}, version = 1)
public abstract class NewsDatabase extends RoomDatabase {

    // Singleton instance of the database
    private static volatile NewsDatabase INSTANCE;

    // Abstract method to get DAO
    public abstract NewsDao newsDao();

    // Executor for background database operations
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    // Get the singleton instance of the database
    public static synchronized NewsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (NewsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    NewsDatabase.class, "news_database")
                            .fallbackToDestructiveMigration() // Allows rebuilding the database on schema changes
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
