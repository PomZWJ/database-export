package io.github.pomzwj.dbexport.core.dbservice.db2;

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

public class Db2DbService extends AbstractDbService {
    private Db2DbService() {

    }

    private static volatile Db2DbService siglegle;

    public static Db2DbService getInstance() {
        if (siglegle == null)
            synchronized (Db2DbService.class) {
                if (siglegle == null)
                    siglegle = new Db2DbService();
            }
        return siglegle;
    }

    @Override
    public List<DbTable> getQueryTableInfoSql() {
        String sql = FileReaderUtils.getSqlFile("sql/db2/query-table.sql");
        DbBaseInfo dbBaseInfo = dbBaseInfoThreadLocal.get();
        String schemas = dbBaseInfo.getSchemas();
        if(!schemas.equalsIgnoreCase("CURRENT SCHEMA")){
            schemas = "'"+schemas+"'";
        }
        List<Map<String, Object>> resultList = this.queryListMapDataBase(dataSourceThreadLocal.get(),String.format(sql,schemas));
        return this.getTableNameAndComments(resultList);
    }

    @Override
    public String getQueryTableDetailSql(DbBaseInfo dbBaseInfo,String tableName) {
        String schemas = dbBaseInfo.getSchemas();
        if(!schemas.equalsIgnoreCase("CURRENT SCHEMA")){
            schemas = "'"+schemas+"'";
        }
        String sql = FileReaderUtils.getSqlFile("sql/db2/query-table-detail.sql");
        return String.format(sql,schemas,tableName);
    }

    @Override
    public String getIndexSql(DbBaseInfo dbBaseInfo,String tableName) {
        String schemas = dbBaseInfo.getSchemas();
        if(!schemas.equalsIgnoreCase("CURRENT SCHEMA")){
            schemas = "'"+schemas+"'";
        }
        String sql = FileReaderUtils.getSqlFile("sql/db2/query-index.sql");
        return String.format(sql,schemas,tableName);
    }

    @Override
    public List<DbColumnInfo> setColumnDataInfo(List<Map<String, Object>> resultList,Class<? extends DbColumnInfo> dbColumBean) {
        List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (Map<String, Object> resultSet : resultList) {
                Db2ColumnInfo dbColumnInfo = new Db2ColumnInfo();
                dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                dbColumnInfo.setDataType(MapUtils.getString(resultSet, "DATA_TYPE"));
                dbColumnInfo.setDataLength(MapUtils.getString(resultSet, "DATA_LENGTH"));
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

    @Override
    public List<DbIndexInfo> setIndexDataInfo(List<Map<String, Object>> resultList, Class<? extends DbIndexInfo> dbIndexBean) {
        List<DbIndexInfo> dbIndexInfos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(resultList)) {
            for (Map<String, Object> resultSet : resultList) {
                Db2IndexInfo indexInfo = new Db2IndexInfo();
                indexInfo.setName(MapUtils.getString(resultSet, "INDEX_NAME"));
                indexInfo.setFields(getIndexColumn(MapUtils.getString(resultSet, "COLUMN_NAME")));
                indexInfo.setType(getIndexType(MapUtils.getString(resultSet, "INDEX_TYPE")));
                indexInfo.setComments(StringUtils.trimLineBreak(MapUtils.getString(resultSet, "INDEX_COMMENT")));
                dbIndexInfos.add(ClassUtils.copyDbIndexTarget(dbIndexBean,indexInfo));
            }
        }
        return dbIndexInfos;
    }


    private static boolean getStringToBoolean(final String val) {
        if (StringUtils.isEmpty(val)) {
            return false;
        } else {
            if ("Y".equals(val)) {
                return true;
            } else {
                return false;
            }
        }
    }
    private static String getIndexType(final String val) {
        if (StringUtils.isEmpty(val)) {
            return "";
        } else {
            if ("P".equalsIgnoreCase(val)) {
                return "P(主键索引)";
            } else if("U".equalsIgnoreCase(val)) {
                return "U(唯一索引)";
            } else if("D".equalsIgnoreCase(val)){
                return "D(重复索引)";
            }
        }
        return "";
    }
    private static String getIndexColumn(String val) {
        if (StringUtils.isEmpty(val)) {
            return "";
        } else {
            return val.replaceAll("\\+"," ").trim().replaceAll(" ",",");
        }
    }
}
