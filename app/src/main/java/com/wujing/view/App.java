package com.wujing.view;

import android.app.Application;

import java.time.Instant;

public class App extends Application {

    public static void setInstant(App instant) {
        mInstant = instant;
    }

    public static App getInstant() {
        return mInstant;
    }

    private static App mInstant;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstant =this;
    }
}
