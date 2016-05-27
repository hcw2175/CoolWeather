package com.hucw.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.hucw.coolweather.model.City;
import com.hucw.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * App数据库操作工具
 * @author  hucw
 * @version 1.0.0
 */
public class CoolWeatherDB {
    public static final String TAG = "CoolWeatherDB";

    /** 数据库名称 */
    public static final String DB_NAME = "cool_weather";
    /** 数据库版本 */
    public static final int DB_VERSION = 1;


    private static CoolWeatherDB coolWeatherDB;     // 静态实例
    private SQLiteDatabase sqLiteDatabase;          // sqlLite数据库实例

    /**
     * constructor
     * ------------------------------------------- */
    public CoolWeatherDB() {}

    public CoolWeatherDB(Context context) {
        // 获取sqlLite数据库私有化实例
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context, DB_NAME, null, DB_VERSION);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB静态实例
     * @param context 上下文
     * @return
     */
    public synchronized static CoolWeatherDB getInstance(Context context){
        if(null != coolWeatherDB)
            return coolWeatherDB;
        return new CoolWeatherDB(context);
    }

    /**
     * methods for Province
     * ------------------------------------------- */
    /**
     * 保存省份数据
     * @param province
     */
    public void saveProvince(Province province){
        Log.d(TAG, "saveProvince");
        if(null != province){
            ContentValues values = new ContentValues();   // 数据存储对象
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            sqLiteDatabase.insert(Province.TABLE_NAME, null, values);
        }
    }

    /**
     * 获取省份数据列表
     * @return
     */
    public List<Province> getProvinceList(){
        List<Province> provinces = new ArrayList<>();
        // 获取游标
        Cursor cursor = this.sqLiteDatabase.query(Province.TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinces.add(province);
            }while (cursor.moveToNext());
        }
        return provinces;
    }

    /**
     * methods for City
     * ------------------------------------------- */
    /**
     * 保存城市数据
     * @param city
     */
    public void saveCity(City city){
        Log.d(TAG, "saveCity");
        if(null != city){
            ContentValues values = new ContentValues();   // 数据存储对象
            values.put("province_name", city.getProvinceName());
            values.put("province_code", city.getProvinceCode());
            sqLiteDatabase.insert(Province.TABLE_NAME, null, values);
        }
    }

    /**
     * 获取省份数据列表
     * @return
     */
    public List<Province> getProvinceList(){
        List<Province> provinces = new ArrayList<>();
        // 获取游标
        Cursor cursor = this.sqLiteDatabase.query(Province.TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinces.add(province);
            }while (cursor.moveToNext());
        }
        return provinces;
    }
}
