package com.pomzwj.dbservice.postgresql;

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
 * postgresql数据库支持
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
@Component
public class PostgresqlDbService extends AbstractDbService {
    static final Logger log = LoggerFactory.getLogger(PostgresqlDbService.class);


    @Override
    public String getQueryTableDetailSql() {
        return "sql/postgresql.sql";
    }

    @Override
    public String getQueryTableInfoSql() {
        return "SELECT relname AS TABLE_NAME,CAST ( obj_description ( relfilenode, 'pg_class' ) AS VARCHAR ) AS COMMENTS FROM pg_class C WHERE relkind = 'r' AND relname NOT LIKE'pg_%' AND relname NOT LIKE'sql_%' AND relchecks = 0 ORDER BY relname";
    }

    @Override
    public void setColumnDataInfo(JdbcTemplate jdbcTemplate, List<DbTable> list, String executeSql, DbBaseInfo dbBaseInfo) {
        String dbName = dbBaseInfo.getDbName();
        for (int j = 0; j < list.size(); j++) {
            DbTable dbTable = list.get(j);
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(executeSql, dbTable.getTableName(), dbName, dbTable.getTableName());
            List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (Map<String, Object> resultSet : resultList) {
                    DbColumnInfo dbColumnInfo = new DbColumnInfo();
                    dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                    dbColumnInfo.setDataType(MapUtils.getString(resultSet, "DATA_TYPE"));
                    dbColumnInfo.setNullAble(getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                    dbColumnInfo.setDefaultVal(MapUtils.getString(resultSet, "DATA_DEFAULT"));
                    dbColumnInfo.setPrimary(false);
                    String comments = MapUtils.getString(resultSet, "COMMENTS");
                    if (StringUtils.isEmpty(comments)) {
                        dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                    } else {
                        dbColumnInfo.setComments(comments);
                    }
                    String isPrimary = MapUtils.getString(resultSet, "IS_PRIMARY");
                    dbColumnInfo.setPrimary(getIsPrimary(isPrimary));
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

    private static boolean getIsPrimary(final String val) {
        if (StringUtils.isEmpty(val)) {
            return false;
        } else {
            if ("p".equals(val)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
