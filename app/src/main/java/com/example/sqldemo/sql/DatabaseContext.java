package com.example.sqldemo.sql;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 用于支持对存储在SD卡上的数据库的访问
 * Created by bmw on 2016/9/24 0024.
 */
public class DatabaseContext extends ContextWrapper {
    private static final String TAG = DatabaseContext.class.getClass().getName();

    /**
     * 构造函数
     *
     * @param base 上下文环境
     */
    public DatabaseContext(Context base) {
        super(base);
    }

    /**
     * @param name 数据库名称
     */
    @Override
    public File getDatabasePath(String name) {
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if (!sdExist) {
            Log.e(TAG, "SD卡不存在，请加载SD卡");
            return null;
        } else {
            String dbDir = android.os.Environment.getExternalStorageDirectory().toString();
            dbDir += "/test_database";
            String dbPath = dbDir + "/" + name;
            File dirFile = new File(dbDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            boolean isFileCreateSuccess = false;
            //判断文件是否存在，不存在则创建该文件
            File dbFile = new File(dbPath);
            if (!dbFile.exists()) {
                try {
                    isFileCreateSuccess = dbFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                isFileCreateSuccess = true;

            //返回数据库文件对象
            if (isFileCreateSuccess)
                return dbFile;
            else
                return null;
        }
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see android.content.ContextWrapper#openOrCreateDatabase(String, int,
     * android.database.sqlite.SQLiteDatabase.CursorFactory,
     * android.database.DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        File file = getDatabasePath(name);
        if (null != file) {
            Log.e(TAG, "db located==>" + file.getAbsolutePath());
        }
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(file, null);
        return result;
    }

}
