package io.github.pomzwj.dbexport.core.domain;

import io.github.pomzwj.dbexport.core.exception.DatabaseExportException;
import io.github.pomzwj.dbexport.core.exception.MessageCode;
import io.github.pomzwj.dbexport.core.type.ExportFileType;
import io.github.pomzwj.dbexport.core.utils.AssertUtils;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import io.github.pomzwj.dbexport.core.utils.JdbcUrlParseUtils;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import net.bytebuddy.dynamic.DynamicType;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DbExportConfig {
    /**
     * 选中的表,用于选择性导出
     */
    private List<String> selectTableList;
    private ExportFileType exportFileTypeEnum;
    private String generationFileTempDir;
    /**
     * 是否显示索引
     */
    private boolean isSearchIndex;

    /**
     * 表名排序
     * A-Z 0
     * Z-A 1
     */
    private int tableOrder;


    /**
     * 显示的列信息
     */
    private List<String> showColumnList;

    /**
     * 显示的索引信息
     */
    private List<String> showIndexList;


    private Class<? extends DbColumnInfo> dbColumnInfoDynamicClazz;

    private Class<? extends DbIndexInfo> dbIndexInfoDynamicClazz;


    public void checkAndInitConfig(DataSource source){
        DbBaseInfo dbBaseInfo = JdbcUrlParseUtils.parseConnectionUrlToBaseInfo(source);
        if(dbBaseInfo == null || dbBaseInfo.getDbKindEnum() == null){
            throw new DatabaseExportException(MessageCode.DATABASE_SOURCE_UNRECOGNIZABLE);
        }
        Class<? extends DbColumnInfo> columnInfoClazz = dbBaseInfo.getDbKindEnum().getColumnInfoClazz();
        Class<? extends DbIndexInfo> indexInfoClazz = dbBaseInfo.getDbKindEnum().getIndexInfoClazz();
        boolean checkColumn = ClassUtils.checkColumnField(columnInfoClazz, this.getShowColumnList());
        boolean checkIndex = ClassUtils.checkIndexField(indexInfoClazz, this.getShowIndexList());

        if(!checkColumn){
            throw new DatabaseExportException(MessageCode.DATABASE_CUSTOMIZABLE_COLUMN_ERROR);
        }
        if(!checkIndex){
            throw new DatabaseExportException(MessageCode.DATABASE_CUSTOMIZABLE_INDEX_ERROR);
        }
        Class<? extends DbColumnInfo> dbColumnLoaded = ClassUtils.createDbColumBean(columnInfoClazz, this.getShowColumnList()).load(DbColumnInfo.class.getClassLoader()).getLoaded();
        Class<? extends DbIndexInfo> dbIndexLoaded = ClassUtils.createDbIndexBean(indexInfoClazz, this.getShowIndexList()).load(DbIndexInfo.class.getClassLoader()).getLoaded();
        this.setDbColumnInfoDynamicClazz(dbColumnLoaded);
        this.setDbIndexInfoDynamicClazz(dbIndexLoaded);
    }

    public void checkOutputConfig(){
        if (StringUtils.isBlank(this.getGenerationFileTempDir())) {
            throw new DatabaseExportException(MessageCode.OUTPUT_DIR_IS_NULL_ERROR);
        }
        if (this.getExportFileTypeEnum() == null) {
            throw new DatabaseExportException(MessageCode.OUTPUT_FILE_FORMAT_IS_NULL_ERROR);
        }
    }



    public ExportFileType getExportFileTypeEnum() {
        return this.exportFileTypeEnum;
    }

    public DbExportConfig setExportFileTypeEnum(ExportFileType exportFileTypeEnum) {
        this.exportFileTypeEnum = exportFileTypeEnum;
        return this;
    }


    public String getGenerationFileTempDir() {
        return generationFileTempDir;
    }

    public DbExportConfig setGenerationFileTempDir(String generationFileTempDir) {
        this.generationFileTempDir = generationFileTempDir;
        return this;
    }

    public void exportParamCheck(){
        AssertUtils.isNull(this.getGenerationFileTempDir(), MessageCode.FILE_PATH_IS_NULL_ERROR);
        AssertUtils.isNull(this.getExportFileTypeEnum(),MessageCode.EXPORT_FILE_TYPE_IS_NULL_ERROR);
    }

    public boolean isSearchIndex() {
        return isSearchIndex;
    }

    public DbExportConfig setSearchIndex(boolean searchIndex) {
        isSearchIndex = searchIndex;
        return this;
    }

    public List<String> getSelectTableList() {
        return selectTableList;
    }

    public DbExportConfig setSelectTableList(List<String> selectTableList) {
        this.selectTableList = selectTableList;
        return this;
    }

    public int getTableOrder() {
        return tableOrder;
    }

    public DbExportConfig setTableOrder(int tableOrder) {
        this.tableOrder = tableOrder;
        return this;
    }

    public List<String> getShowColumnList() {
        return showColumnList;
    }

    public DbExportConfig setShowColumnList(List<String> showColumnList) {
        this.showColumnList = showColumnList;
        return this;
    }

    public List<String> getShowIndexList() {
        return showIndexList;
    }

    public DbExportConfig setShowIndexList(List<String> showIndexList) {
        this.showIndexList = showIndexList;
        return this;
    }


    public Class<? extends DbColumnInfo> getDbColumnInfoDynamicClazz() {
        return dbColumnInfoDynamicClazz;
    }

    public void setDbColumnInfoDynamicClazz(Class<? extends DbColumnInfo> dbColumnInfoDynamicClazz) {
        this.dbColumnInfoDynamicClazz = dbColumnInfoDynamicClazz;
    }

    public Class<? extends DbIndexInfo> getDbIndexInfoDynamicClazz() {
        return dbIndexInfoDynamicClazz;
    }

    public void setDbIndexInfoDynamicClazz(Class<? extends DbIndexInfo> dbIndexInfoDynamicClazz) {
        this.dbIndexInfoDynamicClazz = dbIndexInfoDynamicClazz;
    }
}
