package com.example.sqldemo.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sqldemo.sql.bean.Country;
import com.example.sqldemo.sql.bean.Person;

/**
 * Created by hp on 2019/10/15.
 */
public class SQLiteDatabaseDAO {
    private static SQLiteDatabaseDAO Instance = null;
    private SQLiteHelper mHelper;

    private SQLiteDatabaseDAO(Context context) {
        mHelper = SQLiteHelper.getInstance(context.getApplicationContext());
    }

    public synchronized static SQLiteDatabaseDAO getInstance(Context context) {
        if (null == Instance) {
            Instance = new SQLiteDatabaseDAO(context.getApplicationContext());
        }
        return Instance;
    }

    //=============================================Person======================================================
    public synchronized void insertPerson(int id, String name, int countryId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            db.insert(SQLiteHelper.NAME_PERSON, null, buildPerson(id, name, countryId));
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != db) {
                db.endTransaction();
                db.close();
            }
        }

    }

    public synchronized void deletePersonById(String id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(SQLiteHelper.NAME_PERSON, "personId=?", new String[]{id});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != db) {
                db.endTransaction();
                db.close();
            }
        }

    }

    public synchronized void updatePersonById(String id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            Person person = selectPersonById(id);//查找
            ContentValues target = buildPerson(person.personId, "王二", 1);//修改+插入
            db.update(SQLiteHelper.NAME_PERSON, target, "personId=?", new String[]{person.personId + ""});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != db) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized Person selectPersonById(String id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLiteHelper.NAME_PERSON, null, "personId = ?", new String[]{id}, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            return getPerson(cursor);
        }
        return null;
    }

    public synchronized void selectAllPerson() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(SQLiteHelper.NAME_PERSON, null, null, null, null, null, null);
            if (null != cursor && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Log.e(getClass().getName(), "personId=" + cursor.getString(cursor.getColumnIndex("personId"))
                            + "personName=" + cursor.getString(cursor.getColumnIndex("personName"))
                            + "countryId=" + cursor.getString(cursor.getColumnIndex("countryId")));
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=============================================Country======================================================
    public synchronized void insertCountry(int id, String name) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            db.insert(SQLiteHelper.NAME_COUNTRY, null, buildCountry(id, name));
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != db) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized void deleteCountryById(String id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            //db.delete(SQLiteHelper.NAME_COUNTRY, "countryId=?", new String[]{id});
            db.execSQL("delete from country where countryId=" + id);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != db) {
                db.endTransaction();
                db.close();
            }
        }

    }

    /**
     * 升级版本插入
     *
     * @param id
     */
    public synchronized void updateCountryById(String id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            Country country = selectCountryById(id);//查找
            ContentValues target = buildCountry2(country.countryId, "王二", 1);//修改+插入
            db.update(SQLiteHelper.NAME_COUNTRY, target, "countryId=?", new String[]{country.countryId + ""});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != db) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized Country selectCountryById(String id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLiteHelper.NAME_COUNTRY, null, "countryId=?", new String[]{id}, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            return getCountry(cursor);
        }
        return null;
    }

    public synchronized void selectAllCountry() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(SQLiteHelper.NAME_COUNTRY, null, null, null, null, null, null);
            if (null != cursor && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Log.e(getClass().getName(), "countryId=" + cursor.getString(cursor.getColumnIndex("countryId"))
                            + "countryName=" + cursor.getString(cursor.getColumnIndex("countryName")));
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=============================================多表联合查询======================================================

    public synchronized void selectPersonByCountryId(String id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        try {
//            Cursor cursor = db.rawQuery("select * from country a join person b on a.countryId = b.countryId  where a.countryId = ? ",
//                    new String[]{id});
            Cursor cursor = db.rawQuery("select * from person a join country b on a.countryId = b.countryId  where a.countryId = ? ",
                    new String[]{id});
            if (null != cursor && cursor.getCount() > 0) {//cursor是通过person表获取的
                while (cursor.moveToNext()) {
                    Log.e(getClass().getName(),
                            "countryId=" + cursor.getString(cursor.getColumnIndex("countryId"))
                                    + "personId=" + cursor.getString(cursor.getColumnIndex("personId"))
                                    + "personName=" + cursor.getString(cursor.getColumnIndex("personName")));
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Country getCountry(Cursor cursor) {
        Country country = new Country(cursor.getInt(cursor.getColumnIndex("countryId")),
                cursor.getString(cursor.getColumnIndex("countryName")));
        return country;
    }

    private Person getPerson(Cursor cursor) {
        Person person = new Person(cursor.getInt(cursor.getColumnIndex("personId")),
                cursor.getString(cursor.getColumnIndex("personName")),
                cursor.getInt(cursor.getColumnIndex("countryId")));
        return person;
    }

    private ContentValues buildCountry(int id, String name) {
        ContentValues cvs = new ContentValues();
        cvs.put("countryId", id);
        cvs.put("countryName", name);
        return cvs;
    }

    private ContentValues buildCountry2(int id, String name, int sex) {
        ContentValues cvs = new ContentValues();
        cvs.put("countryId", id);
        cvs.put("countryName", name);
        cvs.put("sex", sex);
        return cvs;
    }

    private ContentValues buildPerson(int id, String name, int countryId) {
        ContentValues cvs = new ContentValues();
        cvs.put("personId", id);
        cvs.put("personName", name);
        cvs.put("countryId", countryId);
        return cvs;
    }
}
