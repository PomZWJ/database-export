package io.github.pomzwj.dbexport.core.filegeneration;

import io.github.pomzwj.dbexport.core.anno.DataColumnName;
import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.*;
import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import io.github.pomzwj.dbexport.core.utils.JdbcUrlParseUtils;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFileGenerationService implements FileGenerationService {
    static final Logger log = LoggerFactory.getLogger(AbstractFileGenerationService.class);

    @Override
    public File makeFile(DataSource dataSource, DbExportConfig dbExportConfig, List<DbTable> tableList) throws Exception {
        DbBaseInfo dbBaseInfo = JdbcUrlParseUtils.parseConnectionUrlToBaseInfo(dataSource);
        dbBaseInfoThreadLocal.set(dbBaseInfo);
        dbExportConfigThreadLocal.set(dbExportConfig);
        dataSourdceInfoThreadLocal.set(dataSource);
        //判断传入的生成文档地址，如果没有则使用默认地址
        String generationFileTempDir = dbExportConfig.getGenerationFileTempDir();

        File fileDir = new File(generationFileTempDir);
        if (!fileDir.isDirectory() || !fileDir.exists()) {
            log.info("generation File Dir is not exist ,create it={}", fileDir.getAbsolutePath());
            fileDir.mkdirs();
        } else {
            log.info("generation File Dir={}", fileDir.getAbsolutePath());
        }
        //文件名的名称=dbName+时间戳.格式
        String fileName = dbBaseInfo.getDbName() + "_" + System.currentTimeMillis() + dbExportConfig.getExportFileTypeEnum().getFileSuffixName();
        File file = new File(generationFileTempDir + File.separator + fileName);
        if (file.exists() && file.isFile()) {
            FileUtils.deleteQuietly(file);
        } else {
            file.createNewFile();
        }
        this.makeFileStream(tableList, file);
        return file;

    }

    /**
     * 获取表头的中文名称
     *
     * @param columnInfoClazz 列信息
     * @return 获取表头的中文名称
     */
    protected List<String> getColumnHeaderName(Class<? extends DbColumnInfo> columnInfoClazz) {
        //获取表头
        List<String> headerList = new ArrayList<>();
        for (Field declaredField : ClassUtils.sortColumnField(columnInfoClazz)) {
            declaredField.setAccessible(true);
            DataColumnName annotation = declaredField.getAnnotation(DataColumnName.class);
            String annoName = annotation.name();
            headerList.add(annoName);
        }
        return headerList;
    }

    /**
     * 获取表索引的中文名称
     *
     * @param indexInfoClazz 索引class
     * @return 表索引的中文名称
     */
    protected List<String> getIndexHeaderName(Class<? extends DbIndexInfo> indexInfoClazz) {
        //获取表头
        List<String> headerList = new ArrayList<>();
        for (Field declaredField : ClassUtils.sortIndexField(indexInfoClazz)) {
            declaredField.setAccessible(true);
            DbIndexName annotation = declaredField.getAnnotation(DbIndexName.class);
            String annoName = annotation.name();
            headerList.add(annoName);
        }
        return headerList;
    }

    /**
     * 根据表头获取表格数据
     * @param dbTable 表信息
     * @param columnInfoClazz  columnInfoClazz
     * @return 根据表头获取表格数据
     * @throws Exception 抛出异常信息
     */
    protected List<List<String>> getTableColumnData(DbTable dbTable, Class<? extends DbColumnInfo> columnInfoClazz) throws Exception {
        List<DbColumnInfo> tabsColumn = dbTable.getTabsColumn();
        List<List<String>> dataBodys = new ArrayList<>();
        for (int k = 0; k < tabsColumn.size(); k++) {
            DbColumnInfo dbColumnInfo = tabsColumn.get(k);
            dataBodys.add(this.getTableData(columnInfoClazz, dbColumnInfo));
        }
        return dataBodys;
    }


    /**
     * 根据表索引获取表格数据
     * @param dbTable 表信息
     * @param indexInfoClazz  indexInfoClazz
     * @return 根据表头获取表格数据
     * @throws Exception 抛出异常信息
     */
    protected List<List<String>> getTableIndexData(DbTable dbTable, Class<? extends DbIndexInfo> indexInfoClazz) throws Exception {
        List<DbIndexInfo> tabsColumnIndexs = dbTable.getTabsIndex();
        if (CollectionUtils.isEmpty(tabsColumnIndexs)) {
            return new ArrayList<>();
        }
        List<List<String>> dataBodys = new ArrayList<>();
        for (int k = 0; k < tabsColumnIndexs.size(); k++) {
            DbIndexInfo dbIndexInfo = tabsColumnIndexs.get(k);
            dataBodys.add(this.getTableIndexData(indexInfoClazz, dbIndexInfo));
        }
        return dataBodys;
    }

    protected List<String> getTableData(Class<? extends DbColumnInfo> columnInfoClazz, DbColumnInfo dbColumnInfo) throws Exception {
        List<String> dataBody = new ArrayList<>();
        for (Field declaredField : ClassUtils.sortColumnField(columnInfoClazz)) {
            declaredField.setAccessible(true);
            dataBody.add(StringUtils.getValue(declaredField.get(dbColumnInfo)));
        }
        return dataBody;
    }

    protected List<String> getTableIndexData(Class<? extends DbIndexInfo> indexInfoClazz, DbIndexInfo dbColumnInfo) throws Exception {
        List<String> dataBody = new ArrayList<>();
        for (Field declaredField : ClassUtils.sortIndexField(indexInfoClazz)) {
            declaredField.setAccessible(true);
            dataBody.add(StringUtils.getValue(declaredField.get(dbColumnInfo)));
        }
        return dataBody;
    }

    protected abstract void makeFileStream(List<DbTable> tableList, File targetFile) throws Exception;


}
