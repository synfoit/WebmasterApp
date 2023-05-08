package com.servilink.webmasterapp.Model;

import java.sql.Timestamp;

public class ManualEntry {

    private String actualDateTime;
    private String dateTime;
    private Long tagId;
    private String value;

    public ManualEntry(String id, Long tagId, String value) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ManualEntry(
            String actualDateTime,
            String dateTime,
            Long tagId,
            String value
    ) {
        this.actualDateTime = actualDateTime;
        this.dateTime = dateTime;
        this.tagId = tagId;
        this.value = value;

    }
}
