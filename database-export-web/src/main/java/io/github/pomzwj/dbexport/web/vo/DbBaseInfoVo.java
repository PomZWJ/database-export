package io.github.pomzwj.dbexport.web.vo;

import io.github.pomzwj.dbexport.core.domain.DbBaseInfo;
import io.github.pomzwj.dbexport.core.type.DataBaseType;
import org.springframework.beans.BeanUtils;

public class DbBaseInfoVo {
    private String ip;
    private String port;
    private String dbName;
    private String userName;
    private String password;
    private String exportFileType;
    private String dbKind;

    /**
     * db2使用
     */
    private String schemas;
    /**
     * 选中的表,用于选择性导出
     */
    private String selectTableStr;
    private String indexSetList;
    private String columnSetList;
    private int showIndex;

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

    public String getDbKind() {
        return dbKind;
    }

    public void setDbKind(String dbKind) {
        this.dbKind = dbKind;
    }


    public String getSchemas() {
        return schemas;
    }

    public void setSchemas(String schemas) {
        this.schemas = schemas;
    }

    public String getSelectTableStr() {
        return selectTableStr;
    }

    public void setSelectTableStr(String selectTableStr) {
        this.selectTableStr = selectTableStr;
    }

    public DbBaseInfo toBase(){
        DbBaseInfo dbBaseInfo = new DbBaseInfo();
        BeanUtils.copyProperties(this, dbBaseInfo);
        //dbBaseInfo.setExportFileTypeEnum(ExportFileType.matchType(this.getExportFileType()));
        dbBaseInfo.setDbKindEnum(DataBaseType.matchType(this.getDbKind()));
        return dbBaseInfo;
    }

    public String getIndexSetList() {
        return indexSetList;
    }

    public void setIndexSetList(String indexSetList) {
        this.indexSetList = indexSetList;
    }

    public String getColumnSetList() {
        return columnSetList;
    }

    public void setColumnSetList(String columnSetList) {
        this.columnSetList = columnSetList;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }
}
