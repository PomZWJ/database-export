package io.github.pomzwj.dbexport.web.params;

import io.github.pomzwj.dbexport.core.exception.MessageCode;
import io.github.pomzwj.dbexport.core.type.DataBaseType;
import io.github.pomzwj.dbexport.core.utils.AssertUtils;

import java.io.Serializable;

public class DbBaseParams implements Serializable {
    private String ip;
    private String port;
    private String dbName;
    private String userName;
    private String password;
    private DataBaseType dbKindEnum;
    private String schemas;

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
        return dbKindEnum;
    }

    public void setDbKindEnum(DataBaseType dbKindEnum) {
        this.dbKindEnum = dbKindEnum;
    }

    public void baseParamsCheck(){
        DataBaseType dbKindEnum = this.getDbKindEnum();
        AssertUtils.isNull(dbKindEnum, MessageCode.DATABASE_KIND_IS_NULL_ERROR);
        if (dbKindEnum.equals(DataBaseType.SQLITE)) {
            AssertUtils.isNull(this.getDbName(), MessageCode.DATABASE_DB_FILE_IS_NULL_ERROR);
        } else {
            //参数校验
            AssertUtils.isNull(this.getIp(), MessageCode.DATABASE_IP_IS_NULL_ERROR);
            AssertUtils.isNull(this.getPort(), MessageCode.DATABASE_PORT_IS_NULL_ERROR);
            AssertUtils.isNull(this.getUserName(), MessageCode.DATABASE_USER_IS_NULL_ERROR);
            AssertUtils.isNull(this.getPassword(), MessageCode.DATABASE_PASSWORD_IS_NULL_ERROR);
        }
    }
}
