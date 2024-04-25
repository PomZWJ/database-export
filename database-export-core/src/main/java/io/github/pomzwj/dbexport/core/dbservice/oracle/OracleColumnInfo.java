package io.github.pomzwj.dbexport.core.dbservice.oracle;

import io.github.pomzwj.dbexport.core.anno.DataColumnName;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;

public class OracleColumnInfo extends DbColumnInfo {
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
