package com.pomzwj.domain;

import com.pomzwj.constant.DataBaseType;
import com.pomzwj.constant.ExportFileType;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.utils.AssertUtils;

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
        return ExportFileType.matchType(this.getExportFileType());
    }

    private void setExportFileTypeEnum(ExportFileType exportFileTypeEnum) {
        this.exportFileTypeEnum = exportFileTypeEnum;
    }

    public DataBaseType getDbKindEnum() {
        return DataBaseType.matchType(this.getDbKind());
    }

    private void setDbKindEnum(DataBaseType dbKindEnum) {
        this.dbKindEnum = dbKindEnum;
    }

    public void fieldCheck(){
        AssertUtils.isNull(this.getDbKindEnum(), MessageCode.DATABASE_KIND_IS_NULL_ERROR);
        AssertUtils.isNull(this.getExportFileTypeEnum(), MessageCode.EXPORT_FILE_TYPE_IS_NOT_MATCH_ERROR);
        if(getDbKindEnum().equals(DataBaseType.SQLITE)){
            AssertUtils.isNull(this.getDbName(), MessageCode.DATABASE_NAME_IS_NULL_ERROR);
        }else{
            //参数校验
            AssertUtils.isNull(this.getIp(), MessageCode.DATABASE_IP_IS_NULL_ERROR);
            AssertUtils.isNull(this.getPort(), MessageCode.DATABASE_PORT_IS_NULL_ERROR);
            AssertUtils.isNull(this.getUserName(), MessageCode.DATABASE_USER_IS_NULL_ERROR);
            AssertUtils.isNull(this.getPassword(), MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR);
        }
    }
}
