package io.github.pomzwj.dbexport.core.dbservice.sqlite;

import io.github.pomzwj.dbexport.core.anno.DataColumnName;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;

public class SqliteColumnInfo extends DbColumnInfo {
    @DataColumnName(name = "是否为空", order = 2)
    public Boolean nullAble;
    @DataColumnName(name = "默认值",order = 3)
    public String defaultVal;
    @DataColumnName(name = "主键",order = 4)
    public Boolean primary;
    @DataColumnName(name = "备注", order = Integer.MAX_VALUE)
    public String comments;

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
