package io.github.pomzwj.dbexport.core;

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


    public static List<DbTable> executeGetTableAndComments(DataSource dataSource, DbExportConfig dbExportConfig) {
        dbExportConfig.checkAndInitConfig(dataSource);
        DbService dbServiceBean = dbServiceFactory.getDbServiceBean(dataSource);
        List<DbTable> tableList = dbServiceBean.getTableList(dataSource, dbExportConfig);
        return tableList;
    }

    public static List<DbTable> executeGetTableDataAll(DataSource dataSource, DbExportConfig dbExportConfig) {
        dbExportConfig.checkAndInitConfig(dataSource);
        DbService dbServiceBean = dbServiceFactory.getDbServiceBean(dataSource);
        List<DbTable> tableList = dbServiceBean.getTableDetailInfo(dataSource, dbExportConfig);
        return tableList;
    }


    public static String executeFile(DataSource dataSource, DbExportConfig dbExportConfig) throws Exception {
        dbExportConfig.checkAndInitConfig(dataSource);
        dbExportConfig.checkOutputConfig();
        DbService dbServiceBean = dbServiceFactory.getDbServiceBean(dataSource);
        List<DbTable> tableList = dbServiceBean.getTableDetailInfo(dataSource, dbExportConfig);
        FileGenerationService fileGenerationBean = fileGenerationFactory.getFileGenerationBean(dbExportConfig.getExportFileTypeEnum());
        File file = fileGenerationBean.makeFile(dataSource, dbExportConfig, tableList);
        return file.getAbsolutePath();

    }


}
