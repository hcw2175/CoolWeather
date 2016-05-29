package com.hucw.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hucw.coolweather.R;
import com.hucw.coolweather.service.AutoUpdateService;
import com.hucw.coolweather.util.HttpCallbackListener;
import com.hucw.coolweather.util.HttpUtil;
import com.hucw.coolweather.util.ResponseUtil;

/**
 * 天气管理Activity
 * @author hucw
 * @version 1.0.0
 */
public class WeatherActivity extends Activity {
    private static final String TAG = "WeatherActivity";

    private static final String ADDRESS_BASE = "http://www.weather.com.cn/data/list3";

    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;

    private Button mBtnSwitchCity;
    private Button mBtnRefreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather);

        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);

        String countyCode = getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }else{
            showWeather();
        }

        mBtnSwitchCity = (Button) findViewById(R.id.switch_city);
        mBtnSwitchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_weather", true);
                startActivity(intent);
                finish();
            }
        });

        mBtnRefreshWeather = (Button) findViewById(R.id.refresh_weather);
        mBtnRefreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishText.setText("同步中...");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherCode = sharedPreferences.getString("weather_code", "");
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
            }
        });
    }

    /**
     * 查询县区所对应的天气代号
     * @param countyCode 县区代码
     */
    private void queryWeatherCode(String countyCode){
        String address = ADDRESS_BASE + "/city" + countyCode + ".xml";
        queryFromServer(address, "weatherCode");
    }

    /**
     * 查询县区所对应的天气信息
     * @param weatherCode 天气代号
     */
    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(address, "weatherInfo");
    }

    private void queryFromServer(final String address, final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d(TAG, "响应数据： " + response);
                if(type.equalsIgnoreCase("weatherCode")){
                    if(!TextUtils.isEmpty(response)){
                        String[] array = response.split("\\|");
                        if(array.length == 2){
                            queryWeatherInfo(array[1]);
                        }
                    }
                } else {
                    ResponseUtil.handleWeatherReponse(WeatherActivity.this, response);
                    // 通过runOnUiThread()方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    private void showWeather(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(sharedPreferences.getString("city_name", ""));
        temp1Text.setText(sharedPreferences.getString("temp2", ""));
        temp2Text.setText(sharedPreferences.getString("temp1", ""));
        weatherDespText.setText(sharedPreferences.getString("weather_desp", ""));
        publishText.setText("今天" + sharedPreferences.getString("publish_time", "") + "发布");
        currentDateText.setText(sharedPreferences.getString("current_time", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);

        // 启动自动更新天气服务
        Intent i = new Intent(this, AutoUpdateService.class);
        this.startService(i);
    }
}
