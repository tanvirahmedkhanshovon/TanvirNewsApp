package com.tanvir.newsapp.model;

import android.app.Application;

import com.tanvir.newsapp.di.AppModule;
import com.tanvir.newsapp.di.AppServiceComponent;
import com.tanvir.newsapp.di.DaggerAppServiceComponent;

public class NewsApp extends Application {

    private static NewsApp app;
    private AppServiceComponent appServiceComponent;

    public static NewsApp getApp() {
        return app;
    }

    public AppServiceComponent getAppServiceComponent() {
        return appServiceComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appServiceComponent = DaggerAppServiceComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }
}
