package com.servilink.webmasterapp.Model;

import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;

public class dashboarLayoutd {
    int id;
    String dashboarLayoutdName;
    String plcName;
    int plcId;
    String unit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDashboarLayoutdName() {
        return dashboarLayoutdName;
    }

    public void setDashboarLayoutdName(String dashboarLayoutdName) {
        this.dashboarLayoutdName = dashboarLayoutdName;
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public int getPlcId() {
        return plcId;
    }

    public void setPlcId(int plcId) {
        this.plcId = plcId;
    }

    public String getDashboarName() {
        return dashboarName;
    }

    public void setDashboarName(String dashboarName) {
        this.dashboarName = dashboarName;
    }

    public int getDashboarId() {
        return dashboarId;
    }

    public void setDashboarId(int dashboarId) {
        this.dashboarId = dashboarId;
    }

    public String getTagsType() {
        return tagsType;
    }

    public void setTagsType(String tagsType) {
        this.tagsType = tagsType;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public ArrayList<dashboardLayoutTag> getDashboardLayoutTags() {
        return dashboardLayoutTags;
    }

    public void setDashboardLayoutTags(ArrayList<dashboardLayoutTag> dashboardLayoutTags) {
        this.dashboardLayoutTags = dashboardLayoutTags;
    }

    public ArrayList<JSONArray> getListOfValues() {
        return listOfValues;
    }

    public void setListOfValues(ArrayList<JSONArray> listOfValues) {
        this.listOfValues = listOfValues;
    }

    public ArrayList<String> getValueTime() {
        return valueTime;
    }

    public void setValueTime(ArrayList<String> valueTime) {
        this.valueTime = valueTime;
    }

    String dashboarName;
    int dashboarId;
    String tagsType;
    String chartType;
    int min;
    int max;
    ArrayList<dashboardLayoutTag> dashboardLayoutTags;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    ArrayList<JSONArray> listOfValues;
    ArrayList<String> valueTime;

    public  dashboarLayoutd(String dashboarLayoutdName,String plcName, String chartType,ArrayList<dashboardLayoutTag> dashboardLayoutTags, ArrayList<JSONArray> listOfValues,  ArrayList<String> valueTime,String unit,int min,int max){
        this.dashboarLayoutdName=dashboarLayoutdName;
        this.chartType=chartType;
        this.plcName=plcName;
        this.dashboardLayoutTags=dashboardLayoutTags;
        this.listOfValues=listOfValues;
        this.valueTime=valueTime;
        this.unit=unit;
        this.min=min;
        this.max=max;
    }
}
