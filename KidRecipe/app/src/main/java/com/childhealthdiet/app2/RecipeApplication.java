package com.ChildHealthDiet.app2;
import android.content.Context;
import android.util.Log;
import com.ChildHealthDiet.app2.model.bean.DaoMaster;
import com.ChildHealthDiet.app2.model.bean.DaoSession;
import com.ChildHealthDiet.app2.utils.FileUtils;
import org.greenrobot.greendao.database.Database;

public class RecipeApplication extends android.app.Application {

    public DaoSession daoSession;
    private static RecipeApplication instance;
    public Boolean isCollectMode = false;

    @Override
    public void onCreate() {
        super.onCreate();
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
}

