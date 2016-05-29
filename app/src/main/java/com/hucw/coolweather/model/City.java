package com.hucw.coolweather.model;

/**
 * 城市数据管理
 * @author  hucw
 * @version 1.0.0
 */
public class City {
    public static final String TABLE_NAME = "City";

    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;          // 省份id

    public City() {}

    public City(String cityName, String cityCode, int provinceId) {
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
