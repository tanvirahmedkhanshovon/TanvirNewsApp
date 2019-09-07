package com.tanvir.newsapp.model;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tanvir.newsapp.R;
import com.tanvir.newsapp.database.AppDatabase;
import com.tanvir.newsapp.database.ArticleDao;
import com.tanvir.newsapp.service.NewsDataService;
import com.tanvir.newsapp.service.RetrofitInstance;
import com.tanvir.newsapp.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class NewsAppRepository {


    private Application application;
    private NewsDataService service;
    private ArrayList<Article> articleArrayList;
    private Observable<NewsApiModel> newsApiModelObservable;
    private MutableLiveData<List<Article>> newsApiMutableLiveData;
    private ArticleDao articleDao;
    private MutableLiveData<Integer> newsCountMutableLiveData;
    private MutableLiveData<Boolean> internetState;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public NewsAppRepository(Application application) {
        this.application = application;
        service = RetrofitInstance.getService();
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        articleDao = appDatabase.articleDao();

    }

    public MutableLiveData<List<Article>> getNewsApiMutableLiveData(String country) {

        articleArrayList = new ArrayList<>();
        newsApiMutableLiveData = new MutableLiveData<>();
        newsApiModelObservable = service.getNewsData(country, application.getResources().getString(R.string.api_key));
        compositeDisposable.add(newsApiModelObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<NewsApiModel, Observable<Article>>() {
                    @Override
                    public Observable<Article> apply(NewsApiModel newsApiModel) throws Exception {
                        return Observable.fromArray(newsApiModel.getArticles().toArray(new Article[0]));
                    }
                })
                .subscribeWith(new DisposableObserver<Article>() {
                    @Override
                    public void onNext(Article article) {
                        if (article.getCode() == null) {


                            articleArrayList.add(article);
                            inserNewsArticleDao(article);

                        } else {

                            articleArrayList.add(article);
                            onComplete();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Article article = new Article();
                        if (e.getLocalizedMessage().contains("Failed") || e.getLocalizedMessage().contains("path not found") || e.getLocalizedMessage().contains("No address associated with hostname")) {

                            try {
                                if (e.getLocalizedMessage().contains("Failed") || e.getLocalizedMessage().contains("path not found") || e.getLocalizedMessage().contains("No address associated with hostname")) {
                                    article.setCode("error");
                                    article.setMessage(e.getLocalizedMessage());

                                }
                            } catch (Exception s) {

                                article.setCode("error");
                                article.setMessage(e.getLocalizedMessage());

                            } finally {
                                articleArrayList.add(article);
                                newsApiMutableLiveData.postValue(articleArrayList);
                            }

                        }

                    }

                    @Override
                    public void onComplete() {

                        newsApiMutableLiveData.postValue(articleArrayList);
                    }
                }));


        return newsApiMutableLiveData;
    }

    public void inserNewsArticleDao(Article newsArticle) {


        Observable<Article> articleObservable = Observable.just(newsArticle);
        compositeDisposable.add(articleObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Article>() {
                    @Override
                    public void onNext(final Article article) {


                        Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                articleDao.insertNews(article);
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableCompletableObserver() {
                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {


                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {


                    }
                }));


    }


    public MutableLiveData<List<Article>> getNewsFromDataBase() {

        newsApiMutableLiveData = new MutableLiveData<>();
        compositeDisposable.add(articleDao.getAllNewsArticle()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(List<Article> merchantCategoryList) throws Exception {

                        newsApiMutableLiveData.postValue(merchantCategoryList);


                    }
                }));


        return newsApiMutableLiveData;

    }

    public MutableLiveData<Integer> getNewsCount() {

        newsCountMutableLiveData = new MutableLiveData<>();
        compositeDisposable.add(articleDao.getCount()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                               @Override
                               public void accept(Integer integer) throws Exception {
                                   newsCountMutableLiveData.postValue(integer);
                               }
                           }
                ));

        return newsCountMutableLiveData;
    }

    public void deleteAll() {

        compositeDisposable.add(Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                articleDao.deleteAll();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));


    }


    public LiveData<Boolean> getInternetInfo() {

        internetState = new MutableLiveData<>();

        if (NetworkUtil.isConnected(application)) {
            internetState.postValue(true);

        } else {


            internetState.postValue(false);
        }


        return internetState;
    }

    public void clear() {

        compositeDisposable.clear();
    }
}
