package com.cstor.tanjiance.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class LatLngBean implements Serializable {

    @SerializedName("lng")
    private Double lng;
    @SerializedName("lat")
    private Double lat;
    @SerializedName("value")
    private Integer value;
    @SerializedName("co2")
    private Integer co2;
    @SerializedName("ch4")
    private Integer ch4;
    @SerializedName("c2h4")
    private Integer c2h4;
    @SerializedName("nh3")
    private Integer nh3;
    @SerializedName("c2s")
    private Integer c2s;
    @SerializedName("h2o")
    private Integer h2o;
    @SerializedName("c2h6")
    private Integer c2h6;

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getCo2() {
        return co2;
    }

    public void setCo2(Integer co2) {
        this.co2 = co2;
    }

    public Integer getCh4() {
        return ch4;
    }

    public void setCh4(Integer ch4) {
        this.ch4 = ch4;
    }

    public Integer getC2h4() {
        return c2h4;
    }

    public void setC2h4(Integer c2h4) {
        this.c2h4 = c2h4;
    }

    public Integer getNh3() {
        return nh3;
    }

    public void setNh3(Integer nh3) {
        this.nh3 = nh3;
    }

    public Integer getC2s() {
        return c2s;
    }

    public void setC2s(Integer c2s) {
        this.c2s = c2s;
    }

    public Integer getH2o() {
        return h2o;
    }

    public void setH2o(Integer h2o) {
        this.h2o = h2o;
    }

    public Integer getC2h6() {
        return c2h6;
    }

    public void setC2h6(Integer c2h6) {
        this.c2h6 = c2h6;
    }

    @Override
    public String toString() {
        return "LatLngBean{" +
                "lng=" + lng +
                ", lat=" + lat +
                ", value=" + value +
                ", co2=" + co2 +
                ", ch4=" + ch4 +
                ", c2h4=" + c2h4 +
                ", nh3=" + nh3 +
                ", c2s=" + c2s +
                ", h2o=" + h2o +
                ", c2h6=" + c2h6 +
                '}';
    }
}
