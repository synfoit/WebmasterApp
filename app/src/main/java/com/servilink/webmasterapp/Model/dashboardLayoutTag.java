package com.servilink.webmasterapp.Model;

public class dashboardLayoutTag {
    int id;
    String dashboardLayoutTagName;
    int tagId;
    String tagName;
    String dashboardLayoutName;
    String dashboardName;
    int dashboardId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDashboardLayoutTagName() {
        return dashboardLayoutTagName;
    }

    public void setDashboardLayoutTagName(String dashboardLayoutTagName) {
        this.dashboardLayoutTagName = dashboardLayoutTagName;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getDashboardLayoutName() {
        return dashboardLayoutName;
    }

    public void setDashboardLayoutName(String dashboardLayoutName) {
        this.dashboardLayoutName = dashboardLayoutName;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public int getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(int dashboardId) {
        this.dashboardId = dashboardId;
    }

    public int getDashboardLayoutId() {
        return dashboardLayoutId;
    }

    public void setDashboardLayoutId(int dashboardLayoutId) {
        this.dashboardLayoutId = dashboardLayoutId;
    }

    public int getPlcId() {
        return plcId;
    }

    public void setPlcId(int plcId) {
        this.plcId = plcId;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    int dashboardLayoutId;
    int plcId;
    String tagType;
    public dashboardLayoutTag(String dashboardLayoutTagName, String tagName,String dashboardLayoutName,String dashboardName){
        this.dashboardLayoutName=dashboardLayoutName;
        this.dashboardLayoutTagName=dashboardLayoutTagName;
        this.tagName=tagName;
        this.dashboardName=dashboardName;
    }
}
