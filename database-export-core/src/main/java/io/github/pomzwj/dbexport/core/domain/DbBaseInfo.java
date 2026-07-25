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

    /**
     * 获取schemas
     *
     * @return schemas
     */
    public String getSchemas() {
        return schemas;
    }

    /**
     * 设置schemas
     *
     * @param schemas schemas
     */
    public void setSchemas(String schemas) {
        this.schemas = schemas;
    }

    /**
     * 获取ip
     *
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置ip
     *
     * @param ip ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取port
     *
     * @return port
     */
    public String getPort() {
        return port;
    }

    /**
     * 设置port
     *
     * @param port port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * 获取dbName
     *
     * @return dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * 设置dbName
     *
     * @param dbName dbName
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * 获取userName
     *
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置userName
     *
     * @param userName userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置password
     *
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * 获取dbKindEnum
     *
     * @return dbKindEnum
     */
    public DataBaseType getDbKindEnum() {
        return this.dbKindEnum;
    }

    /**
     * 设置dbKindEnum
     *
     * @param dbKindEnum dbKindEnum
     */
    public void setDbKindEnum(DataBaseType dbKindEnum) {
        this.dbKindEnum = dbKindEnum;
    }
}
