package com.c.commen.base;

import android.app.Application;
import android.content.Context;


/**
 * Created by wg on 2015/7/27.
 */
public class CommenApp extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

}
