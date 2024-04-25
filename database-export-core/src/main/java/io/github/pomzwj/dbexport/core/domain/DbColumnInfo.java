package io.github.pomzwj.dbexport.core.domain;

import io.github.pomzwj.dbexport.core.anno.DataColumnName;

import java.io.Serializable;

/**
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2021-03-04
 */
public class DbColumnInfo implements Serializable {
    @DataColumnName(name = "列名", order = 0)
    public String columnName;
    @DataColumnName(name = "数据类型", order = 1)
    public String dataType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
