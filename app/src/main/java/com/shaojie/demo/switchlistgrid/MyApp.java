package com.shaojie.demo.switchlistgrid;


import android.app.Application;

public class MyApp extends Application {

    private static MyApp appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }

    public static MyApp getInstance() {
        return appInstance;
    }

}
