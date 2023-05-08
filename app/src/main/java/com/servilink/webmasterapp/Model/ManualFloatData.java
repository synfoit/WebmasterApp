package com.servilink.webmasterapp.Model;


public class ManualFloatData {
    private String actualDateTime;
    private String dateTime;
    private Long tagId;
    private Float value;

    public ManualFloatData(String id, long tagId, Float value) {
        this.tagId=tagId;
        this.value=value;
    }

    public String getActualDateTime() {
        return actualDateTime;
    }

    public void setActualDateTime(String actualDateTime) {
        this.actualDateTime = actualDateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public ManualFloatData(
            String actualDateTime,
            String dateTime,
            Long tagId,
            Float value
    ) {
        this.actualDateTime = actualDateTime;
        this.dateTime = dateTime;
        this.tagId = tagId;
        this.value = value;

    }
}
