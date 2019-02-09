package com.decodesqlite.app2.decodesqlite;


import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import net.sqlcipher.database.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_TABLE = "\uFEFFCREATE TABLE childrenfood (\n" +
            "    id          INTEGER NOT NULL\n" +
            "                        PRIMARY KEY,\n" +
            "    name        TEXT    NOT NULL,\n" +
            "    ingredients TEXT    NOT NULL,\n" +
            "    practice    TEXT    NOT NULL,\n" +
            "    prompt      TEXT    NOT NULL,\n" +
            "    symptoms    TEXT    NOT NULL\n" +
            "                        DEFAULT 口角炎,\n" +
            "    month       TEXT,\n" +
            "    lock        TEXT,\n" +
            "    eatTime     TEXT,\n" +
            "    type        TEXT,\n" +
            "    picture     TEXT,\n" +
            "    material    TEXT,\n" +
            "    collect     BOOLEAN,\n" +
            "    basket      BOOLEAN,\n" +
            "    bedit       BOOLEAN\n" +
            ");";

    public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

    }

}