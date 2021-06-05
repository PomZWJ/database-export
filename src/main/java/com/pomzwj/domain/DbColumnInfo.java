package com.pomzwj.domain;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author zhaowj
 * @date 2021-03-04
 */
public class DbColumnInfo implements Serializable {
    public String columnName;
    public String dataType;
    public String dataLength;
    public Boolean nullAble;
    public String defaultVal;
    public Boolean autoIncrement;
    public Boolean primary;
    public String comments;
    public String dataScale;

    public String getColumnName() {
        if(StringUtils.isEmpty(columnName)){
            return "";
        }
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        if(StringUtils.isEmpty(dataType)){
            return "";
        }
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataLength() {
        if(StringUtils.isEmpty(dataLength)){
            return "";
        }
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public Boolean getNullAble() {
        if(nullAble == null){
            return false;
        }
        return nullAble;
    }

    public void setNullAble(Boolean nullAble) {
        this.nullAble = nullAble;
    }

    public String getDefaultVal() {
        if(StringUtils.isEmpty(defaultVal)){
            return "";
        }
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public Boolean getAutoIncrement() {
        if(autoIncrement == null){
            return false;
        }
        return autoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Boolean getPrimary() {
        if(primary == null){
            return false;
        }
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getComments() {
        if(StringUtils.isEmpty(comments)){
            return "";
        }
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDataScale() {
        if(StringUtils.isEmpty(dataScale)){
            return "";
        }
        return dataScale;
    }

    public void setDataScale(String dataScale) {
        this.dataScale = dataScale;
    }
}
