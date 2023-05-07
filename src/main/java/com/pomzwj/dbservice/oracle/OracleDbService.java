package com.pomzwj.dbservice.oracle;

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

import java.util.*;

/**
 * oracle数据库操作
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
@Component
public class OracleDbService extends AbstractDbService {

	static final Logger log = LoggerFactory.getLogger(OracleDbService.class);
	@Value("${database.getTableNameSql.oracle}")
	String oracleGetTableNameSql;
	final static String queryTableDetailSql = "sql/oracle.sql";

	@Override
	public String getQueryTableDetailSql() {
		return queryTableDetailSql;
	}

	@Override
	public List<DbTable> getTableName(JdbcTemplate jdbcTemplate,DbBaseInfo dbBaseInfo) {
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(oracleGetTableNameSql);
		List<DbTable> tableList = this.getTableNameAndComments(resultList);
		return tableList;
	}

	@Override
	public void setColumnDataInfo(JdbcTemplate jdbcTemplate, List<DbTable> list, String executeSql, DbBaseInfo dbBaseInfo) {
		String userName = dbBaseInfo.getUserName();
		for (int j = 0; j < list.size(); j++) {
			DbTable dbTable = list.get(j);
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(executeSql, userName, dbTable.getTableName());
			List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(resultList)) {
				for (Map<String, Object> resultSet : resultList) {
					DbColumnInfo dbColumnInfo = new DbColumnInfo();
					dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "COLUMN_NAME"));
					dbColumnInfo.setDataType(MapUtils.getString(resultSet, "DATA_TYPE"));
					String dataLength = MapUtils.getString(resultSet, "DATA_LENGTH");
					String dataPrecision = MapUtils.getString(resultSet, "DATA_PRECISION");
					if (StringUtils.isNotEmpty(dataPrecision)) {
						dataLength = dataPrecision;
					}
					dbColumnInfo.setDataLength(dataLength);
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
		if (org.apache.commons.lang3.StringUtils.isEmpty(val)) {
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
