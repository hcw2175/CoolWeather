package com.hucw.coolweather.api;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * 获取天气Retrofit Service api接口
 * @author hucw
 * @version 1.0.0
 */
public interface WeatherService {

    /**
     * 查询所有省份
     * @return
     */
    @GET("list3/city.xml")
    Call<ResponseBody> findAllProvince();

    /**
     * 根据代码查询市或者县(区)
     * @param code 代码
     * @return
     */
    @GET("list3/city{code}.xml")
    Call<ResponseBody> findArea(@Path("code") String code);

    /**
     * 获取指定区域天气数据
     * @param weatherCode 天气代码
     * @return
     */
    @GET("cityinfo/{weatherCode}.html")
    Call<ResponseBody> getWeatherInfo(@Path("weatherCode") String weatherCode);
}
