package com.m.rabbit;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by wg on 2015/7/27.
 */
public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
