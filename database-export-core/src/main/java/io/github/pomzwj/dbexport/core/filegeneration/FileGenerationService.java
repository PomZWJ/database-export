package io.github.pomzwj.dbexport.core.filegeneration;

import io.github.pomzwj.dbexport.core.domain.DbExportConfig;
import io.github.pomzwj.dbexport.core.domain.DbBaseInfo;
import io.github.pomzwj.dbexport.core.domain.DbTable;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;

public interface FileGenerationService {
    ThreadLocal<DataSource> dataSourdceInfoThreadLocal = new ThreadLocal<>();
    ThreadLocal<DbBaseInfo> dbBaseInfoThreadLocal = new ThreadLocal<>();
    ThreadLocal<DbExportConfig> dbExportConfigThreadLocal = new ThreadLocal<>();
    /**
     * 生成文件
     * @param dataSource
     * @param dbExportConfig
     * @param tableList
     * @return
     * @throws Exception
     */
    File makeFile(DataSource dataSource, DbExportConfig dbExportConfig, List<DbTable> tableList) throws Exception;

}
