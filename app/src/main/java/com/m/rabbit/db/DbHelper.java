package com.m.rabbit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.m.rabbit.bean.DaoMaster;
import com.m.rabbit.bean.DaoSession;
import com.m.rabbit.bean.User;
import com.m.rabbit.bean.UserDao;

import java.util.List;

import de.greenrobot.dao.query.Query;

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


    public static void save(User note){
        daoSession.getUserDao().insert(note);
    }

    public static List<User> getAllNote(){
        Query query = daoSession.getUserDao().queryBuilder()
                .orderAsc(UserDao.Properties.Id)
                .build();
        return query.list();
    }

    public static UserDao getUserDao(){
        return daoSession.getUserDao();
    }

    public static User getUserById(int id){
        Query query = getUserDao().queryBuilder()
                .where(UserDao.Properties.Id.eq(id))
                .limit(1)
                .build();
        return (User) query.list().get(0);
    }

    public static void delUserByName(long id){
        getUserDao().deleteByKey(id);
    }

}
