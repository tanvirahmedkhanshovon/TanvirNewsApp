package com.tanvir.newsapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tanvir.newsapp.model.Article;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(Article article);

    @Query("SELECT COUNT(*) FROM newsarticle")
    Flowable<Integer> getCount();

    //****** only used in testing *****//
    @Query("SELECT COUNT(*) FROM newsarticle")
    int getCountForTest();

    @Query("SELECT * FROM newsarticle ORDER BY  id ASC")
    Flowable<List<Article>> getAllNewsArticle();

    @Query("DELETE FROM newsarticle")
    void deleteAll();
}