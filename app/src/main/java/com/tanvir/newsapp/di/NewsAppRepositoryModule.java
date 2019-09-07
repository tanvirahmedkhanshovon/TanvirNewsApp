package com.tanvir.newsapp.di;

import android.app.Application;

import com.tanvir.newsapp.model.NewsAppRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NewsAppRepositoryModule {

    @Singleton
    @Provides
    NewsAppRepository providesRepository(Application application) {

        return new NewsAppRepository(application);
    }
}
