package com.pomzwj.dbservice.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.pomzwj.dbpool.druid.DruidPoolUtils;
import com.pomzwj.dbservice.DbService;
import com.pomzwj.domain.*;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.utils.DbConnnecttion;
import com.pomzwj.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * mysql数据库操作
 */
@Component
public class MySqlDbService implements DbService {

	static final Logger log = LoggerFactory.getLogger(MySqlDbService.class);

	@Autowired
	private DruidPoolUtils druidPoolUtils;
	@Value("${database.getTableNameSql.mysql}")
	String mysqlGetTableNameSql;

	@Override
	public List<DbTable> getTableDetailInfo(DbBaseInfo dbBaseInfo) throws Exception {
		DruidDataSource dbPool = druidPoolUtils.createDbPool(dbBaseInfo);
		try {
			//1.获取所有表基本信息
			List<DbTable> tableName = this.getTableName(dbPool.getConnection(), dbBaseInfo.getDbName());
			//2.获取每张表的每一列信息
			this.getColDetail(dbPool, tableName);
			return tableName;
		} catch (Exception e) {
			log.error("发生错误 = {}", e);
			throw e;
		} finally {
			druidPoolUtils.closeDbPool(dbPool);
		}
	}


	private List<DbTable> getTableName(Connection connection, String dbName) throws Exception {
		List<DbTable> tableList = new ArrayList<>();
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(String.format(mysqlGetTableNameSql, dbName));
			while (resultSet.next()) {
				DbTable dbTable = new DbTable();
				String tableName = resultSet.getString("TABLE_NAME");
				String tableComments = resultSet.getString("COMMENTS");
				if (StringUtils.isEmpty(tableComments)) {
					dbTable.setTableComments(FiledDefaultValue.TABLE_COMMENTS_DEFAULT);
				} else {
					dbTable.setTableComments(tableComments);
				}
				dbTable.setTableName(tableName);
				tableList.add(dbTable);
			}
		} catch (Exception e) {
			log.error("获取所有表发生错误 = {}", e);
			throw e;
		} finally {
			DbConnnecttion.closeResultSet(resultSet);
			DbConnnecttion.closeStat(statement);
		}
		return tableList;
	}

	private void getColDetail(DruidDataSource dbPool, List<DbTable> dbTableList) throws Exception {
		int pageNum = dbTableList.size();
		//如果表数量大于100则启用多线程
		if(pageNum < 100){
			this.getTabsColumnInfo(dbPool.getConnection(),dbTableList);
		}else{
			int pageSize = 10;
			int totalPage = (pageNum + pageSize - 1) / pageSize;
			boolean finishFlag = false;
			List[] listArray = new ArrayList[totalPage];
			for (int i = 0; i < totalPage; i++) {
				List<DbTable> temp = new ArrayList<>();
				for (int j = 0; j < pageSize; j++) {
					int index = i * pageSize + j;
					if (index >= pageNum) {
						finishFlag = true;
						break;
					}
					temp.add(dbTableList.get(index));
				}
				listArray[i] = temp;
				if (finishFlag) {
					break;
				}
			}
			this.getTabsColumnInfoByMultiThread(dbPool, listArray);
		}
	}

	private void getTabsColumnInfoByMultiThread(DruidDataSource dbPool, List[] listArray) throws Exception {
		ClassPathResource classPathResource = new ClassPathResource("sql/mysql.sql");
		InputStream inputStream = classPathResource.getInputStream();
		if (inputStream == null) {
			throw new FileNotFoundException("没有找到查询详细字段的SQL文件");
		}
		ExecutorService es = Executors.newFixedThreadPool(10);
		try {
			String executeSql = IOUtils.toString(inputStream, DbService.DefaultCharsetName);


			List<Future> resultFuture = new ArrayList<>();
			for (int i = 0; i < listArray.length; i++) {
				int finalI = i;
				Future<Boolean> submit = es.submit(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						ResultSet resultSet = null;
						PreparedStatement preparedStatement = null;
						try {
							System.out.println(finalI);
							List<DbTable> list = (List<DbTable>) listArray[finalI];
							for (int j = 0; j < list.size(); j++) {
								DruidPooledConnection connection = dbPool.getConnection();
								preparedStatement = connection.prepareStatement(executeSql);
								List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
								DbTable dbTable = list.get(j);
								try{
									getColumnDetail(preparedStatement,resultSet,dbTable,dbColumnInfos);
								}finally {
									DbConnnecttion.closeResultSet(resultSet);
									DbConnnecttion.closeStat(preparedStatement);
									connection.close();
								}

							}
							System.out.println(finalI);
							return true;
						} catch (Exception e) {
							e.printStackTrace();
						}
						return false;
					}
				});
				resultFuture.add(submit);
			}
			for (int i = 0; i < resultFuture.size(); i++) {
				Boolean o = (Boolean) resultFuture.get(i).get();
				if (!o) {
					throw new DatabaseExportException("导出word失败");
				}
			}
		} catch (Exception e) {
			log.error("发生错误 = {}", e);
			throw e;
		}
	}

	private void getTabsColumnInfo(Connection connection, List<DbTable> dbTableList) throws Exception {
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		ClassPathResource classPathResource = new ClassPathResource("sql/mysql.sql");
		InputStream inputStream = classPathResource.getInputStream();
		if (inputStream == null) {
			throw new FileNotFoundException("没有找到查询详细字段的SQL文件");
		}
		try{
			String executeSql = IOUtils.toString(inputStream, DbService.DefaultCharsetName);
			preparedStatement = connection.prepareStatement(executeSql);
			for (int i = 0; i < dbTableList.size(); i++) {
				List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
				DbTable dbTable = dbTableList.get(i);
				this.getColumnDetail(preparedStatement,resultSet,dbTable,dbColumnInfos);
			}
		}catch (Exception e){
			log.error("发生错误 = {}",e);
			throw e;
		}finally {
			DbConnnecttion.closeResultSet(resultSet);
			DbConnnecttion.closeStat(preparedStatement);
		}
	}

	public void getColumnDetail(PreparedStatement preparedStatement,ResultSet resultSet,DbTable dbTable,List<DbColumnInfo> dbColumnInfos)throws Exception{
		preparedStatement.setString(1, dbTable.getTableName());
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			DbColumnInfo dbColumnInfo = new DbColumnInfo();
			dbColumnInfo.setColumnName(resultSet.getString("COLUMN_NAME"));
			dbColumnInfo.setDataType(resultSet.getString("COLUMN_TYPE"));
			dbColumnInfo.setNullAble(getStringToBoolean(resultSet.getString("NULLABLE")));
			dbColumnInfo.setDefaultVal(resultSet.getString("DATA_DEFAULT"));
			dbColumnInfo.setAutoIncrement(false);
			dbColumnInfo.setPrimary(false);
			String comments = resultSet.getString("COMMENTS");
			if (StringUtils.isEmpty(comments)) {
				dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
			} else {
				dbColumnInfo.setComments(comments);
			}
			String extraInfo = resultSet.getString("EXTRA_INFO");
			if (!StringUtils.isEmpty(extraInfo) && extraInfo.contains("auto_increment")) {
				dbColumnInfo.setAutoIncrement(true);
			}
			String columnKey = resultSet.getString("COLUMN_KEY");
			if (!StringUtils.isEmpty(columnKey) && columnKey.contains("PRI")) {
				dbColumnInfo.setPrimary(true);
			}
			dbColumnInfos.add(dbColumnInfo);
		}
		dbTable.setTabsColumn(dbColumnInfos);
	}



	private static boolean getStringToBoolean(final String val) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(val)) {
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
