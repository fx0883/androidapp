package com.ChildHealthDiet.app2.model;

import com.ChildHealthDiet.app2.RecipeApplication;
import com.ChildHealthDiet.app2.model.bean.DaoMaster;
import com.ChildHealthDiet.app2.model.bean.DaoSession;

import org.greenrobot.greendao.database.Database;

public class DaoDbHelper {
    private static final String DB_NAME = "babydietfood.db";

    private static volatile DaoDbHelper sInstance;
    private Database mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DaoDbHelper(){
        //封装数据库的创建、更新、删除
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(RecipeApplication.getContext(), DB_NAME);
        Database mDb = helper.getEncryptedWritableDb("key123");
        mDaoSession = new DaoMaster(mDb).newSession();
    }

    public static DaoDbHelper getInstance(){
        if (sInstance == null){
            synchronized (DaoDbHelper.class){
                if (sInstance == null){
                    sInstance = new DaoDbHelper();
                }
            }
        }
        return sInstance;
    }

    public DaoSession getSession(){
        return mDaoSession;
    }

    public Database getDatabase(){
        return mDb;
    }


}
