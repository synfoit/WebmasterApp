package com.servilink.webmasterapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

public class Tag  implements   Serializable  {
    private Long tagId;
    private Long plcId;
    private String datatype;
    private String tagName;
    private String tagAddress;
    private Boolean status;
    private String tagType;
    List<String> stringList;
    public List<Dictionary> getDictionaryList() {
        return dictionaryList;
    }

    public void setDictionaryList(List<Dictionary> dictionaryList) {
        this.dictionaryList = dictionaryList;
    }

    List<Dictionary> dictionaryList;
    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    JSONArray jsonArray;

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public Tag(Long tagId,
               Long plcId,
               String datatype,
               String tagName,
               String tagAddress,
               Boolean status,
               String tagType, List<Dictionary> dictionaryList, List<String> stringList
            ) {
        this.tagId = tagId;
        this.plcId = plcId;
        this.datatype = datatype;
        this.tagName = tagName;
        this.tagAddress = tagAddress;
        this.status = status;
        this.tagType = tagType;
        this.dictionaryList = dictionaryList;
        this.stringList=stringList;

    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getPlcId() {
        return plcId;
    }

    public void setPlcId(Long plcId) {
        this.plcId = plcId;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagAddress() {
        return tagAddress;
    }

    public void setTagAddress(String tagAddress) {
        this.tagAddress = tagAddress;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }



}
