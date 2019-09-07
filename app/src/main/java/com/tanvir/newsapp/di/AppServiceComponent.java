package com.tanvir.newsapp.di;

import com.tanvir.newsapp.view.NewsListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NewsAppRepositoryModule.class})
public interface AppServiceComponent {

    void inject(NewsListFragment newsListFragment);
}
