package com.hucw.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Android SQLite数据库辅助类
 * @author  hucw
 * @version 1.0.0
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    /** Province表建表语句 */
    public static final String CREATE_PROVINCE = "create table Province (" + "id integer primary key autoincrement, "
            + "province_name text, "
            + "province_code text)";

    /** City表建表语句 */
    public static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "city_name text, "
            + "city_code text, "
            + "province_id integer)";


    /** County表建表语句 */
    public static final String CREATE_COUNTY = "create table County ("
            + "id integer primary key autoincrement, "
            + "county_name text, "
            + "county_code text, "
            + "city_id integer)";


    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
