package io.github.pomzwj.dbexport.core.filegeneration;

import io.github.pomzwj.dbexport.core.domain.DbExportConfig;
import io.github.pomzwj.dbexport.core.domain.DbBaseInfo;
import io.github.pomzwj.dbexport.core.domain.DbTable;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;

/**
 * 文件生成服务接口，定义根据数据库表生成文档文件的能力。
 *
 * @author PomZWJ
 */
public interface FileGenerationService {
    /**
     * 数据源线程本地变量
     */
    ThreadLocal<DataSource> dataSourdceInfoThreadLocal = new ThreadLocal<>();

    /**
     * 数据库基本信息线程本地变量
     */
    ThreadLocal<DbBaseInfo> dbBaseInfoThreadLocal = new ThreadLocal<>();

    /**
     * 数据库导出配置线程本地变量
     */
    ThreadLocal<DbExportConfig> dbExportConfigThreadLocal = new ThreadLocal<>();

    /**
     * 生成文件
     *
     * @param dataSource     数据源
     * @param dbExportConfig 导出配置
     * @param tableList      表数据
     * @return 返回生成的文件
     * @throws Exception 抛出异常
     */
    File makeFile(DataSource dataSource, DbExportConfig dbExportConfig, List<DbTable> tableList) throws Exception;
}
