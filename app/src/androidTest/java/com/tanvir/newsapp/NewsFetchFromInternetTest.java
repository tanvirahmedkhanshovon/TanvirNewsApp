package com.tanvir.newsapp;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tanvir.newsapp.model.Article;
import com.tanvir.newsapp.model.NewsAppRepository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NewsFetchFromInternetTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    Application app =
            (Application) InstrumentationRegistry
                    .getTargetContext()
                    .getApplicationContext();

    @Test
    public void getNewsFromInterNet() throws Exception {

        NewsAppRepository newsAppRepository = new NewsAppRepository(app);

        newsAppRepository.getNewsApiMutableLiveData("us").observeForever(new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                assertEquals(38, articles.size());
            }
        });


    }
}
