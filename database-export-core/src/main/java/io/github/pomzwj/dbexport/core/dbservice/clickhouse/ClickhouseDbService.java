package io.github.pomzwj.dbexport.core.dbservice.clickhouse;

import io.github.pomzwj.dbexport.core.dbservice.AbstractDbService;
import io.github.pomzwj.dbexport.core.domain.*;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import io.github.pomzwj.dbexport.core.utils.FileReaderUtils;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * clickhouse数据库支持服务
 *
 * @author PomZWJ
 */
public class ClickhouseDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(ClickhouseDbService.class);

    private ClickhouseDbService(){

    }

    private static volatile ClickhouseDbService instance;

    /**
     * 获取ClickhouseDbService单例实例
     *
     * @return ClickhouseDbService单例实例
     */
    public static ClickhouseDbService getInstance() {
        if(instance == null)
            synchronized(ClickhouseDbService.class) {
                if (instance == null)
                    instance = new ClickhouseDbService();
            }
        return instance;
    }

    /**
     * 获取查询表信息的sql
     *
     * @return 查询表信息的sql,获取表名和备注
     */
    @Override
    public List<DbTable> getQueryTableInfoSql() {
        String sql = FileReaderUtils.getSqlFile("sql/clickhouse/query-table.sql");
        List<Map<String, Object>> resultList = this.queryListMapDataBase(dataSourceThreadLocal.get(),sql,dbBaseInfoThreadLocal.get().getDbName());
        return this.getTableNameAndComments(resultList);
    }

    /**
     * 查询表详细信息的sql位置
     *
     * @param dbBaseInfo 数据源基本信息
     * @param tableName  表名
     * @return 查询表详细信息的sql
     */
    @Override
    public String getQueryTableDetailSql(DbBaseInfo dbBaseInfo,String tableName) {
        String sql = FileReaderUtils.getSqlFile("sql/clickhouse/query-table-detail.sql");
        return String.format(sql,tableName,dbBaseInfo.getDbName());
    }

    /**
     * 获取索引SQL
     *
     * @param dbBaseInfo 数据源基本信息
     * @param tableName  表名
     * @return 获取索引SQL
     */
    @Override
    public String getIndexSql(DbBaseInfo dbBaseInfo,String tableName) {
        String sql = FileReaderUtils.getSqlFile("sql/clickhouse/query-index.sql");
        return String.format(sql,dbBaseInfo.getDbName(),tableName
                ,dbBaseInfo.getDbName(),tableName
                ,dbBaseInfo.getDbName(),tableName
                ,dbBaseInfo.getDbName(),tableName);
    }

    /**
     * 解析表字段
     *
     * @param resultList  结果集
     * @param dbColumBean 解析对象
     * @return 解析表字段
     */
    @Override
    public List<DbColumnInfo> setColumnDataInfo(List<Map<String, Object>> resultList,Class<? extends DbColumnInfo> dbColumBean) {
        List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (Map<String, Object> resultSet : resultList) {
                ClickhouseColumnInfo dbColumnInfo = new ClickhouseColumnInfo();
                dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "name"));
                dbColumnInfo.setDataType(this.getDataType(MapUtils.getString(resultSet, "type")));
                dbColumnInfo.setNullAble(this.isNullable(MapUtils.getString(resultSet, "type")));
                dbColumnInfo.setDefaultVal(
                        this.getDefaultValue(MapUtils.getString(resultSet, "default_kind", ""),
                                MapUtils.getString(resultSet, "default_expression", "")));
                String comments = MapUtils.getString(resultSet, "comment");
                if (StringUtils.isEmpty(comments)) {
                    dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                } else {
                    dbColumnInfo.setComments(StringUtils.trimLineBreak(comments));
                }
                dbColumnInfos.add(ClassUtils.copyDbColumnTarget(dbColumBean,dbColumnInfo));
            }
        }
        return dbColumnInfos;
    }

    /**
     * 解析表索引
     *
     * @param resultList  结果集
     * @param dbIndexBean 解析对象
     * @return 解析表索引
     */
    @Override
    public List<DbIndexInfo> setIndexDataInfo(List<Map<String, Object>> resultList, Class<? extends DbIndexInfo> dbIndexBean) {
        List<DbIndexInfo> dbIndexInfos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(resultList)) {
            for (Map<String, Object> resultSet : resultList) {
                ClickHouseIndexInfo clickHouseIndexInfo = new ClickHouseIndexInfo();
                clickHouseIndexInfo.setFields(MapUtils.getString(resultSet, "fields"));
                clickHouseIndexInfo.setType(MapUtils.getString(resultSet, "type"));
                dbIndexInfos.add(ClassUtils.copyDbIndexTarget(dbIndexBean,clickHouseIndexInfo));
            }
        }
        return dbIndexInfos;
    }


    private boolean isNullable(String var) {
        if (StringUtils.isNotEmpty(var)) {
            if (var.startsWith("Nullable")) {
                return true;
            }
        }
        return false;
    }

    private String getDataType(String var) {
        if (StringUtils.isNotEmpty(var)) {
            if (var.startsWith("Nullable")) {
                return var.substring(9, var.length() - 1);
            } else {
                return var;
            }
        }
        return "";
    }

    private String getDefaultValue(String defaultType, String defaultExpression) {
        if ("DEFAULT".equalsIgnoreCase(defaultType)) {
            return defaultExpression;
        } else {
            return "";
        }
    }
}
