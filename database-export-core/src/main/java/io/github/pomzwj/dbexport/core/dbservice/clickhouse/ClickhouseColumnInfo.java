package io.github.pomzwj.dbexport.core.dbservice.clickhouse;

import io.github.pomzwj.dbexport.core.anno.DataColumnName;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;

public class ClickhouseColumnInfo extends DbColumnInfo {
    @DataColumnName(name = "是否为空", order = 2)
    public Boolean nullAble;
    @DataColumnName(name = "默认值",order = 3)
    public String defaultVal;
    @DataColumnName(name = "备注", order = Integer.MAX_VALUE)
    public String comments;

    public Boolean getNullAble() {
        return nullAble;
    }

    public void setNullAble(Boolean nullAble) {
        this.nullAble = nullAble;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
