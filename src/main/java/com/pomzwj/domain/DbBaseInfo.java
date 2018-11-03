package com.pomzwj.domain;

import java.io.Serializable;

/**
 * 类说明:数据库基础信息
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/30/0030.
 */
public class DbBaseInfo implements Serializable{
    private String ip;
    private String port;
    private String dbName;
    private String userName;
    private String password;

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
}
