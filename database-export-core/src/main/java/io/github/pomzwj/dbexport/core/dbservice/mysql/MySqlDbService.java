package io.github.pomzwj.dbexport.core.dbservice.mysql;

import io.github.pomzwj.dbexport.core.dbservice.AbstractDbService;
import io.github.pomzwj.dbexport.core.domain.*;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import io.github.pomzwj.dbexport.core.utils.FileReaderUtils;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * mysql数据库操作
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public class MySqlDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(MySqlDbService.class);

    private MySqlDbService(){

    }

    private static volatile MySqlDbService siglegle;

    public static MySqlDbService getInstance() {
        if(siglegle == null)
            synchronized(MySqlDbService.class) {
                if (siglegle == null)
                    siglegle = new MySqlDbService();
            }
        return siglegle;
    }

    @Override
    public List<DbTable> getQueryTableInfoSql() {
        String sql = FileReaderUtils.getSqlFile("sql/mysql/query-table.sql");
        DbBaseInfo dbBaseInfo = dbBaseInfoThreadLocal.get();
        DataSource dataSource = dataSourceThreadLocal.get();
        List<Map<String, Object>> resultList = this.queryListMapDataBase(dataSource,sql.toString(), dbBaseInfo.getDbName());
        return this.getTableNameAndComments(resultList);
    }

    @Override
    public String getQueryTableDetailSql(DbBaseInfo dbBaseInfo,String tableName) {
        String sql = FileReaderUtils.getSqlFile("sql/mysql/query-table-detail.sql");
        return String.format(sql,tableName);
    }

    @Override
    public String getIndexSql(DbBaseInfo dbBaseInfo,String tableName) {
        String sql = FileReaderUtils.getSqlFile("sql/mysql/query-index.sql");
        return String.format(sql,dbBaseInfo.getDbName(),tableName);
    }

    @Override
    public List<DbColumnInfo> setColumnDataInfo(List<Map<String, Object>> resultList,Class<? extends DbColumnInfo> dbColumBean) {
        List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (Map<String, Object> resultSet : resultList) {
                MySqlColumnInfo mySqlColumnInfo = new MySqlColumnInfo();
                mySqlColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                mySqlColumnInfo.setDataType(MapUtils.getString(resultSet, "COLUMN_TYPE"));
                mySqlColumnInfo.setNullAble(getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                mySqlColumnInfo.setDefaultVal(MapUtils.getString(resultSet, "DATA_DEFAULT",""));
                mySqlColumnInfo.setAutoIncrement(false);
                mySqlColumnInfo.setPrimary(true);
                String comments = MapUtils.getString(resultSet, "COMMENTS");
                if (StringUtils.isEmpty(comments)) {
                    mySqlColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                } else {
                    mySqlColumnInfo.setComments(StringUtils.trimLineBreak(comments));
                }
                String extraInfo = MapUtils.getString(resultSet, "EXTRA_INFO");
                if (!StringUtils.isEmpty(extraInfo) && extraInfo.contains("auto_increment")) {
                    mySqlColumnInfo.setAutoIncrement(true);
                }
                String columnKey = MapUtils.getString(resultSet, "COLUMN_KEY");
                if (!StringUtils.isEmpty(columnKey) && columnKey.contains("PRI")) {
                    mySqlColumnInfo.setPrimary(true);
                }
                dbColumnInfos.add(ClassUtils.copyDbColumnTarget(dbColumBean,mySqlColumnInfo));
            }
        }
        return dbColumnInfos;
    }

    @Override
    public List<DbIndexInfo> setIndexDataInfo(List<Map<String, Object>> resultList,Class<? extends DbIndexInfo> dbIndexBean) {
        List<DbIndexInfo> dbIndexInfos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(resultList)) {
            for (Map<String, Object> resultSet : resultList) {
                MySqlIndexInfo mySqlIndexInfo = new MySqlIndexInfo();
                mySqlIndexInfo.setName(MapUtils.getString(resultSet, "INDEX_NAME"));
                mySqlIndexInfo.setFields(MapUtils.getString(resultSet, "COLUMN_NAME"));
                mySqlIndexInfo.setType(MapUtils.getString(resultSet, "INDEX_TYPE"));
                mySqlIndexInfo.setComments(StringUtils.trimLineBreak(MapUtils.getString(resultSet, "INDEX_COMMENT")));
                mySqlIndexInfo.setSeqIndex(MapUtils.getInteger(resultSet, "SEQ_IN_INDEX"));

                Integer nonUnique = MapUtils.getInteger(resultSet, "NON_UNIQUE");
                if(nonUnique == null || nonUnique == 0){
                    mySqlIndexInfo.setUnique(true);
                }else{
                    mySqlIndexInfo.setUnique(false);
                }
                dbIndexInfos.add(ClassUtils.copyDbIndexTarget(dbIndexBean,mySqlIndexInfo));
            }
        }
        return dbIndexInfos;
    }


    private static Boolean getStringToBoolean(final String val) {
        if (StringUtils.isEmpty(val) || !"YES".equalsIgnoreCase(val)) {
            return false;
        } else {
            return true;
        }
    }
}
