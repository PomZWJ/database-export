package io.github.pomzwj.dbexport.core;

import io.github.pomzwj.dbexport.core.domain.DbBaseInfo;
import io.github.pomzwj.dbexport.core.domain.DbExportConfig;
import io.github.pomzwj.dbexport.core.dbservice.DbService;
import io.github.pomzwj.dbexport.core.dbservice.DbServiceFactory;
import io.github.pomzwj.dbexport.core.domain.DbTable;
import io.github.pomzwj.dbexport.core.filegeneration.FileGenerationFactory;
import io.github.pomzwj.dbexport.core.filegeneration.FileGenerationService;
import io.github.pomzwj.dbexport.core.init.InitDataBaseExportRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 数据库导出执行入口，提供获取表信息及生成导出文件的能力
 *
 * @author PomZWJ
 */
public class DataBaseExportExecute {
    static final Logger log = LoggerFactory.getLogger(DataBaseExportExecute.class);

    static DbServiceFactory dbServiceFactory;
    static FileGenerationFactory fileGenerationFactory;

    static {
        try {
            InitDataBaseExportRunner.init();
            dbServiceFactory = new DbServiceFactory();
            fileGenerationFactory = new FileGenerationFactory();
        } catch (IOException e) {
            log.error("DataBaseExport init fail,reason is {}", e.getMessage(),e);
        }
    }


    /**
     * 执行获取数据库表及注释信息
     *
     * @param dataSource     数据源
     * @param dbExportConfig 数据库导出配置
     * @return 数据库表列表
     */
    public static List<DbTable> executeGetTableAndComments(DataSource dataSource, DbExportConfig dbExportConfig) {
        dbExportConfig.checkAndInitConfig(dataSource);
        DbService dbServiceBean = dbServiceFactory.getDbServiceBean(dbExportConfig.getDbBaseInfo().getDbKindEnum());
        List<DbTable> tableList = dbServiceBean.getTableList(dataSource, dbExportConfig);
        return tableList;
    }

    /**
     * 执行获取数据库表的完整数据信息（含字段、索引等详情）
     *
     * @param dataSource     数据源
     * @param dbExportConfig 数据库导出配置
     * @return 数据库表详情列表
     */
    public static List<DbTable> executeGetTableDataAll(DataSource dataSource, DbExportConfig dbExportConfig) {
        dbExportConfig.checkAndInitConfig(dataSource);
        DbService dbServiceBean = dbServiceFactory.getDbServiceBean(dbExportConfig.getDbBaseInfo().getDbKindEnum());
        List<DbTable> tableList = dbServiceBean.getTableDetailInfo(dataSource, dbExportConfig);
        return tableList;
    }


    /**
     * 执行导出文件生成
     *
     * @param dataSource     数据源
     * @param dbExportConfig 数据库导出配置
     * @return 生成文件的绝对路径
     * @throws Exception 如果生成文件过程中发生异常
     */
    public static String executeFile(DataSource dataSource, DbExportConfig dbExportConfig) throws Exception {
        dbExportConfig.checkAndInitConfig(dataSource);
        dbExportConfig.checkOutputConfig();
        //复用 checkAndInitConfig 中已解析的 dbBaseInfo，避免重复解析
        DbBaseInfo dbBaseInfo = dbExportConfig.getDbBaseInfo();
        DbService dbServiceBean = dbServiceFactory.getDbServiceBean(dbBaseInfo.getDbKindEnum());
        List<DbTable> tableList = dbServiceBean.getTableDetailInfo(dataSource, dbExportConfig);
        FileGenerationService fileGenerationBean = fileGenerationFactory.getFileGenerationBean(dbExportConfig.getExportFileTypeEnum());
        File file = fileGenerationBean.makeFile(dataSource, dbExportConfig, tableList);
        return file.getAbsolutePath();

    }


}
