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
    /**
     * 数据库基本信息线程本地变量
     */
    ThreadLocal<DbBaseInfo> dbBaseInfoThreadLocal = new ThreadLocal<>();

    /**
     * 数据源线程本地变量
     */
    ThreadLocal<DataSource> dataSourceThreadLocal = new ThreadLocal<>();

    /**
     * 导出配置线程本地变量
     */
    ThreadLocal<DbExportConfig> dbExportConfigThreadLocal = new ThreadLocal<>();

    /**
     * 获取数据库表的详细信息(自定义数据源)
     *
     * @param dataSource     数据源
     * @param dbExportConfig 导出配置
     * @return 数据库表详细信息列表
     */
    List<DbTable> getTableDetailInfo(DataSource dataSource, DbExportConfig dbExportConfig);

    /**
     * 获取所有表信息(表名+备注)(自定义数据源)
     *
     * @param dataSource     数据源
     * @param dbExportConfig 导出配置
     * @return 数据库表列表
     */
    List<DbTable> getTableList(DataSource dataSource, DbExportConfig dbExportConfig);
}
