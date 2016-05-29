package com.hucw.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.hucw.coolweather.model.City;
import com.hucw.coolweather.model.County;
import com.hucw.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * App数据库操作工具
 * @author  hucw
 * @version 1.0.0
 */
public class CoolWeatherDB {
    private static final String TAG = "CoolWeatherDB";

    /** 数据库名称 */
    public static final String DB_NAME = "cool_weather";
    /** 数据库版本 */
    public static final int DB_VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;     // 静态实例
    private SQLiteDatabase sqLiteDatabase;          // sqlLite数据库实例

    /**
     * constructor
     * ------------------------------------------- */
    public CoolWeatherDB(Context context) {
        // 获取sqlLite数据库私有化实例
        CoolWeatherDBHelper coolWeatherDBHelper = new CoolWeatherDBHelper(context, DB_NAME, null, DB_VERSION);
        sqLiteDatabase = coolWeatherDBHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB静态实例
     * @param context 上下文
     * @return
     */
    public synchronized static CoolWeatherDB getInstance(Context context){
        if(null == coolWeatherDB)
            coolWeatherDB = new CoolWeatherDB(context);
        return coolWeatherDB;
    }

    /**
     * methods for Province
     * ------------------------------------------- */
    /**
     * 保存省份数据
     * @param province
     */
    public void saveProvince(Province province){
        if(null != province){
            Log.d(TAG, "saveProvince: " + province.getProvinceName());

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
        cursor.close();
        return provinces;
    }

    /**
     * methods for City
     * ------------------------------------------- */
    /**
     * 保存城市数据
     * @param city 城市
     */
    public void saveCity(City city){
        if(null != city){
            Log.d(TAG, "saveCity: " + city.getCityName());

            ContentValues values = new ContentValues();   // 数据存储对象
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            sqLiteDatabase.insert(City.TABLE_NAME, null, values);
        }
    }

    /**
     * 获取城市数据列表
     * @return List
     */
    public List<City> getCityList(int provinceId){
        List<City> cities = new ArrayList<>();
        // 获取游标
        Cursor cursor = this.sqLiteDatabase.query(City.TABLE_NAME, null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                cities.add(city);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return cities;
    }

    /**
     * methods for County
     * ------------------------------------------- */
    /**
     * 保存县/区数据
     * @param county 县/区
     */
    public void saveCounty(County county){
        if(null != county){
            Log.d(TAG, "saveCounty: " + county.getCountyName());
            ContentValues values = new ContentValues();   // 数据存储对象
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            sqLiteDatabase.insert(County.TABLE_NAME, null, values);
        }
    }

    /**
     * 获取县/区数据列表
     * @return List
     */
    public List<County> getCountyList(int cityId){
        List<County> counties = new ArrayList<>();
        // 获取游标
        Cursor cursor = this.sqLiteDatabase.query(County.TABLE_NAME, null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
        if(cursor.moveToFirst()){
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                counties.add(county);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return counties;
    }
}
