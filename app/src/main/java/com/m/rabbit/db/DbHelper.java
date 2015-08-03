package com.m.rabbit.db;

import android.database.sqlite.SQLiteOpenHelper;

import com.m.rabbit.MApplication;

import de.greenrobot.daoexample.DaoMaster;
import de.greenrobot.daoexample.DaoSession;
import de.greenrobot.daoexample.Note;
import de.greenrobot.daoexample.NoteDao;

/**
 * Created by wg on 2015/8/3.
 */
public class DbHelper {

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    /**
     * 取得DaoMaster
     * @return
     */
    public static DaoMaster getDaoMaster()
    {
        if (daoMaster == null)
        {
            SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(MApplication.context,"mm.db", null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }
    /**
     * 取得DaoSession
     *
     */
    public static DaoSession getDaoSession()
    {
        if (daoSession == null)
        {
            if (daoMaster == null)
            {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


    public static void save(Note note){
        NoteDao noteDao = getDaoSession().getNoteDao();
        noteDao.insert(note);
    }

    public static void getAllNote(){
    }


}
