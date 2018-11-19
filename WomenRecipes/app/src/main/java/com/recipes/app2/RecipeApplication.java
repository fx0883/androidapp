/*
 * Copyright (c) 2017. CMRI PRIVATE LIMITED. All rights reserved
 * Created by WangBo on 17-6-23 上午11:50
 *
 * Last modified 17-6-23 上午11:50
 */

package com.recipes.app2;

import android.util.Log;

import com.recipes.app2.model.bean.DaoMaster;
import com.recipes.app2.model.bean.DaoSession;
import com.recipes.app2.utils.FileUtils;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.disposables.CompositeDisposable;

public class RecipeApplication extends android.app.Application {

    public DaoSession daoSession;
    private static RecipeApplication instance;

    private final CompositeDisposable disposables = new CompositeDisposable();
    @Override
    public void onCreate() {
        super.onCreate();

        //复制assets目录下的数据库文件到应用数据库中
        try {
//            copyDataBase("zone.db");
            FileUtils.copyDataBase("db/womanrecipes.db",this);
        } catch (Exception e) {
            Log.e("Application", e.getMessage());
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "womanrecipes.db", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        instance = this;
    }

    // 获取Application
    public static RecipeApplication getApplication() {
        return instance;
    }

//    /**
//     * Copies your database from your local assets-folder to the just created
//     * empty database in the system folder, from where it can be accessed and
//     * handled. This is done by transfering bytestream.
//     * */
//    private void copyDataBase(String dbname) throws IOException {
//        // Open your local db as the input stream
//        InputStream myInput = this.getAssets().open(dbname);
//        // Path to the just created empty db
////        File outFileName = this.getDatabasePath(dbname);
//        // Open your local db as the input stream
//
//        String[] retList = dbname.split("/");
//        dbname = retList[retList.length-1];
//
//        File outFileName = this.getDatabasePath(dbname);
//        // Path to the just created empty db
//
//        if (!outFileName.exists()) {
//            outFileName.getParentFile().mkdirs();
//
//            // Open the empty db as the output stream
//            OutputStream myOutput = new FileOutputStream(outFileName);
//            // transfer bytes from the inputfile to the outputfile
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = myInput.read(buffer)) > 0) {
//                myOutput.write(buffer, 0, length);
//            }
//            // Close the streams
//            myOutput.flush();
//            myOutput.close();
//            myInput.close();
//        }
//    }
}
