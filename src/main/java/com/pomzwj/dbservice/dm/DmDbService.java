package com.pomzwj.dbservice.dm;

import com.pomzwj.dbservice.AbstractDbService;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.FiledDefaultValue;
import com.pomzwj.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DmDbService extends AbstractDbService {
    @Override
    public String getQueryTableDetailSql() {
        return "sql/dm.sql";
    }

    @Override
    public String getQueryTableInfoSql() {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.object_name as TABLE_NAME,b.comment$ as COMMENTS ");
        sql.append(" from dba_objects a left join SYSTABLECOMMENTS b on b.tvname = a.object_name ");
        sql.append(" where a.object_type = 'TABLE' and a.owner = '%s'  ");
        sql.append(" and a.object_name not like 'SREF_CON%%' and a.object_name not like 'GEN_%%'");
        return sql.toString();
    }

    @Override
    public void setColumnDataInfo(JdbcTemplate jdbcTemplate, List<DbTable> list, String executeSql, DbBaseInfo dbBaseInfo) {
        String dbName = dbBaseInfo.getDbName();
        for (int j = 0; j < list.size(); j++) {
            DbTable dbTable = list.get(j);
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(executeSql, dbName,dbName, dbTable.getTableName());
            List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (Map<String, Object> resultSet : resultList) {
                    DbColumnInfo dbColumnInfo = new DbColumnInfo();
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
            if ("Y".equals(val)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
