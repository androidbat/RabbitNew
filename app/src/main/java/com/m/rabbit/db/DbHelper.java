package com.m.rabbit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.m.rabbit.bean.DaoMaster;
import com.m.rabbit.bean.DaoSession;

/**
 * Created by wg on 2015/8/3.
 */
public class DbHelper {

    public static DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static SQLiteOpenHelper helper;

    public static void init(Context context,String name){
        helper = new DaoMaster.OpenHelper(context, name, null) {
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//                ArrayList<String> mupdates = new ArrayList<String>();
//                mupdates.add("ALTER TABLE NOTE ADD COLUMN BB INTEGER");
//                for (int i=0;i<mupdates.size();i++){
//                    try {
//                        db.execSQL(mupdates.get(i));
//                    } catch (Exception e) {
//                    }
//                }
            }
        };
        daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

}
