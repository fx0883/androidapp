package com.ChildHealthDiet.app2.model.update;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Update2Helper {
    private static final String TAG = "BookChapterHelper";
    private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION = "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS";

    private static final String DIVIDER = ",";
    private static final String QUOTE = "'%s'";

    private static Update2Helper instance;

    public static Update2Helper getInstance() {
        if (instance == null) {
            instance = new Update2Helper();
        }
        return instance;
    }

    public void update(Database db) {
        updateCollBook(db);
        updateBookChapter(db);
    }

    private void updateBookChapter(Database db) {
    }

    private void updateCollBook(Database db) {
    }

    /**
     * 生成临时列表
     *
     * @param db
     */
    private void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>> bookChapterClass) {

    }


    /**
     * 通过反射，删除要更新的表
     */
    private void deleteOriginalTables(Database db, Class<? extends AbstractDao<?, ?>> bookChapterClass) {
        try {
            Method method = bookChapterClass.getMethod("dropTable", Database.class, boolean.class);
            method.invoke(null, db, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射，重新创建要更新的表
     */
    private void createOrignalTables(Database db, Class<? extends AbstractDao<?, ?>> bookChapterClass) {
        try {
            Method method = bookChapterClass.getMethod("createTable", Database.class, boolean.class);
            method.invoke(null, db, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储新的数据库表 以及数据
     *
     * @param db
     */
    private void restoreData(Database db, Class<? extends AbstractDao<?, ?>> bookChapterClass) {
        DaoConfig daoConfig = new DaoConfig(db, bookChapterClass);
        String tableName = daoConfig.tablename;
        String tempTableName = daoConfig.tablename.concat("_TEMP");
        ArrayList<String> properties = new ArrayList();

        for (int j = 0; j < daoConfig.properties.length; j++) {
            String columnName = daoConfig.properties[j].columnName;
            if (getColumns(db, tableName).contains(columnName)) {
                properties.add(columnName);
            }
        }

        StringBuilder insertTableStringBuilder = new StringBuilder();

        insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
        insertTableStringBuilder.append(TextUtils.join(",", properties));
        insertTableStringBuilder.append(") SELECT ");
        insertTableStringBuilder.append(TextUtils.join(",", properties));
        insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");

        Log.d(TAG, "restoreData: " + insertTableStringBuilder.toString());

        StringBuilder dropTableStringBuilder = new StringBuilder();
        dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
        db.execSQL(insertTableStringBuilder.toString());
        db.execSQL(dropTableStringBuilder.toString());
    }

    private String getTypeByClass(Class<?> type) throws Exception {
        if (type.equals(String.class)) {
            return "TEXT";
        }
        if (type.equals(Long.class) || type.equals(Integer.class) || type.equals(long.class)) {
            return "INTEGER";
        }
        if (type.equals(boolean.class)) {
            return "BOOLEAN";
        }

        Exception exception = new Exception(CONVERSION_CLASS_NOT_FOUND_EXCEPTION.concat(" - Class: ").concat(type.toString()));
        exception.printStackTrace();
        throw exception;
    }

    private List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return columns;
    }
}
