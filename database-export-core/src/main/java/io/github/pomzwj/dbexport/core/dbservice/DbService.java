package io.github.pomzwj.dbexport.core.dbservice;

import io.github.pomzwj.dbexport.core.dbservice.sqlite.SqliteDbService;
import io.github.pomzwj.dbexport.core.domain.DbBaseInfo;
import io.github.pomzwj.dbexport.core.domain.DbExportConfig;
import io.github.pomzwj.dbexport.core.domain.DbTable;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据库服务接口
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public interface DbService {
    String DEFAULT_CHARSET_NAME = "UTF-8";
    ThreadLocal<DbBaseInfo> dbBaseInfoThreadLocal = new ThreadLocal<>();
    ThreadLocal<DataSource> dataSourceThreadLocal = new ThreadLocal<>();
    ThreadLocal<DbExportConfig> dbExportConfigThreadLocal = new ThreadLocal<>();

    /**
     * 获取数据库表的详细信息(自定义数据源)
     *
     * @param dataSource     数据源
     * @param dbExportConfig 导出配置
     * @return List
     * @throws Exception
     */
    List<DbTable> getTableDetailInfo(DataSource dataSource, DbExportConfig dbExportConfig) throws Exception;

    /**
     * 获取所有表信息(表名+备注)(自定义数据源)
     *
     * @param dataSource     数据源
     * @param dbExportConfig 导出配置
     * @return List
     * @throws Exception
     */
    List<DbTable> getTableList(DataSource dataSource, DbExportConfig dbExportConfig);
}
