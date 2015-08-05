package com.m.rabbit.db;

import com.m.rabbit.bean.User;
import com.m.rabbit.bean.UserDao;

import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * Created by wg on 2015/8/5.
 */
public class UserDaoManager {

    private static UserDao getUserDao(){
        return DbHelper.daoSession.getUserDao();
    }

    public static void save(User note){
        getUserDao().insert(note);
    }

    public static List<User> getAllNote(){
        Query query = getUserDao().queryBuilder()
                .orderAsc(UserDao.Properties.Id)
                .build();
        return query.list();
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
