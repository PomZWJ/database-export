package com.pomzwj.domain;

import com.pomzwj.constant.DataBaseType;
import com.pomzwj.constant.ExportFileType;

import java.io.Serializable;

/**
 * 类说明:数据库基础信息
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2018/10/30/0030.
 */
public class DbBaseInfo implements Serializable{
    private String dbKind;
    private String ip;
    private String port;
    private String dbName;
    private String userName;
    private String password;
    private String exportFileType;
    private ExportFileType exportFileTypeEnum;
    private DataBaseType dbKindEnum;

    public String getDbKind() {
        return dbKind;
    }

    public void setDbKind(String dbKind) {
        this.dbKind = dbKind;
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

    public String getExportFileType() {
        return exportFileType;
    }

    public void setExportFileType(String exportFileType) {
        this.exportFileType = exportFileType;
    }

    public ExportFileType getExportFileTypeEnum() {
        return exportFileTypeEnum;
    }

    public void setExportFileTypeEnum(ExportFileType exportFileTypeEnum) {
        this.exportFileTypeEnum = exportFileTypeEnum;
    }

    public DataBaseType getDbKindEnum() {
        return dbKindEnum;
    }

    public void setDbKindEnum(DataBaseType dbKindEnum) {
        this.dbKindEnum = dbKindEnum;
    }
}
