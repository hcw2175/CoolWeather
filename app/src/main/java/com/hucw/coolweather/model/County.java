package com.hucw.coolweather.model;

/**
 * 县/区
 * @author  hucw
 * @version 1.0.0
 */
public class County {
    public static final String TABLE_NAME = "County";

    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;

    public County() {}

    public County(String countyName, String countyCode, int cityId) {
        this.countyName = countyName;
        this.countyCode = countyCode;
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
