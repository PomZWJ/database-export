package com.pomzwj.dbservice.oracle;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pomzwj.dbpool.druid.DruidPoolUtils;
import com.pomzwj.dbservice.AbstractDbService;
import com.pomzwj.dbservice.DbService;
import com.pomzwj.domain.*;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

/**
 * oracle数据库操作
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
@Component
public class OracleDbService extends AbstractDbService implements DbService {

	static final Logger log = LoggerFactory.getLogger(OracleDbService.class);

	@Autowired
	private DruidPoolUtils druidPoolUtils;
	@Value("${database.getTableNameSql.oracle}")
	String oracleGetTableNameSql;

	@Override
	public List<DbTable> getTableDetailInfo(DbBaseInfo dbBaseInfo) throws Exception {
		//获取数据库线程池
		DruidDataSource dbPool = null;
		try {
			dbPool = druidPoolUtils.createDbPool(dbBaseInfo);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dbPool);
			List<DbTable> tableName = this.getTableName(jdbcTemplate);
			//2.1 把List进行分页
			List[] listArray = this.listToPageArray(tableName);
			//2.2进行表列的数据获取
			this.getTableColumnInfoByMultiThread(jdbcTemplate, listArray, dbBaseInfo.getUserName());
			return tableName;
		} catch (Exception e) {
			log.error("生成oracle数据发生错误 = {}", e);
			throw e;
		} finally {
			druidPoolUtils.closeDbPool(dbPool);
		}
	}


	private List<DbTable> getTableName(JdbcTemplate jdbcTemplate) {
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(oracleGetTableNameSql);
		List<DbTable> tableList = this.getTableNameAndComments(resultList);
		return tableList;
	}

	private void getTableColumnInfoByMultiThread(JdbcTemplate jdbcTemplate, List[] listArray, String userName) throws Exception {

		ClassPathResource classPathResource = new ClassPathResource("sql/oracle.sql");
		InputStream inputStream = classPathResource.getInputStream();
		if (inputStream == null) {
			throw new FileNotFoundException("没有找到查询详细字段的SQL文件");
		}
		String executeSql = IOUtils.toString(inputStream, DbService.DefaultCharsetName);

		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
		ExecutorService es = new ThreadPoolExecutor(5, 10, 200L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), namedThreadFactory);
		try {
			List<Future> resultFuture = new ArrayList<>();
			for (int i = 0; i < listArray.length; i++) {
				int finalI = i;
				Future<Boolean> submit = es.submit(() -> {
					try {
						List<DbTable> list = (List<DbTable>) listArray[finalI];
						setColumnDataInfo(jdbcTemplate, list, executeSql, userName);
						return true;
					} catch (Exception e) {
						log.error("多线程查询表的列信息失败,e={}", e);
					}
					return false;
				});
				resultFuture.add(submit);
			}
			for (int i = 0; i < resultFuture.size(); i++) {
				Boolean o = (Boolean) resultFuture.get(i).get();
				if (!o) {
					throw new DatabaseExportException("导出word失败");
				}
			}
		} finally {
			if (es != null) {
				es.shutdown();
			}
			if(inputStream!=null){
				inputStream.close();
			}
		}

	}

	public void setColumnDataInfo(JdbcTemplate jdbcTemplate, List<DbTable> list, String executeSql, String userName) {
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
