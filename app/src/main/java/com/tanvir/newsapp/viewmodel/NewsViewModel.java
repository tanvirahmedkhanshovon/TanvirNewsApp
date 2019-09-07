package com.tanvir.newsapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tanvir.newsapp.model.Article;
import com.tanvir.newsapp.model.NewsAppRepository;

import java.util.List;

public class NewsViewModel extends ViewModel {

    private NewsAppRepository newsAppRepository;

    public NewsViewModel(NewsAppRepository newsAppRepository) {
        this.newsAppRepository = newsAppRepository;
    }

    public LiveData<List<Article>> getNewsFromInternetAndSaveToDataBase(String country) {


        return newsAppRepository.getNewsApiMutableLiveData(country);
    }

    public LiveData<List<Article>> getNewsFromDataBaseInOffLine() {


        return newsAppRepository.getNewsFromDataBase();
    }

    public LiveData<Integer> getNewsCount() {


        return newsAppRepository.getNewsCount();
    }

    public LiveData<Boolean> hasInternetConnection() {


        return newsAppRepository.getInternetInfo();
    }

    public void deleteAll() {

        newsAppRepository.deleteAll();

    }

    public void clear() {

        newsAppRepository.clear();
    }
}
