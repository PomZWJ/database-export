package com.pomzwj.dbservice.mysql;

import com.pomzwj.dbservice.AbstractDbService;
import com.pomzwj.domain.*;
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
 * mysql数据库操作
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
@Component
public class MySqlDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(MySqlDbService.class);


    @Override
    public String getQueryTableDetailSql() {
        return "sql/mysql.sql";
    }

    @Override
    public String getQueryTableInfoSql() {
        return "select table_name TABLE_NAME, table_comment COMMENTS from information_schema.tables where table_schema='%s' and table_type='BASE TABLE'";
    }

    @Override
    public void setColumnDataInfo(JdbcTemplate jdbcTemplate, List<DbTable> list, String executeSql, DbBaseInfo dbBaseInfo) {
        for (int j = 0; j < list.size(); j++) {
            DbTable dbTable = list.get(j);
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(executeSql, dbTable.getTableName());
            List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (Map<String, Object> resultSet : resultList) {
                    DbColumnInfo dbColumnInfo = new DbColumnInfo();
                    dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                    dbColumnInfo.setDataType(MapUtils.getString(resultSet, "COLUMN_TYPE"));
                    dbColumnInfo.setNullAble(getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                    dbColumnInfo.setDefaultVal(MapUtils.getString(resultSet, "DATA_DEFAULT"));
                    dbColumnInfo.setAutoIncrement(false);
                    dbColumnInfo.setPrimary(false);
                    String comments = MapUtils.getString(resultSet, "COMMENTS");
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
}
