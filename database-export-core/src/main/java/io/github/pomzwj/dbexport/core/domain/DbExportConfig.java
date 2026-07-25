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

/**
 * 类说明:数据库导出配置
 *
 * @author PomZWJ
 */
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
     * 显示的列信息
     */
    private List<String> showColumnList;

    /**
     * 显示的索引信息
     */
    private List<String> showIndexList;


    private Class<? extends DbColumnInfo> dbColumnInfoDynamicClazz;

    private Class<? extends DbIndexInfo> dbIndexInfoDynamicClazz;

    /**
     * 缓存解析后的数据库基础信息，避免在 executeFile 链路中被重复解析
     */
    private DbBaseInfo dbBaseInfo;


    /**
     * 检查并初始化配置
     *
     * @param source 数据源
     */
    public void checkAndInitConfig(DataSource source){
        DbBaseInfo dbBaseInfo = JdbcUrlParseUtils.parseConnectionUrlToBaseInfo(source);
        if(dbBaseInfo == null || dbBaseInfo.getDbKindEnum() == null){
            throw new DatabaseExportException(MessageCode.DATABASE_SOURCE_UNRECOGNIZABLE);
        }
        //缓存解析结果，供后续 DbServiceFactory / makeFile 复用
        this.dbBaseInfo = dbBaseInfo;
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

    /**
     * 检查输出配置
     */
    public void checkOutputConfig(){
        if (StringUtils.isBlank(this.getGenerationFileTempDir())) {
            throw new DatabaseExportException(MessageCode.OUTPUT_DIR_IS_NULL_ERROR);
        }
        if (this.getExportFileTypeEnum() == null) {
            throw new DatabaseExportException(MessageCode.OUTPUT_FILE_FORMAT_IS_NULL_ERROR);
        }
    }



    /**
     * 获取exportFileTypeEnum
     *
     * @return exportFileTypeEnum
     */
    public ExportFileType getExportFileTypeEnum() {
        return this.exportFileTypeEnum;
    }

    /**
     * 设置exportFileTypeEnum
     *
     * @param exportFileTypeEnum exportFileTypeEnum
     * @return 当前配置对象
     */
    public DbExportConfig setExportFileTypeEnum(ExportFileType exportFileTypeEnum) {
        this.exportFileTypeEnum = exportFileTypeEnum;
        return this;
    }


    /**
     * 获取generationFileTempDir
     *
     * @return generationFileTempDir
     */
    public String getGenerationFileTempDir() {
        return generationFileTempDir;
    }

    /**
     * 设置generationFileTempDir
     *
     * @param generationFileTempDir generationFileTempDir
     * @return 当前配置对象
     */
    public DbExportConfig setGenerationFileTempDir(String generationFileTempDir) {
        this.generationFileTempDir = generationFileTempDir;
        return this;
    }

    /**
     * 导出参数检查
     */
    public void exportParamCheck(){
        AssertUtils.isNull(this.getGenerationFileTempDir(), MessageCode.FILE_PATH_IS_NULL_ERROR);
        AssertUtils.isNull(this.getExportFileTypeEnum(),MessageCode.EXPORT_FILE_TYPE_IS_NULL_ERROR);
    }

    /**
     * 获取isSearchIndex
     *
     * @return isSearchIndex
     */
    public boolean isSearchIndex() {
        return isSearchIndex;
    }

    /**
     * 设置searchIndex
     *
     * @param searchIndex searchIndex
     * @return 当前配置对象
     */
    public DbExportConfig setSearchIndex(boolean searchIndex) {
        isSearchIndex = searchIndex;
        return this;
    }

    /**
     * 获取selectTableList
     *
     * @return selectTableList
     */
    public List<String> getSelectTableList() {
        return selectTableList;
    }

    /**
     * 设置selectTableList
     *
     * @param selectTableList selectTableList
     * @return 当前配置对象
     */
    public DbExportConfig setSelectTableList(List<String> selectTableList) {
        this.selectTableList = selectTableList;
        return this;
    }

    /**
     * 获取showColumnList
     *
     * @return showColumnList
     */
    public List<String> getShowColumnList() {
        return showColumnList;
    }

    /**
     * 设置showColumnList
     *
     * @param showColumnList showColumnList
     * @return 当前配置对象
     */
    public DbExportConfig setShowColumnList(List<String> showColumnList) {
        this.showColumnList = showColumnList;
        return this;
    }

    /**
     * 获取showIndexList
     *
     * @return showIndexList
     */
    public List<String> getShowIndexList() {
        return showIndexList;
    }

    /**
     * 设置showIndexList
     *
     * @param showIndexList showIndexList
     * @return 当前配置对象
     */
    public DbExportConfig setShowIndexList(List<String> showIndexList) {
        this.showIndexList = showIndexList;
        return this;
    }


    /**
     * 获取dbColumnInfoDynamicClazz
     *
     * @return dbColumnInfoDynamicClazz
     */
    public Class<? extends DbColumnInfo> getDbColumnInfoDynamicClazz() {
        return dbColumnInfoDynamicClazz;
    }

    /**
     * 设置dbColumnInfoDynamicClazz
     *
     * @param dbColumnInfoDynamicClazz dbColumnInfoDynamicClazz
     */
    public void setDbColumnInfoDynamicClazz(Class<? extends DbColumnInfo> dbColumnInfoDynamicClazz) {
        this.dbColumnInfoDynamicClazz = dbColumnInfoDynamicClazz;
    }

    /**
     * 获取dbIndexInfoDynamicClazz
     *
     * @return dbIndexInfoDynamicClazz
     */
    public Class<? extends DbIndexInfo> getDbIndexInfoDynamicClazz() {
        return dbIndexInfoDynamicClazz;
    }

    /**
     * 设置dbIndexInfoDynamicClazz
     *
     * @param dbIndexInfoDynamicClazz dbIndexInfoDynamicClazz
     */
    public void setDbIndexInfoDynamicClazz(Class<? extends DbIndexInfo> dbIndexInfoDynamicClazz) {
        this.dbIndexInfoDynamicClazz = dbIndexInfoDynamicClazz;
    }

    /**
     * 获取dbBaseInfo
     *
     * @return dbBaseInfo
     */
    public DbBaseInfo getDbBaseInfo() {
        return dbBaseInfo;
    }
}
