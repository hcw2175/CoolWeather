package com.hucw.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.hucw.coolweather.db.CoolWeatherDB;
import com.hucw.coolweather.model.City;
import com.hucw.coolweather.model.County;
import com.hucw.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 省份、城市、县/区数据解析工具
 * @author hucw
 * @version 1.0.0
 */
public class ResponseUtil {
    private static final String TAG = "ResponseUtil";

    /**
     * 解析并存储省份数据
     * @param coolWeatherDB 数据库操作实例
     * @param response      数据，格式如：01|北京,02|天津
     * @return 处理结果
     */
    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB, String response){
        if(response.contains("<!DOCTYPE HTML>")){
            Log.d(TAG, "数据请求错误");
            return false;
        }

        if(TextUtils.isEmpty(response)){
            Log.d(TAG, "返回的省份数据为空， 无法进行处理");
            return false;
        }

        String[] allProvinces = response.split(",");
        if(allProvinces.length == 0){
            Log.d(TAG, "返回的省份数据为空， 无法进行处理");
            return false;
        }

        for(String province : allProvinces){
            String[] provinceInfo = province.split("\\|");
            coolWeatherDB.saveProvince(new Province(provinceInfo[1],provinceInfo[0]));
        }
        return true;
    }

    /**
     * 解析并存储城市数据
     * @param coolWeatherDB 数据库操作实例
     * @param response      数据，格式如：1901|南京,1902|无锡
     * @param provinceId    所属省份id
     * @return 处理结果
     */
    public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId){
        if(response.contains("<!DOCTYPE HTML>")){
            Log.d(TAG, "数据请求错误");
            return false;
        }

        if(TextUtils.isEmpty(response)){
            Log.d(TAG, "返回的城市数据为空， 无法进行处理");
            return false;
        }

        String[] allCities = response.split(",");
        if(allCities.length == 0){
            Log.d(TAG, "返回的城市数据为空， 无法进行处理");
            return false;
        }

        for(String city : allCities){
            String[] cityInfo = city.split("\\|");
            coolWeatherDB.saveCity(new City(cityInfo[1],cityInfo[0], provinceId));
        }
        return true;
    }

    /**
     * 解析并存储县区数据
     * @param coolWeatherDB 数据库操作实例
     * @param response      数据，格式如：190401|苏州,190402|常熟
     * @param cityId        所属城市id
     * @return 处理结果
     */
    public synchronized static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB, String response, int cityId){
        if(response.contains("<!DOCTYPE HTML>")){
            Log.d(TAG, "数据请求错误");
            return false;
        }

        if(TextUtils.isEmpty(response)){
            Log.d(TAG, "返回的县区数据为空， 无法进行处理");
            return false;
        }

        String[] allCounties = response.split(",");
        if(allCounties.length == 0){
            Log.d(TAG, "返回的县区数据为空， 无法进行处理");
            return false;
        }

        for(String county : allCounties){
            String[] countyInfo = county.split("\\|");
            coolWeatherDB.saveCounty(new County(countyInfo[1],countyInfo[0], cityId));
        }
        return true;
    }

    /**
     * 解析并存储县区天气数据
     * @param context       上下文
     * @param response      数据，格式如：190401|苏州,190402|常熟
     * @return 处理结果
     */
    public static void handleWeatherReponse(Context context, String response){
        if(TextUtils.isEmpty(response)){
            Log.d(TAG, "返回的县区天气数据为空， 无法进行处理");
            return;
        }

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("city_selected", true);
            editor.putString("city_name", cityName);
            editor.putString("weather_code", weatherCode);
            editor.putString("temp1", temp1);
            editor.putString("temp2", temp2);
            editor.putString("weather_desp", weatherDesp);
            editor.putString("publish_time", publishTime);
            editor.putString("current_time", simpleDateFormat.format(new Date()));
            editor.apply();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
