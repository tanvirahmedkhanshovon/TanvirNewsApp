package com.tanvir.newsapp;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tanvir.newsapp.database.AppDatabase;
import com.tanvir.newsapp.database.ArticleDao;
import com.tanvir.newsapp.model.Article;
import com.tanvir.newsapp.model.NewsAppRepository;
import com.tanvir.newsapp.model.Source;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NewsEntryReadWriteTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    Application app =
            (Application) InstrumentationRegistry
                    .getTargetContext()
                    .getApplicationContext();
    private ArticleDao articleDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        articleDao = db.articleDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeNewsAndReadFromRepositoryTest() throws Exception {
        NewsAppRepository newsAppRepository = new NewsAppRepository(app);

        final Article article = new Article();
        article.setTitle("Bangladesh has won the world cup!!!");
        Source source = new Source();
        source.setId("test");
        source.setName("Test");
        article.setSource(source);
        newsAppRepository.inserNewsArticleDao(article);
        newsAppRepository.getNewsFromDataBase().observeForever(new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                assertEquals(article.getTitle(), articles.get(articles.size() - 1).getTitle());
            }
        });
    }

    @Test
    public void writeNewsAndReadInList() throws Exception {
        Article article = new Article();
        article.setTitle("Bangladesh has won the world cup!!!");
        Source source = new Source();
        source.setId("test");
        source.setName("Test");
        article.setSource(source);
        articleDao.insertNews(article);
        List<Article> articleList = new ArrayList<>();
        articleList.add(article);
        assertEquals(articleList.size(), articleDao.getCountForTest());
    }

    @Test
    public void deleteAllDataFromDataBase() throws Exception {
        Article article = new Article();
        article.setTitle("Bangladesh has won the world cup!!!");
        Source source = new Source();
        source.setId("test");
        source.setName("Test");
        article.setSource(source);
        articleDao.insertNews(article);
        articleDao.insertNews(article);
        articleDao.insertNews(article);
        articleDao.insertNews(article);
        articleDao.deleteAll();
        assertEquals(0, articleDao.getCountForTest());


    }
}

