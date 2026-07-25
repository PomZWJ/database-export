package io.github.pomzwj.dbexport.core.dbservice.oracle;

import io.github.pomzwj.dbexport.core.dbservice.AbstractDbService;
import io.github.pomzwj.dbexport.core.domain.*;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import io.github.pomzwj.dbexport.core.utils.FileReaderUtils;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * oracle数据库操作服务
 *
 * @author PomZWJ
 */
public class OracleDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(OracleDbService.class);

    private OracleDbService(){

    }

    private static volatile OracleDbService instance;

    /**
     * 获取OracleDbService单例实例
     *
     * @return OracleDbService单例实例
     */
    public static OracleDbService getInstance() {
        if(instance == null)
            synchronized(OracleDbService.class) {
                if (instance == null)
                    instance = new OracleDbService();
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
        String sql = FileReaderUtils.getSqlFile("sql/oracle/query-table.sql");
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
        String sql = FileReaderUtils.getSqlFile("sql/oracle/query-table-detail.sql");
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
        String sql = FileReaderUtils.getSqlFile("sql/oracle/query-index.sql");
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
                OracleColumnInfo dbColumnInfo = new OracleColumnInfo();
                dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                dbColumnInfo.setDataType(MapUtils.getString(resultSet, "DATA_TYPE"));
                String dataLength = MapUtils.getString(resultSet, "DATA_LENGTH");
                String dataPrecision = MapUtils.getString(resultSet, "DATA_PRECISION");
                if (StringUtils.isNotEmpty(dataPrecision)) {
                    dataLength = dataPrecision;
                }
                dbColumnInfo.setDataLength(dataLength);
                dbColumnInfo.setDataScale(MapUtils.getString(resultSet, "DATA_SCALE"));
                dbColumnInfo.setNullAble(getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                dbColumnInfo.setDefaultVal(MapUtils.getString(resultSet, "DATA_DEFAULT"));
                String comments = MapUtils.getString(resultSet, "COMMENTS");
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
                OracleIndexInfo oracleIndexInfo = new OracleIndexInfo();
                oracleIndexInfo.setName(MapUtils.getString(resultSet, "INDEX_NAME"));
                oracleIndexInfo.setFields(MapUtils.getString(resultSet, "COLUMN_NAME"));
                oracleIndexInfo.setSeqIndex(MapUtils.getInteger(resultSet, "COLUMN_POSITION"));
                String nonUnique = MapUtils.getString(resultSet, "UNIQUENESS");
                if(StringUtils.isNotBlank(nonUnique) && nonUnique.equals("UNIQUE")){
                    oracleIndexInfo.setUnique(true);
                }else{
                    oracleIndexInfo.setUnique(false);
                }
                String indexType = MapUtils.getString(resultSet, "INDEX_TYPE");
                String constraintType = MapUtils.getString(resultSet, "CONSTRAINT_TYPE");
                if(StringUtils.isNotEmpty(constraintType) && constraintType.equalsIgnoreCase("P")){
                    oracleIndexInfo.setType("主键索引");
                }else{
                    oracleIndexInfo.setType(indexType);
                }

                dbIndexInfos.add(ClassUtils.copyDbIndexTarget(dbIndexBean,oracleIndexInfo));
            }
        }
        return dbIndexInfos;
    }


    private static boolean getStringToBoolean(final String val) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(val)) {
            return false;
        } else {
            if ("Y".equals(val)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
