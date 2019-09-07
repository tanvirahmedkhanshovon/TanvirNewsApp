package com.tanvir.newsapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tanvir.newsapp.model.Article;

@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "NewsApp.db";
    private static final Object lock = new Object();
    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {

            synchronized (lock) {

                if (instance == null) {

                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }

    public abstract ArticleDao articleDao();
}
