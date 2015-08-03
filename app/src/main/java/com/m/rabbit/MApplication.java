package com.m.rabbit;

import android.app.Application;
import android.content.Context;

import com.m.rabbit.db.DbHelper;
import com.squareup.leakcanary.LeakCanary;

import java.util.Date;

import de.greenrobot.daoexample.Note;

/**
 * Created by wg on 2015/7/27.
 */
public class MApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        context = this;
        DbHelper.save(new Note("tet","com",new Date(),12l,1,2));
    }
}
