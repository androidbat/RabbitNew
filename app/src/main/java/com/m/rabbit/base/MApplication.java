package com.m.rabbit.base;

import android.content.Context;

import com.c.commen.base.CommenApp;
import com.c.commen.dialog.DialogHelper;
import com.m.rabbit.R;
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
        DialogHelper.init(R.layout.dialog_alert,0);
    }

}
