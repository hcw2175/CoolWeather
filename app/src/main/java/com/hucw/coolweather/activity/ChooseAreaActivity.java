package com.hucw.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hucw.coolweather.R;
import com.hucw.coolweather.db.CoolWeatherDB;
import com.hucw.coolweather.model.City;
import com.hucw.coolweather.model.County;
import com.hucw.coolweather.model.Province;
import com.hucw.coolweather.util.HttpCallbackListener;
import com.hucw.coolweather.util.HttpUtil;
import com.hucw.coolweather.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择区域Activity
 * @author hucw
 * @version 1.0.0
 */
public class ChooseAreaActivity extends Activity {
    private static final String TAG = "ChooseAreaActivity";

    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;
    private static final String ADDRESS_BASE = "http://www.weather.com.cn/data/list3";

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinces;
    private List<City> cities;
    private List<County> counties;

    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private int currentLevel;

    private boolean isFromWeatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取参数，判断是否从WeatherActivity中跳转过来
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_weather", false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("city_selected", false) && !isFromWeatherActivity){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }

        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 设置布局
        setContentView(R.layout.choose_area);

        // 列表元素
        listView = (ListView)findViewById(R.id.list_view);

        // 标题
        titleText = (TextView)findViewById(R.id.title_text);

        // 适配器
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        // 数据库操作
        coolWeatherDB = CoolWeatherDB.getInstance(this);

        // 列表数据点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.valueOf(currentLevel));

                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinces.get(position);
                    queryCities();
                } else if( currentLevel == LEVEL_CITY){
                    selectedCity = cities.get(position);
                    queryCounties();
                } else if( currentLevel == LEVEL_COUNTY){
                    String countyCode = counties.get(position).getCountyCode();
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("county_code", countyCode);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // 查询省份数据
        queryProvinces();
    }

    /**
     * 查询省份数据
     */
    private void queryProvinces(){
        provinces = coolWeatherDB.getProvinceList();
        if(provinces.size() > 0){
            dataList.clear(); // 清除原有数据
            for (Province province : provinces){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged(); // 通知数据已改变

            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, "province");
        }
    }

    /**
     * 查询城市数据
     */
    private void queryCities(){
        cities = coolWeatherDB.getCityList(selectedProvince.getId());
        if(cities.size() > 0){
            dataList.clear(); // 清除原有数据
            for (City city : cities){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged(); // 通知数据已改变

            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    /**
     * 查询县区数据
     */
    private void queryCounties(){
        counties = coolWeatherDB.getCountyList(selectedCity.getId());
        if(counties.size() > 0){
            dataList.clear(); // 清除原有数据
            for (County county : counties){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged(); // 通知数据已改变

            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }

    /**
     * 从服务器查询数据
     * @param code 代码
     * @param type 类型
     */
    private void queryFromServer(final String code, final String type){
        String address;
        if(!TextUtils.isEmpty(code)){
            address = ADDRESS_BASE + "/city" + code + ".xml";
        } else {
            address = ADDRESS_BASE + "/city.xml";
        }

        // 显示loading
        this.showProgressDialog();

        // 发送网络请求
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result;
                if(type.equalsIgnoreCase("province")){
                    result = ResponseUtil.handleProvinceReponse(coolWeatherDB, response);
                }else if(type.equalsIgnoreCase("city")){
                    result = ResponseUtil.handleCityReponse(coolWeatherDB, response, selectedProvince.getId());
                }else{
                    result = ResponseUtil.handleCountyReponse(coolWeatherDB, response, selectedCity.getId());
                }

                if(result){
                    // 通过runOnUiThread()方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if(type.equalsIgnoreCase("province")){
                                queryProvinces();
                            }else if(type.equalsIgnoreCase("city")){
                                queryCities();
                            }else{
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();

                // 通过runOnUiThread()方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if(null == progressDialog){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 取消进度对话框
     */
    private void closeProgressDialog(){
        if(null == progressDialog)
            return;
        progressDialog.dismiss();
    }

    /**
     * 重写物理返回键事件
     * 根据当前级别判断，判断该列表是返回城市、省份列表，还是直接退出
     */
    @Override
    public void onBackPressed() {
        if(currentLevel == LEVEL_COUNTY){
            queryCities();
        }else if(currentLevel == LEVEL_CITY){
            queryProvinces();
        }else{
            if(isFromWeatherActivity){
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }
}
