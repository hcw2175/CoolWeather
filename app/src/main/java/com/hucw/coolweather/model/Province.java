package com.hucw.coolweather.model;

/**
 * 省份
 * @author  hucw
 * @version 1.0.0
 */
public class Province {
    public static final String TABLE_NAME = "Province";


    private int id;
    private String provinceName;
    private String provinceCode;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
