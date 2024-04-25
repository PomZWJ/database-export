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
 * clickhouse数据库支持
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public class ClickhouseDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(ClickhouseDbService.class);

    private ClickhouseDbService(){

    }

    private static volatile ClickhouseDbService siglegle;

    public static ClickhouseDbService getInstance() {
        if(siglegle == null)
            synchronized(ClickhouseDbService.class) {
                if (siglegle == null)
                    siglegle = new ClickhouseDbService();
            }
        return siglegle;
    }

    @Override
    public List<DbTable> getQueryTableInfoSql() {
        String sql = FileReaderUtils.getSqlFile("sql/clickhouse/query-table.sql");
        List<Map<String, Object>> resultList = this.queryListMapDataBase(dataSourceThreadLocal.get(),sql,dbBaseInfoThreadLocal.get().getDbName());
        return this.getTableNameAndComments(resultList);
    }

    @Override
    public String getQueryTableDetailSql(DbBaseInfo dbBaseInfo,String tableName) {
        String sql = FileReaderUtils.getSqlFile("sql/clickhouse/query-table-detail.sql");
        return String.format(sql,tableName,dbBaseInfo.getDbName());
    }

    @Override
    public String getIndexSql(DbBaseInfo dbBaseInfo,String tableName) {
        String sql = FileReaderUtils.getSqlFile("sql/clickhouse/query-index.sql");
        System.out.println(sql);
        return String.format(sql,dbBaseInfo.getDbName(),tableName
                ,dbBaseInfo.getDbName(),tableName
                ,dbBaseInfo.getDbName(),tableName
                ,dbBaseInfo.getDbName(),tableName);
    }

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
