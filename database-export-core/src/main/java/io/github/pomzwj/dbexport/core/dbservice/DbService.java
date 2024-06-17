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
 */
public interface DbService {
    ThreadLocal<DbBaseInfo> dbBaseInfoThreadLocal = new ThreadLocal<>();
    ThreadLocal<DataSource> dataSourceThreadLocal = new ThreadLocal<>();
    ThreadLocal<DbExportConfig> dbExportConfigThreadLocal = new ThreadLocal<>();

    /**
     * 获取数据库表的详细信息(自定义数据源)
     *
     * @param dataSource     数据源
     * @param dbExportConfig 导出配置
     * @return List
     */
    List<DbTable> getTableDetailInfo(DataSource dataSource, DbExportConfig dbExportConfig);

    /**
     * 获取所有表信息(表名+备注)(自定义数据源)
     *
     * @param dataSource     数据源
     * @param dbExportConfig 导出配置
     * @return List
     */
    List<DbTable> getTableList(DataSource dataSource, DbExportConfig dbExportConfig);
}
