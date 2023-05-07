package com.pomzwj.dbservice.clickhouse;

import com.pomzwj.dbservice.AbstractDbService;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.FiledDefaultValue;
import com.pomzwj.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
@Component
public class ClickhouseDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(ClickhouseDbService.class);
    final static String queryTableDetailSql = "sql/postgresql.sql";
    @Value("${database.getTableNameSql.clickhouse}")
    String clickhouseGetTableNameSql;

    @Override
    public String getQueryTableDetailSql() {
        return queryTableDetailSql;
    }

    @Override
    public List<DbTable> getTableName(JdbcTemplate jdbcTemplate, DbBaseInfo dbBaseInfo) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(String.format(clickhouseGetTableNameSql, dbBaseInfo.getDbName()));
        List<DbTable> tableList = this.getTableNameAndComments(resultList);
        return tableList;
    }

    @Override
    public void setColumnDataInfo(JdbcTemplate jdbcTemplate, List<DbTable> list, String executeSql, DbBaseInfo dbBaseInfo) {
        String dbName = dbBaseInfo.getDbName();
        for (int j = 0; j < list.size(); j++) {
            DbTable dbTable = list.get(j);
            //因为有的没有comment,这边是做个错误判断
            try {
                Map<String, Object> engineAndCommentMap = jdbcTemplate.queryForMap(String.format("select comment, engine from system.tables WHERE database = '%s' and name = '%s'", dbName, dbTable.getTableName()));
                String comment = MapUtils.getString(engineAndCommentMap, "comment", "");
                String engine = MapUtils.getString(engineAndCommentMap, "engine", "");
                if (StringUtils.isEmpty(comment) && StringUtils.isEmpty(engine)) {
                    dbTable.setTableComments(comment + " engine:" + engine);
                } else if (StringUtils.isNotEmpty(comment) && StringUtils.isEmpty(engine)) {
                    dbTable.setTableComments(comment);
                } else if (StringUtils.isEmpty(comment) && StringUtils.isNotEmpty(engine)) {
                    dbTable.setTableComments("engine:" + engine);
                } else {
                    dbTable.setTableComments("");
                }
            } catch (Exception e) {
                Map<String, Object> engineAndCommentMap = jdbcTemplate.queryForMap(String.format("select '' as comment,engine from system.tables WHERE database = '%s' and name = '%s'", dbName, dbTable.getTableName()));
                String engine = MapUtils.getString(engineAndCommentMap, "engine", "");
                if (StringUtils.isNotEmpty(engine)) {
                    dbTable.setTableComments("engine:" + engine);
                } else {
                    dbTable.setTableComments("");
                }
            }
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList("desc " + dbName + "." + dbTable.getTableName());
            List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (Map<String, Object> resultSet : resultList) {
                    DbColumnInfo dbColumnInfo = new DbColumnInfo();
                    dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "name"));
                    dbColumnInfo.setDataType(this.getDataType(MapUtils.getString(resultSet, "type")));
                    dbColumnInfo.setNullAble(this.isNullable(MapUtils.getString(resultSet, "type")));
                    dbColumnInfo.setDefaultVal(
                            this.getDefaultValue(MapUtils.getString(resultSet, "default_type", ""),
                                    MapUtils.getString(resultSet, "default_expression", "")));
                    //dbColumnInfo.setAutoIncrement(false);
                    //dbColumnInfo.setPrimary(false);
                    String comments = MapUtils.getString(resultSet, "comment");
                    if (StringUtils.isEmpty(comments)) {
                        dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                    } else {
                        dbColumnInfo.setComments(comments);
                    }
                    String extraInfo = MapUtils.getString(resultSet, "EXTRA_INFO");
                    if (!StringUtils.isEmpty(extraInfo) && extraInfo.contains("auto_increment")) {
                        dbColumnInfo.setAutoIncrement(true);
                    }
                    String columnKey = MapUtils.getString(resultSet, "COLUMN_KEY");
                    if (!StringUtils.isEmpty(columnKey) && columnKey.contains("PRI")) {
                        dbColumnInfo.setPrimary(true);
                    }
                    dbColumnInfos.add(dbColumnInfo);
                }
                dbTable.setTabsColumn(dbColumnInfos);
            }
        }
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
