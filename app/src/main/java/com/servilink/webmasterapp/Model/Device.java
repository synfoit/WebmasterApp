package com.servilink.webmasterapp.Model;

import java.io.Serializable;
import java.util.List;

public class Device implements Serializable {
    private int id;
    private String plcName; // Should be unique
    private String plcCompanyName;
    private String plcTypeName;
    private String plcPyFile;
    private String plcIp;
    private String plcStatus;
    private String plcCreatedBy;
    List<Tag> tagList;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public String getPlcCompanyName() {
        return plcCompanyName;
    }

    public void setPlcCompanyName(String plcCompanyName) {
        this.plcCompanyName = plcCompanyName;
    }

    public String getPlcTypeName() {
        return plcTypeName;
    }

    public void setPlcTypeName(String plcTypeName) {
        this.plcTypeName = plcTypeName;
    }

    public String getPlcPyFile() {
        return plcPyFile;
    }

    public void setPlcPyFile(String plcPyFile) {
        this.plcPyFile = plcPyFile;
    }

    public String getPlcIp() {
        return plcIp;
    }

    public void setPlcIp(String plcIp) {
        this.plcIp = plcIp;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public String getPlcStatus() {
        return plcStatus;
    }

    public void setPlcStatus(String plcStatus) {
        this.plcStatus = plcStatus;
    }

    public String getPlcCreatedBy() {
        return plcCreatedBy;
    }

    public void setPlcCreatedBy(String plcCreatedBy) {
        this.plcCreatedBy = plcCreatedBy;
    }

    public String getDashboardType() {
        return dashboardType;
    }

    public void setDashboardType(String dashboardType) {
        this.dashboardType = dashboardType;
    }

    private String dashboardType;
    public  Device(int id,
             String plcName, // Should be unique
             String plcCompanyName,
             String plcTypeName,
             String plcPyFile,
             String plcIp,
             String plcStatus,
             String plcCreatedBy,
             String dashboardType,List<Tag> tagList){
        this.id=id;
        this.plcName=plcName;
        this.plcCompanyName=plcCompanyName;
        this.plcTypeName=plcTypeName;
        this.plcPyFile=plcPyFile;
        this.plcIp=plcIp;
        this.plcStatus=plcStatus;
        this.plcCreatedBy=plcCreatedBy;
        this.dashboardType=dashboardType;
        this.tagList=tagList;

    }
}
