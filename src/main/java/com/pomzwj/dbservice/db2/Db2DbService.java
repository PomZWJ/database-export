package com.pomzwj.dbservice.db2;

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
public class Db2DbService extends AbstractDbService {
    @Override
    public String getQueryTableDetailSql() {
        return "sql/db2.sql";
    }

    @Override
    public String getQueryTableInfoSql() {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.TABNAME  AS TABLE_NAME,a.REMARKS AS COMMENTS ");
        sql.append(" from SYSCAT.TABLES a ");
        sql.append(" where 1=1 AND  TYPE = 'T' AND TABSCHEMA = '%s'");
        return sql.toString();
    }

    @Override
    public void setColumnDataInfo(JdbcTemplate jdbcTemplate, List<DbTable> list, String executeSql, DbBaseInfo dbBaseInfo) {
        String dbSchema = dbBaseInfo.getDbSchema();
        for (int j = 0; j < list.size(); j++) {
            DbTable dbTable = list.get(j);
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(executeSql, dbSchema, dbTable.getTableName());
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
