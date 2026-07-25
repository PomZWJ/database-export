package io.github.pomzwj.dbexport.core.dbservice.postgresql;

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
 * postgresql数据库支持服务
 *
 * @author PomZWJ
 */
public class PostgresqlDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(PostgresqlDbService.class);

    private PostgresqlDbService(){

    }

    private static volatile PostgresqlDbService instance;

    /**
     * 获取PostgresqlDbService单例实例
     *
     * @return PostgresqlDbService单例实例
     */
    public static PostgresqlDbService getInstance() {
        if(instance == null)
            synchronized(PostgresqlDbService.class) {
                if (instance == null)
                    instance = new PostgresqlDbService();
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
        DbBaseInfo dbBaseInfo = dbBaseInfoThreadLocal.get();
        String sql = FileReaderUtils.getSqlFile("sql/postgresql/query-table.sql");
        List<Map<String, Object>> resultList = this.queryListMapDataBase(dataSourceThreadLocal.get(),sql,StringUtils.isEmpty(dbBaseInfo.getSchemas())?"public":dbBaseInfo.getSchemas());
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
        String sql = FileReaderUtils.getSqlFile("sql/postgresql/query-table-detail.sql");
        String schemas = dbBaseInfo.getSchemas();
        String formatSql = String.format(sql, tableName, dbBaseInfo.getDbName(), tableName,schemas, tableName,schemas);
        return formatSql;
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
        String sql = FileReaderUtils.getSqlFile("sql/postgresql/query-index.sql");
        return String.format(sql,tableName,dbBaseInfo.getSchemas());
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
                PostgresqlColumnInfo dbColumnInfo = new PostgresqlColumnInfo();
                dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                dbColumnInfo.setDataType(MapUtils.getString(resultSet, "DATA_TYPE"));
                dbColumnInfo.setNullAble(getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                dbColumnInfo.setDefaultVal(MapUtils.getString(resultSet, "DATA_DEFAULT"));
                dbColumnInfo.setPrimary(false);
                String comments = MapUtils.getString(resultSet, "COMMENTS");
                if (StringUtils.isEmpty(comments)) {
                    dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                } else {
                    dbColumnInfo.setComments(StringUtils.trimLineBreak(comments));
                }
                String isPrimary = MapUtils.getString(resultSet, "IS_PRIMARY");
                dbColumnInfo.setPrimary(getIsPrimary(isPrimary));
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
                PostgresqlIndexInfo postgresqlIndexInfo = new PostgresqlIndexInfo();
                postgresqlIndexInfo.setName(MapUtils.getString(resultSet, "INDEX_NAME"));
                String columnName = MapUtils.getString(resultSet, "COLUMN_NAME");
                if(StringUtils.isNotEmpty(columnName)){
                    if(columnName.startsWith("{") && columnName.endsWith("}")){
                        postgresqlIndexInfo.setFields(removeSurroundingChars(columnName,'{','}'));
                    }else{
                        postgresqlIndexInfo.setFields(columnName);
                    }
                }
                postgresqlIndexInfo.setType(MapUtils.getString(resultSet, "INDEX_TYPE"));

                String nonUnique = MapUtils.getString(resultSet, "NON_UNIQUE");
                if(nonUnique != null && nonUnique.equalsIgnoreCase("true")){
                    postgresqlIndexInfo.setUnique(true);
                }else{
                    postgresqlIndexInfo.setUnique(false);
                }
                dbIndexInfos.add(ClassUtils.copyDbIndexTarget(dbIndexBean,postgresqlIndexInfo));
            }
        }
        return dbIndexInfos;
    }


    private static String removeSurroundingChars(String str, char startChar, char endChar) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        if (str.startsWith(String.valueOf(startChar))) {
            str = str.substring(1);
        }

        if (str.endsWith(String.valueOf(endChar))) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    private static boolean getStringToBoolean(final String val) {
        if (StringUtils.isEmpty(val)) {
            return false;
        } else {
            if ("YES".equals(val)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static boolean getIsPrimary(final String val) {
        if (StringUtils.isEmpty(val)) {
            return false;
        } else {
            if ("PRIMARY KEY".equalsIgnoreCase(val)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
