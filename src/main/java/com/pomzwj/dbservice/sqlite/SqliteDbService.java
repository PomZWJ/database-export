package com.pomzwj.dbservice.sqlite;

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
 * sqlite数据库支持
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
@Component
public class SqliteDbService extends AbstractDbService {

    static final Logger log = LoggerFactory.getLogger(SqliteDbService.class);
    final static String queryTableDetailSql = "sql/sqlite.sql";
    @Value("${database.getTableNameSql.sqlite}")
    String sqliteGetTableNameSql;

    @Override
    public String getQueryTableDetailSql() {
        return queryTableDetailSql;
    }
    @Override
    public List<DbTable> getTableName(JdbcTemplate jdbcTemplate,DbBaseInfo dbBaseInfo) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sqliteGetTableNameSql);
        List<DbTable> tableList = this.getTableNameAndComments(resultList);
        return tableList;
    }

    @Override
    public void setColumnDataInfo(JdbcTemplate jdbcTemplate,List<DbTable> list,String executeSql,DbBaseInfo dbBaseInfo){
        for (int j = 0; j < list.size(); j++) {
            DbTable dbTable = list.get(j);
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(executeSql, dbTable.getTableName());
            List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (Map<String, Object> resultSet : resultList) {
                    DbColumnInfo dbColumnInfo = new DbColumnInfo();
                    dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
                    dbColumnInfo.setDataType(MapUtils.getString(resultSet, "DATA_TYPE"));
                    dbColumnInfo.setNullAble(!getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                    dbColumnInfo.setDefaultVal(MapUtils.getString(resultSet, "DATA_DEFAULT"));
                    dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                    String isPrimary = MapUtils.getString(resultSet, "ISPK");
                    dbColumnInfo.setPrimary(getStringToBoolean(isPrimary));
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
            if ("1".equals(val)) {
                return true;
            } else {
                return false;
            }
        }
    }


}
