package com.pomzwj.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据库类型
 */
public enum DataBaseType {
    /**
     * MYSQL
     */
    MYSQL,
    /**
     * ORACLE
     */
    ORACLE,
    /**
     * SQL_SERVER
     */
    SQLSERVER;

    DataBaseType(){

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
