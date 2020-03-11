

package com.Recipes.app2;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.Recipes.app2.model.bean.DaoMaster;
import com.Recipes.app2.model.bean.DaoSession;
import com.Recipes.app2.utils.FileUtils;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.reactivex.disposables.CompositeDisposable;

public class RecipeApplication extends android.app.Application {

    public DaoSession daoSession;
    private static RecipeApplication instance;

    public Boolean isCollectMode = false;

    private final CompositeDisposable disposables = new CompositeDisposable();
    @Override
    public void onCreate() {
        super.onCreate();
        disableAPIDialog();
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

    public static Context getContext() {
        return instance.getApplicationContext();
    }


    /**
     * 反射 禁止弹窗
     */
    private void disableAPIDialog(){
        if (Build.VERSION.SDK_INT < 28)return;
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
