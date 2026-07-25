package io.github.pomzwj.dbexport.core.dbservice.sqlserver;

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
 * sqlserver数据库操作服务
 *
 * @author PomZWJ
 */
public class SqlServerDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(SqlServerDbService.class);

    private SqlServerDbService(){

    }

    private static volatile SqlServerDbService instance;

    /**
     * 获取SqlServerDbService单例实例
     *
     * @return SqlServerDbService单例实例
     */
    public static SqlServerDbService getInstance() {
        if(instance == null)
            synchronized(SqlServerDbService.class) {
                if (instance == null)
                    instance = new SqlServerDbService();
            }
        return instance;
    }

    /**
     * 获取查询表信息的sql
     *
     * @return 查询表信息的sql,获取表名和备注
     */
    @Override
    public List<DbTable> getQueryTableInfoSql()  {
        String sql = FileReaderUtils.getSqlFile("sql/sqlserver/query-table.sql");
        List<Map<String, Object>> resultList = this.queryListMapDataBase(dataSourceThreadLocal.get(),sql);
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
        String sql = FileReaderUtils.getSqlFile("sql/sqlserver/query-table-detail.sql");
        return String.format(sql,tableName);
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
        String sql = FileReaderUtils.getSqlFile("sql/sqlserver/query-index.sql");
        return String.format(sql,tableName);
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
                SqlServerColumnInfo dbColumnInfo = new SqlServerColumnInfo();
                dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                dbColumnInfo.setDataType(MapUtils.getString(resultSet, "COLUMN_TYPE"));
                dbColumnInfo.setDataLength(MapUtils.getString(resultSet, "DATA_LENGTH"));
                dbColumnInfo.setNullAble(getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                dbColumnInfo.setDefaultVal(MapUtils.getString(resultSet, "DATA_DEFAULT"));
                dbColumnInfo.setDataScale(MapUtils.getString(resultSet, "DATA_SCALE"));
                String comments = MapUtils.getString(resultSet, "COMMENTS");
                dbColumnInfo.setAutoIncrement(getStringToBoolean(MapUtils.getString(resultSet, "AUTOINCREMENT")));
                dbColumnInfo.setPrimary(getStringToBoolean(MapUtils.getString(resultSet, "PRIMARY_KEY")));
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
                SqlServerIndexInfo sqlServerIndexInfo = new SqlServerIndexInfo();
                sqlServerIndexInfo.setName(MapUtils.getString(resultSet, "index_name"));
                sqlServerIndexInfo.setFields(MapUtils.getString(resultSet, "column_name"));
                sqlServerIndexInfo.setIndexId(MapUtils.getInteger(resultSet, "index_id"));
                sqlServerIndexInfo.setIndexType(MapUtils.getString(resultSet,"index_type"));
                Integer isDescending = MapUtils.getInteger(resultSet, "is_descending");
                sqlServerIndexInfo.setDescending(isDescending != null && isDescending.equals(1));
                dbIndexInfos.add(ClassUtils.copyDbIndexTarget(dbIndexBean,sqlServerIndexInfo));
            }
        }
        return dbIndexInfos;
    }

    private static boolean getStringToBoolean(final String val) {
        if (StringUtils.isEmpty(val)) {
            return false;
        } else {
            if ("TRUE".equals(val)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
