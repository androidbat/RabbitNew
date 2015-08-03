package com.m.rabbit;

import android.app.Application;
import android.content.Context;

import com.m.rabbit.bean.User;
import com.m.rabbit.db.DbHelper;
import com.squareup.leakcanary.LeakCanary;


/**
 * Created by wg on 2015/7/27.
 */
public class MApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        LeakCanary.install(this);
        DbHelper.init(this, "mm.db");
        DbHelper.save(new User());
    }
}
