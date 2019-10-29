package com.example.sqldemo.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 注意1.增删改用execute，查用select +sql语句
 * 2.版本更新，VERSION++，没有数据库走onCreate,迭代走onUpdate
 * 3.此类具有更新添加字段时备份功能（onUpdate方法）
 * 4.此类封装了SQLiteOpenHelper的各种方法
 * 5.游标Cursor用完了一定要关闭
 * 6.ContentValues用在增加，和UpDate上
 * 7.特别注意事务的作用，比如支付失败，更新失败。。
 * 字段名一旦修改，就相当于把所有的条目都删除了
 * Created by wangmaobo on 2016/9/22 0022.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "three_country.db";
    private static final int DATABASE_VERSION = 1;
    private static volatile SQLiteHelper Instance;
    public static final String NAME_COUNTRY = "country";
    public static final String NAME_PERSON = "person";

    public static SQLiteHelper getInstance(Context context) {
        if (null == Instance) {
            synchronized (SQLiteHelper.class) {
                if (null == Instance) {
                    DatabaseContext databaseContext = new DatabaseContext(context);
                    Instance = new SQLiteHelper(databaseContext);
                }
            }
        }
        return Instance;
    }

    /**
     * @param context
     */
    private SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e(getClass().getName(), "init database!");
    }

    //1、人与国家是多对一关系 在创建表时，先建被关联的表country，才能建关联表person
    //2、在插入记录时，必须先插被关联的表country，才能插关联表person
    private String createCountryTable = "CREATE TABLE " + NAME_COUNTRY + "(countryId INTEGER PRIMARY KEY AUTOINCREMENT,countryName TEXT)";
    private String createPersonTable = "create table " + NAME_PERSON +
            "(personId INTEGER PRIMARY KEY AUTOINCREMENT,personName TEXT,countryId INTEGER,foreign key(countryId) references "//foreign 指定关联字段，名字可以不一样
            + NAME_COUNTRY + "(countryId) on update cascade on delete cascade)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(getClass().getName(), "onCreate!");
        db.execSQL(createCountryTable);
        db.execSQL(createPersonTable);
        db.execSQL("insert into country(countryId,countryName) values (1,'魏'),(2,'蜀'),(3,'吴')");
        db.execSQL("insert into person(personId,personName,countryId) values (11,'曹操',1),(12,'刘备',2),(13,'孙权',3),(14,'夏侯惇',1),(15,'诸葛亮',2),(16," +
                "'甘宁',3)");
    }

    //加字段更新方法1：只更新国家的 此方法可以 修改，增加或删除字段名称
    //创建临时表，缓存上个版本的旧数据,以删除countryName,只保留countryName举例
    private String CREATE_TEMP_COUNTRY = "alter table " + NAME_COUNTRY + " rename to temp_country";
    //创建新数据库
    private String CREATE_UPDATE_COUNTRY = "create table " + NAME_COUNTRY + "(countryId INTEGER PRIMARY KEY AUTOINCREMENT)";
    //将临时表里的数据插入到新数据库 //select * ,'' from temp_country
    private String INSERT_DATA = "insert into " + NAME_COUNTRY + " select countryId from temp_country";
    //删除临时数据库
    private String DROP_TEMP_COUNTRY = "drop table temp_country";

    //加字段更新方法2：只更新国家的 此方法只能 添加字段或修改字段名称，以增加举例
    private static String ADD_COUNTRY = "alter table country add column location INTEGER";

    //或者方法3:同样是删除
    private String CREATE_TEMP_COUNTRY2 = "create table temp_country as select countryId from country";
    private String DROP_OLD_COUNTRY = "drop table country";
    private String RENAME_COUNTRY = "alter table temp_country rename to country";

    //create table temp as select recordId, customer, place, time from record where 1 = 1;
    //drop table record;
    //alter table temp rename to record;
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(getClass().getName(), "onUpgrade! oldVersion=" + oldVersion + ",newVersion=" + newVersion);
        //update database
        try {
            if (newVersion > oldVersion) {
                db.beginTransaction();
                //way 1
//                db.execSQL(CREATE_TEMP_COUNTRY);
//                db.execSQL(CREATE_UPDATE_COUNTRY);
//                db.execSQL(INSERT_DATA);
//                db.execSQL(DROP_TEMP_COUNTRY);


                //添加字段的具体值，需要修改ben类添加字段，比如设置sex=1
                //SQLiteDatabaseDAO.getInstance(this).updateCountryById("1");
                //way 2
//                db.execSQL(ADD_COUNTRY);
//                db.execSQL("update country set location=100 where countryId=1");


                //way 3
                db.execSQL(CREATE_TEMP_COUNTRY2);
                db.execSQL(DROP_OLD_COUNTRY);
                db.execSQL(RENAME_COUNTRY);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != db) {
                db.endTransaction();
            }
        }
    }
}
