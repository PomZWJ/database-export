package io.github.pomzwj.dbexport.core.dbservice.sqlite;

import io.github.pomzwj.dbexport.core.domain.*;
import io.github.pomzwj.dbexport.core.dbservice.AbstractDbService;
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
 * sqlite数据库支持服务
 *
 * @author PomZWJ
 */
public class SqliteDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(SqliteDbService.class);

    private SqliteDbService(){

    }

    private static volatile SqliteDbService instance;

    /**
     * 获取SqliteDbService单例实例
     *
     * @return SqliteDbService单例实例
     */
    public static SqliteDbService getInstance() {
        if(instance == null)
            synchronized(SqliteDbService.class) {
                if (instance == null)
                    instance = new SqliteDbService();
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
        String sql = FileReaderUtils.getSqlFile("sql/sqlite/query-table.sql");
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
        return String.format(FileReaderUtils.getSqlFile("sql/sqlite/query-table-detail.sql"),tableName);
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
        return String.format(FileReaderUtils.getSqlFile("sql/sqlite/query-index.sql"),tableName);
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
                SqliteColumnInfo dbColumnInfo = new SqliteColumnInfo();
                dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                dbColumnInfo.setDataType(MapUtils.getString(resultSet, "DATA_TYPE"));
                dbColumnInfo.setNullAble(!getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                dbColumnInfo.setDefaultVal(MapUtils.getString(resultSet, "DATA_DEFAULT",""));
                dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                String isPrimary = MapUtils.getString(resultSet, "ISPK");
                dbColumnInfo.setPrimary(getStringToBoolean(isPrimary));
                dbColumnInfos.add(dbColumnInfo);
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
                SqliteIndexInfo sqliteIndexInfo = new SqliteIndexInfo();
                sqliteIndexInfo.setName(MapUtils.getString(resultSet, "name"));

                String sql = MapUtils.getString(resultSet, "sql");
                String sqlUpperCase = sql.toUpperCase();
                if(sqlUpperCase.contains("UNIQUE")){
                    sqliteIndexInfo.setUnique(true);
                }else{
                    sqliteIndexInfo.setUnique(false);
                }
                sqliteIndexInfo.setFields(sql.split("\\(")[1].split("\\)")[0]);
                sqliteIndexInfo.setType(MapUtils.getString(resultSet, "INDEX_TYPE","其他"));
                sqliteIndexInfo.setComments(StringUtils.trimLineBreak(MapUtils.getString(resultSet, "INDEX_COMMENT","")));
                dbIndexInfos.add(sqliteIndexInfo);
            }
        }
        return dbIndexInfos;
    }

    private static boolean getStringToBoolean(final String val) {
        if (StringUtils.isEmpty(val)) {
            return false;
        } else {
            if ("1".equals(val)) {
                return true;
            } else {
                return false;
            }
        }
    }


}
