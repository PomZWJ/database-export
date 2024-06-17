package io.github.pomzwj.dbexport.core.domain;

import io.github.pomzwj.dbexport.core.type.DataBaseType;
import io.github.pomzwj.dbexport.core.exception.MessageCode;
import io.github.pomzwj.dbexport.core.utils.AssertUtils;

import java.io.Serializable;

/**
 * 类说明:数据库基础信息
 *
 * @author PomZWJ
 */
public class DbBaseInfo implements Serializable {
    private String ip;
    private String port;
    private String dbName;
    private String userName;
    private String password;
    private String schemas;
    private DataBaseType dbKindEnum;

    public String getSchemas() {
        return schemas;
    }

    public void setSchemas(String schemas) {
        this.schemas = schemas;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public DataBaseType getDbKindEnum() {
        return this.dbKindEnum;
    }

    public void setDbKindEnum(DataBaseType dbKindEnum) {
        this.dbKindEnum = dbKindEnum;
    }
}
