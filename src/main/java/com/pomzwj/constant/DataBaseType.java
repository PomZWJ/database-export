package com.pomzwj.constant;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 数据库类型(详情见DbColumnInfo注解)
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public enum DataBaseType {
    /**
     * MYSQL
     */
    MYSQL(Arrays.asList("columnName", "dataType","nullAble","primary","autoIncrement", "defaultVal", "comments")),
    /**
     * ORACLE
     */
    ORACLE(Arrays.asList("columnName", "dataType","dataLength","dataScale","nullAble", "defaultVal", "comments")),
    /**
     * SQL_SERVER
     */
    SQLSERVER(Arrays.asList("columnName", "dataType","dataLength","dataScale","nullAble","primary","autoIncrement", "defaultVal", "comments")),

    /**
     * postgresql
     */
    POSTGRESQL(Arrays.asList("columnName", "dataType","nullAble", "defaultVal", "comments")),

    /**
     * clickhouse
     */
    CLICKHOUSE(Arrays.asList("columnName","dataType","nullAble","defaultVal","comments"));
    private List<String>columnName;

    DataBaseType(List<String> columnName){
        this.columnName = columnName;
    }

    public List<String> getColumnName() {
        return columnName;
    }

    public void setColumnName(List<String> columnName) {
        this.columnName = columnName;
    }

    public static DataBaseType matchType(String value){
        if (StringUtils.isNotEmpty(value)) {
            for (DataBaseType dataBase: DataBaseType.values()) {
                if (dataBase.name().equals(value.toUpperCase())) {
                    return dataBase;
                }
            }
        }
        return null;
    }
}
