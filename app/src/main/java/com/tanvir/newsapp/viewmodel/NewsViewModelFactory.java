package com.tanvir.newsapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tanvir.newsapp.model.NewsAppRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NewsViewModelFactory implements ViewModelProvider.Factory {
    private final NewsAppRepository newsAppRepository;

    @Inject
    public NewsViewModelFactory(NewsAppRepository newsAppRepository) {
        this.newsAppRepository = newsAppRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NewsViewModel(newsAppRepository);
    }
}
