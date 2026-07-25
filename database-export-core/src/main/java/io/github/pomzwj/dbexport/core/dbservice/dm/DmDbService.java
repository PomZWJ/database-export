package io.github.pomzwj.dbexport.core.dbservice.dm;

import io.github.pomzwj.dbexport.core.dbservice.AbstractDbService;
import io.github.pomzwj.dbexport.core.domain.*;
import io.github.pomzwj.dbexport.core.utils.ClassUtils;
import io.github.pomzwj.dbexport.core.utils.FileReaderUtils;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * dm数据库操作服务
 *
 * @author PomZWJ
 */
public class DmDbService extends AbstractDbService {

    private DmDbService(){

    }

    private static volatile DmDbService instance;

    /**
     * 获取DmDbService单例实例
     *
     * @return DmDbService单例实例
     */
    public static DmDbService getInstance() {
        if(instance == null)
            synchronized(DmDbService.class) {
                if (instance == null)
                    instance = new DmDbService();
            }
        return instance;
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
        String sql = FileReaderUtils.getSqlFile("sql/dm/query-table-detail.sql");
        return String.format(sql,dbBaseInfo.getDbName(),dbBaseInfo.getDbName(),tableName);
    }

    /**
     * 获取查询表信息的sql
     *
     * @return 查询表信息的sql,获取表名和备注
     */
    @Override
    public List<DbTable> getQueryTableInfoSql(){
        String sql = FileReaderUtils.getSqlFile("sql/dm/query-table.sql");
        List<Map<String, Object>> resultList = this.queryListMapDataBase(dataSourceThreadLocal.get(),sql.toString(),dbBaseInfoThreadLocal.get().getDbName());
        return this.getTableNameAndComments(resultList);
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
        String sql = FileReaderUtils.getSqlFile("sql/dm/query-index.sql");
        return String.format(sql,dbBaseInfo.getDbName(),tableName,dbBaseInfo.getDbName(),tableName);
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
                DmIndexInfo dmIndexInfo = new DmIndexInfo();
                dmIndexInfo.setName(MapUtils.getString(resultSet, "INDEX_NAME"));
                dmIndexInfo.setFields(MapUtils.getString(resultSet, "COLUMN_NAME"));
                dmIndexInfo.setType(MapUtils.getString(resultSet, "INDEX_TYPE"));
                dmIndexInfo.setSeqIndex(MapUtils.getInteger(resultSet, "COLUMN_POSITION"));
                String nonUnique = MapUtils.getString(resultSet, "UNIQUENESS");
                if(StringUtils.isNotBlank(nonUnique) && nonUnique.equals("UNIQUE")){
                    dmIndexInfo.setUnique(true);
                }else{
                    dmIndexInfo.setUnique(false);
                }
                dbIndexInfos.add(ClassUtils.copyDbIndexTarget(dbIndexBean,dmIndexInfo));
            }
        }
        return dbIndexInfos;
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
                DmDbColumnInfo dbColumnInfo = new DmDbColumnInfo();
                dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                dbColumnInfo.setDataType(MapUtils.getString(resultSet, "DATA_TYPE"));
                dbColumnInfo.setDataLength(MapUtils.getString(resultSet, "DATA_LENGTH"));
                dbColumnInfo.setDataScale(MapUtils.getString(resultSet, "DATA_SCALE"));
                dbColumnInfo.setNullAble(getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                dbColumnInfo.setPrimary(getStringToBoolean(MapUtils.getString(resultSet, "IS_PRIMARY_KEY")));
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



    private static boolean getStringToBoolean(final String val) {
        if (StringUtils.isEmpty(val)) {
            return false;
        } else {
            if ("Y".equalsIgnoreCase(val) || "YES".equalsIgnoreCase(val)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
