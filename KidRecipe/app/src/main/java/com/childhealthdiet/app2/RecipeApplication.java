package com.ChildHealthDiet.app2;


import android.content.Context;
import android.util.Log;

import com.ChildHealthDiet.app2.model.bean.DaoMaster;
import com.ChildHealthDiet.app2.model.bean.DaoSession;
import com.ChildHealthDiet.app2.utils.FileUtils;


import org.greenrobot.greendao.database.Database;

//import android.database.sqlite.SQLiteDatabase;
//即将
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//改为


public class RecipeApplication extends android.app.Application {

    public DaoSession daoSession;
    private static RecipeApplication instance;

    public Boolean isCollectMode = false;

//    private final CompositeDisposable disposables = new CompositeDisposable();
    @Override
    public void onCreate() {
        super.onCreate();

        //复制assets目录下的数据库文件到应用数据库中
        try {
//            copyDataBase("zone.db");
            FileUtils.copyDataBase("db/babydietfood.db",this);
        } catch (Exception e) {
            Log.e("Application", e.getMessage());
        }


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "babydietfood.db");
        Database db = helper.getEncryptedWritableDb("key123");
        daoSession = new DaoMaster(db).newSession();


//        SQLiteDatabase.loadLibs(this);
//        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "my_database.db", null, 1);
//        SQLiteDatabase db = dbHelper.getWritableDatabase("530117");

//        daoSession = new DaoMaster(db).newSession();

//        SQLiteDatabase.loadLibs(this); //first init the db libraries with the context
//        SQLiteOpenHelper.getWritableDatabase("thisismysecret");

//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "babydietfood.db", null);
//        Database db = helper.getEncryptedWritableDb("530117");
//
////
////        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "babydietfood.db", null, 1);
////        SQLiteDatabase db = dbHelper.getWritableDatabase("530117");
//
////        SQLiteDatabase db = dbHelper.getWritableDatabase("530117");
//
//        daoSession = new DaoMaster(db).newSession();
////
////
////
//        RecipeBeanDao recipeBeanDao = daoSession.getRecipeBeanDao();
////
////
////
////
//        List<RecipeBean> recipeBeans  = recipeBeanDao.queryBuilder().list();
//
//        for(RecipeBean item:recipeBeans ){
//            Log.e("Application", item.getName());
//        }

        //        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, getDbPath(), null);
        //第二个参数可以设置数据库的地址
//        MySQLiteOpenHelper mHelper = new MySQLiteOpenHelper(this,getDbPath(),null);
////        SQLiteDatabase db = mHelper.getWritableDatabase();
//        //加密
//        Database db = mHelper.getEncryptedWritableDb("1234");
//        DaoMaster mDaoMaster = new DaoMaster(db);
//        DaoSession mDaoSession = mDaoMaster.newSession();
//        UserDao userDao = mDaoSession.getUserDao();
//        User user=new User();
//        user.setName("李四");
//        user.setYear(10);
//        userDao.save(user);
//        List<User> users = userDao.loadAll();
//        textView.setText(users.get(0).getName());

        instance = this;
    }

    // 获取Application
    public static RecipeApplication getApplication() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }





}

