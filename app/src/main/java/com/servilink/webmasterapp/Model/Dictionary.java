package com.servilink.webmasterapp.Model;

import java.io.Serializable;

public class Dictionary implements Serializable {
    private Long Id;
    private Long tagId;
    private String dropDownValue;
    private String tagName;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getDropDownValue() {
        return dropDownValue;
    }

    public void setDropDownValue(String dropDownValue) {
        this.dropDownValue = dropDownValue;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Dictionary(Long Id,
                      Long tagId,
                      String dropDownValue,
                      String tagName) {
        this.Id = Id;
        this.tagId = tagId;
        this.dropDownValue = dropDownValue;
        this.tagName = tagName;
    }
}
