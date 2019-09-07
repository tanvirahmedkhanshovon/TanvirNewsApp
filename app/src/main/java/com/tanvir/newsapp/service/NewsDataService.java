package com.tanvir.newsapp.service;

import com.tanvir.newsapp.model.NewsApiModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsDataService {

    @GET("v2/top-headlines")
    Observable<NewsApiModel> getNewsData(@Query("country") String country, @Query("apiKey") String apiKey);

}