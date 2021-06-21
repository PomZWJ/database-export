package com.pomzwj.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 数据库类型(详情见DbColumnInfo注解)
 * @author PomZwj
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
    SQLSERVER(Arrays.asList("columnName", "dataType","dataLength","dataScale","nullAble","primary","autoIncrement", "defaultVal", "comments"));

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
