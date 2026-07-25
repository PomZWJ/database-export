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
 * mysql数据库操作服务
 *
 * @author PomZWJ
 */
public class MySqlDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(MySqlDbService.class);

    private MySqlDbService(){

    }

    private static volatile MySqlDbService instance;

    /**
     * 获取MySqlDbService单例实例
     *
     * @return MySqlDbService单例实例
     */
    public static MySqlDbService getInstance() {
        if(instance == null)
            synchronized(MySqlDbService.class) {
                if (instance == null)
                    instance = new MySqlDbService();
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
        String sql = FileReaderUtils.getSqlFile("sql/mysql/query-table.sql");
        DbBaseInfo dbBaseInfo = dbBaseInfoThreadLocal.get();
        DataSource dataSource = dataSourceThreadLocal.get();
        List<Map<String, Object>> resultList = this.queryListMapDataBase(dataSource,sql.toString(), dbBaseInfo.getDbName());
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
        String sql = FileReaderUtils.getSqlFile("sql/mysql/query-table-detail.sql");
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
        String sql = FileReaderUtils.getSqlFile("sql/mysql/query-index.sql");
        return String.format(sql,dbBaseInfo.getDbName(),tableName);
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
                MySqlColumnInfo mySqlColumnInfo = new MySqlColumnInfo();
                mySqlColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                mySqlColumnInfo.setDataType(MapUtils.getString(resultSet, "COLUMN_TYPE"));
                mySqlColumnInfo.setNullAble(getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                mySqlColumnInfo.setDefaultVal(MapUtils.getString(resultSet, "DATA_DEFAULT",""));
                mySqlColumnInfo.setAutoIncrement(false);
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
                mySqlColumnInfo.setPrimary(!StringUtils.isEmpty(columnKey) && columnKey.contains("PRI"));
                dbColumnInfos.add(ClassUtils.copyDbColumnTarget(dbColumBean,mySqlColumnInfo));
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
