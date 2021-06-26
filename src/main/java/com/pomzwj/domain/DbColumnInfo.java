package com.pomzwj.domain;

import com.pomzwj.anno.DataColumnName;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2021-03-04
 */
public class DbColumnInfo implements Serializable {
    @DataColumnName(name = "列名")
    public String columnName;
    @DataColumnName(name = "数据类型")
    public String dataType;
    @DataColumnName(name = "数据长度")
    public String dataLength;
    @DataColumnName(name = "是否为空")
    public Boolean nullAble;
    @DataColumnName(name = "默认值")
    public String defaultVal;
    @DataColumnName(name = "自增")
    public Boolean autoIncrement;
    @DataColumnName(name = "主键")
    public Boolean primary;
    @DataColumnName(name = "备注")
    public String comments;
    @DataColumnName(name = "精度")
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

    public String getNullAble() {
        if(nullAble == null || nullAble == false){
            return "否";
        }else{
            return "是";
        }
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

    public String getAutoIncrement() {
        if(autoIncrement == null || autoIncrement == false){
            return "否";
        }else{
            return "是";
        }
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public String getPrimary() {
        if(primary == null || primary == false){
            return "否";
        }else{
            return "是";
        }

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
