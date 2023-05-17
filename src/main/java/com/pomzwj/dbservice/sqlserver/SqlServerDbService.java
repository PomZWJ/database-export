package com.pomzwj.dbservice.sqlserver;

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
 * sqlserver数据库
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
@Component
public class SqlServerDbService extends AbstractDbService {

    static final Logger log = LoggerFactory.getLogger(SqlServerDbService.class);

    @Override
    public String getQueryTableDetailSql() {
        return "sql/sqlserver.sql";
    }

    @Override
    public String getQueryTableInfoSql() {
        return "select TABLE_NAME=d.name,COMMENTS=f.value  from sysobjects d left join sys.extended_properties f on d.id=f.major_id and f.minor_id=0 where d.xtype = 'u' and d.name != 'sysdiagrams'";
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
                    dbColumnInfo.setDataLength(MapUtils.getString(resultSet, "DATA_LENGTH"));
                    dbColumnInfo.setNullAble(getStringToBoolean(MapUtils.getString(resultSet, "NULLABLE")));
                    dbColumnInfo.setDefaultVal(MapUtils.getString(resultSet, "DATA_DEFAULT"));
                    dbColumnInfo.setDataScale(MapUtils.getString(resultSet, "DATA_SCALE"));
                    String comments = MapUtils.getString(resultSet, "COMMENTS");
                    dbColumnInfo.setAutoIncrement(getStringToBoolean(MapUtils.getString(resultSet, "AUTOINCREMENT")));
                    dbColumnInfo.setPrimary(getStringToBoolean(MapUtils.getString(resultSet, "PRIMARY_KEY")));
                    if (StringUtils.isEmpty(comments)) {
                        dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                    } else {
                        dbColumnInfo.setComments(comments);
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
            if ("TRUE".equals(val)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
