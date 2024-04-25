package io.github.pomzwj.dbexport.core.dbservice.sqlserver;

import io.github.pomzwj.dbexport.core.anno.DataColumnName;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;

public class SqlServerColumnInfo extends DbColumnInfo {
    @DataColumnName(name = "是否为空", order = 2)
    public Boolean nullAble;
    @DataColumnName(name = "数据长度",order = 3)
    public String dataLength;
    @DataColumnName(name = "精度",order = 4)
    public String dataScale;
    @DataColumnName(name = "默认值",order = 5)
    public String defaultVal;
    @DataColumnName(name = "备注", order = Integer.MAX_VALUE)
    public String comments;
    @DataColumnName(name = "主键",order = 6)
    public Boolean primary;
    @DataColumnName(name = "自增",order = 7)
    public Boolean autoIncrement;

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public String getDataScale() {
        return dataScale;
    }

    public void setDataScale(String dataScale) {
        this.dataScale = dataScale;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public Boolean getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Boolean getNullAble() {
        return nullAble;
    }

    public void setNullAble(Boolean nullAble) {
        this.nullAble = nullAble;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
