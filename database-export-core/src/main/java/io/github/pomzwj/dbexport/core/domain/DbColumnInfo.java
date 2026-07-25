package io.github.pomzwj.dbexport.core.domain;

import io.github.pomzwj.dbexport.core.anno.DataColumnName;

import java.io.Serializable;

/**
 * 类说明:数据库列信息
 *
 * @author PomZWJ
 */
public class DbColumnInfo implements Serializable {
    /**
     * 列名
     */
    @DataColumnName(name = "列名", order = 0)
    public String columnName;
    /**
     * 数据类型
     */
    @DataColumnName(name = "数据类型", order = 1)
    public String dataType;

    /**
     * 获取columnName
     *
     * @return columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 设置columnName
     *
     * @param columnName columnName
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * 获取dataType
     *
     * @return dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * 设置dataType
     *
     * @param dataType dataType
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
