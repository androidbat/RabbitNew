package com.m.rabbit.base;

import android.content.Context;

import com.c.commen.base.CommenApp;
import com.m.rabbit.db.DbHelper;
import com.squareup.leakcanary.LeakCanary;


/**
 * Created by wg on 2015/7/27.
 */
public class MApplication extends CommenApp {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        LeakCanary.install(this);
        DbHelper.init(this, "mm.db");
    }

}
