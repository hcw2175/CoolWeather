package com.hucw.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.hucw.coolweather.activity.WeatherActivity;
import com.hucw.coolweather.api.WeatherService;
import com.hucw.coolweather.helper.RetrofitHelper;
import com.hucw.coolweather.receiver.AutoUpdateReceiver;
import com.hucw.coolweather.util.HttpCallbackListener;
import com.hucw.coolweather.util.HttpUtil;
import com.hucw.coolweather.util.ResponseUtil;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 后台自动更新天气服务
 * @author hucw
 * @version 1.0.0
 */
public class AutoUpdateService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 启动新的线程更新天气数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();

        // 设置定时任务
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 更新间隔：8小时
        int anHour = 8*60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;

        // 设置任务接收器
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode = sharedPreferences.getString("weather_code", "");
        if(!TextUtils.isEmpty(weatherCode)){
            WeatherService weatherService = RetrofitHelper.createApi(this, WeatherService.class);
            weatherService.getWeatherInfo(weatherCode).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        ResponseUtil.handleWeatherReponse(AutoUpdateService.this, response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
