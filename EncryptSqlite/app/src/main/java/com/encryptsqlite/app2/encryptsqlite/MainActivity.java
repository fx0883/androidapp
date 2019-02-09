//package com.encryptsqlite.app2.encryptsqlite;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;

package com.encryptsqlite.app2.encryptsqlite;

//import android.content.Context;
//import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//
//import net.sqlcipher.database.SQLiteDatabase;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//public class MainActivity extends AppCompatActivity {
//
//    private String TAG = "MainActivity";
//    private String baoName = "com.encryptsqlite.app2.encryptsqlite";
//    File dbFile = new File("/data"
//            + Environment.getDataDirectory().getAbsolutePath()
//            + "/" + baoName + "/databases/");
//    String dbPath = "/data"
//            + Environment.getDataDirectory().getAbsolutePath()
//            + "/" + baoName + "/databases/babydietfood.db";// 要把你Raw文件的db保存到sdcard中
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initPublicDB(); // 初始化数据库
//
//        try {
//            encrypt(this,"babydietfood.db","test123"); // 加密数据库
////            SQLCipherUtils.encrypt() to encrypt an existing database.
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    private void encrypt2(Context ctxt,String encryptedName,String decryptedName,String key) {
////        Message msg = new Message();
//        try {
////            File file = new File(getApplication().getDatabasePath(DATA_PATH + encryptedName).getPath());
//            File originalFile = ctxt.getDatabasePath(decryptedName); // 获取数据库路径
//
//            if (originalFile.exists()) { // 判断数据库是否存在
//                Log.i("SSS","db yes");
//            }else {
//                Log.i("SSS","db no");
//            }
//
//            // 新建加密后的数据库文件，并设置其密码key
//            SQLiteDatabase dataTarget = SQLiteDatabase.openOrCreateDatabase(ctxt.getDatabasePath(decryptedName).getPath()+encryptedName, key, null);
//            String path = File.separator + "data" + File.separator +decryptedName;
//            //执行sql语句，连接未加密的数据库，并将其取别名为sourceLib，因为未加密数据库没密码，所以密码为""
//            dataTarget.execSQL("attach '"+path+"' as sourceLib key '';");
//            /*String passwordString = "1234"; //只能对已加密的数据库修改密码，且无法直接修改为“”或null的密码
//            database.changePassword(passwordString.toCharArray());*/
//
//            // 执行sql语句，在加密后的数据库文件中新建表，并将未加密的数据库表拷贝到新的加密数据库中，原数据库有多张表，该操作重复多少次
//            dataTarget.execSQL("create table new_table as select * from sourceLib.table");
//
//            //断开同加密后的数据库的连接
//            dataTarget.execSQL("detach database 'sourceLib'");
//            dataTarget.close();
////            msg.what = SUCCESS_ID;
//        } catch (Exception e) {
////            msg.what = FAIL_ID;
//            e.printStackTrace();
//        }finally {
////            mHandler.sendMessage(msg);
//        }
//    }
//
//
//
//
//    /**
//     * 加密函数
//     * 来源：https://www.jianshu.com/p/3baf311f8c8c
//     *
//     */
//    public void encrypt(Context ctxt, String dbName,
//                        String passphrase) throws IOException {
//        File originalFile = ctxt.getDatabasePath(dbName); // 获取数据库路径
//
//        if (originalFile.exists()) { // 判断数据库是否存在
//            Log.i("SSS","db yes");
//        }else {
//            Log.i("SSS","db no");
//        }
//
//        if (originalFile.exists()) {
//            File newFile =
//                    File.createTempFile("sqlcipherutils", "tmp",
//                            ctxt.getCacheDir());
//            SQLiteDatabase.loadLibs(getApplicationContext());
//            SQLiteDatabase db =
//                    SQLiteDatabase.openDatabase(originalFile.getAbsolutePath(),
//                            "", null,
//                            SQLiteDatabase.OPEN_READWRITE);
//
//            db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s';",
//                    newFile.getAbsolutePath(), passphrase));
//            db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
//            db.rawExecSQL("DETACH DATABASE encrypted;");
//
//            int version = db.getVersion();
//
//            db.close();
//
//            db =
//                    SQLiteDatabase.openDatabase(newFile.getAbsolutePath(),
//                            passphrase, null,
//                            SQLiteDatabase.OPEN_READWRITE);
//            db.setVersion(version);
//            db.close();
//
//            originalFile.delete();
//            newFile.renameTo(originalFile);
//        }
//    }
//
//    /**
//     * 初始化数据库
//     */
//    private void initPublicDB() {
//        if (!dbFile.exists()) { // 如果文件夹不存在，则创建新的文件夹
//            dbFile.mkdirs();
//        }
//        if (!(new File(dbPath).exists())) {  //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
//            Log.v(TAG, "导入数据库到/" + baoName + "/databases/");
//            InputStream is = getResources().openRawResource(R.raw.babydietfood); // 要导入的数据库
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(dbPath);
//                byte[] buffer = new byte[1024];
//                int count = 0;
//                while ((count = is.read(buffer)) > 0) {
//                    fos.write(buffer, 0, count);
//                }
//                fos.flush();
//                fos.close();
//                is.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Log.v(TAG, " " + (new File(dbPath).exists()));
//    }
//}



import android.os.Environment;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;              //注意导入的包

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int SUCCESS_ID = 1;
    private static final int FAIL_ID = 0;
    private Button mEncryptButton;
    private Button mDecryptButton;

    private String DATA_PATH = "dbs";

    private String baoName = "com.encryptsqlite.app2.encryptsqlite";
    File dbFile = new File("/data"
            + Environment.getDataDirectory().getAbsolutePath()
            + "/" + baoName + "/databases/");
    String dbPath = "/data"
            + Environment.getDataDirectory().getAbsolutePath()
            + "/" + baoName + "/databases/babydietfood.db";// 要把你Raw文件的db保存到sdcard中

//    MainActivity.this.getFilesDir().getAbsolutePath()
//                    + File.separator + encryptedName

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 该语句必不可少
        SQLiteDatabase.loadLibs(MainActivity.this);

        mEncryptButton = (Button) findViewById(R.id.btn_encry);
        mDecryptButton = (Button) findViewById(R.id.btn_read);
        // 开启按钮点击进行加密
        mEncryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 开启一个子线程来作为加密的耗时操作
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 这里传入新加密后的数据库名，未加密数据库名，以及加密的密码
                        encrypt("encrypted.db", "babydietfood.db", "key123");
                    }
                });
                thread.start();

            }
        });

        mDecryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里因为加密好的数据库放到了/data/data/包名/files/目录下，注意这里取得是绝对路径
                String path = MainActivity.this.getFilesDir().getAbsolutePath()
                        + File.separator + "encrypted.db";
                readClipherData(path, "key123");
            }
        });

//        File file = new File(getApplication().getDatabasePath(DATA_PATH + "bad.db").getPath());
//
        initPublicDB();
    }

    /**
     * 初始化数据库
     */
    private void initPublicDB() {
//        if (!dbFile.exists()) { // 如果文件夹不存在，则创建新的文件夹
//            dbFile.mkdirs();
//        }

        dbPath = MainActivity.this.getFilesDir().getAbsolutePath()
                + File.separator + "babydietfood.db";
        if (!(new File(dbPath).exists())) {  //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
            Log.v(TAG, "导入数据库到/" + baoName + "/databases/");
            InputStream is = getResources().openRawResource(R.raw.babydietfood); // 要导入的数据库
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(dbPath);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.v(TAG, " " + (new File(dbPath).exists()));
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS_ID:
                    Toast.makeText(MainActivity.this, "加密成功", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_ID:
                    Toast.makeText(MainActivity.this, "加密失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 加密数据库
     */
    private void encrypt(String encryptedName,String decryptedName,String key) {
        Message msg = new Message();
        try {
            File file = new File(getApplication().getDatabasePath(DATA_PATH + encryptedName).getPath());
            // 新建加密后的数据库文件，并设置其密码key
            SQLiteDatabase dataTarget = SQLiteDatabase.openOrCreateDatabase(MainActivity.this.getFilesDir().getAbsolutePath()
                    + File.separator + encryptedName, key, null);
//            String path = File.separator + "data" + File.separator +decryptedName;

//            String path = File.separator + "files" + File.separator +decryptedName;
            String path = "/data/user/0/com.encryptsqlite.app2.encryptsqlite/files/babydietfood.db";
            //执行sql语句，连接未加密的数据库，并将其取别名为sourceLib，因为未加密数据库没密码，所以密码为""
            dataTarget.execSQL("attach '"+path+"' as sourceLib key '';");
            /*String passwordString = "1234"; //只能对已加密的数据库修改密码，且无法直接修改为“”或null的密码
            database.changePassword(passwordString.toCharArray());*/

            // 执行sql语句，在加密后的数据库文件中新建表，并将未加密的数据库表拷贝到新的加密数据库中，原数据库有多张表，该操作重复多少次
//            dataTarget.execSQL("create table new_table as select * from sourceLib.table");


            dataTarget.execSQL("create table childrenfood as select * from sourceLib.childrenfood");

            //断开同加密后的数据库的连接
            dataTarget.execSQL("detach database 'sourceLib'");
            dataTarget.close();
            msg.what = SUCCESS_ID;
        } catch (Exception e) {
            msg.what = FAIL_ID;
            e.printStackTrace();
        }finally {
            mHandler.sendMessage(msg);
        }
    }

    /*
     *读取加密后的数据
     */

    private void readClipherData(String databasePath, String key){
        try{
            SQLiteDatabase encrypteddatabase = SQLiteDatabase.openOrCreateDatabase(databasePath, key, null);
            String[] columns  = new String[]{
                    "*"
            };
            String sections = "";     //这里填写查询条件
            Cursor cursor = encrypteddatabase.query("childrenfood", columns, sections, null, null, null, null);
            if (cursor == null){
                Toast.makeText(MainActivity.this, "未查询到数据", Toast.LENGTH_SHORT).show();
            }else {
                if (cursor.moveToFirst()) {
                    do {
//                        String value = cursor.getString(cursor.getColumnIndexOrThrow("value"));
                        String value = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        Log.e(TAG, "readClipherData: value = " + value);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            encrypteddatabase.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}




