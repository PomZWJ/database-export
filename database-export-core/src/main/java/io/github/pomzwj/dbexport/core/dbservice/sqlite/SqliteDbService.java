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
 * sqlite数据库支持
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public class SqliteDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(SqliteDbService.class);

    private SqliteDbService(){

    }

    private static volatile SqliteDbService siglegle;

    public static SqliteDbService getInstance() {
        if(siglegle == null)
            synchronized(SqliteDbService.class) {
                if (siglegle == null)
                    siglegle = new SqliteDbService();
            }
        return siglegle;
    }

    @Override
    public List<DbTable> getQueryTableInfoSql() {
        String sql = FileReaderUtils.getSqlFile("sql/sqlite/query-table.sql");
        List<Map<String, Object>> resultList = this.queryListMapDataBase(dataSourceThreadLocal.get(),sql);
        return this.getTableNameAndComments(resultList);
    }

    @Override
    public String getQueryTableDetailSql(DbBaseInfo dbBaseInfo,String tableName) {
        return String.format(FileReaderUtils.getSqlFile("sql/sqlite/query-table-detail.sql"),tableName);
    }

    @Override
    public String getIndexSql(DbBaseInfo dbBaseInfo,String tableName) {
        return String.format(FileReaderUtils.getSqlFile("sql/sqlite/query-index.sql"),tableName);
    }

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
