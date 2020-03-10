package com.ChildHealthDiet.app2;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.ChildHealthDiet.app2.model.bean.DaoMaster;
import com.ChildHealthDiet.app2.model.bean.DaoSession;
import com.ChildHealthDiet.app2.utils.FileUtils;
import org.greenrobot.greendao.database.Database;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RecipeApplication extends android.app.Application {

    public DaoSession daoSession;
    private static RecipeApplication instance;
    public Boolean isCollectMode = false;

    @Override
    public void onCreate() {
        super.onCreate();
        disableAPIDialog();
        //复制assets目录下的数据库文件到应用数据库中
        try {
            FileUtils.copyDataBase("db/babydietfood.db",this);
        } catch (Exception e) {
            Log.e("Application", e.getMessage());
        }
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "babydietfood.db");
        Database db = helper.getEncryptedWritableDb("key123");
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


}

