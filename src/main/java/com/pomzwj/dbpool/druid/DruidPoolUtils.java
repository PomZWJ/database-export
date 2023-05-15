package com.pomzwj.dbpool.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.pomzwj.constant.DataBaseType;
import com.pomzwj.dbpool.DbPoolService;
import com.pomzwj.domain.DbBaseInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2021-06-25
 */
@Component
public class DruidPoolUtils implements DbPoolService {

	@Value("${database.jdbc.mysql}")
	String mysqlJdbc;
	@Value("${database.driver.mysql}")
	String mysqlDriver;
	@Value("${database.jdbc.oracle}")
	String oracleJdbc;
	@Value("${database.driver.oracle}")
	String oracleDriver;
	@Value("${database.jdbc.sqlServer}")
	String sqlServerJdbc;
	@Value("${database.driver.sqlServer}")
	String sqlServerDriver;
	@Value("${database.jdbc.postgresql}")
	String postgresqlJdbc;
	@Value("${database.driver.postgresql}")
	String postgresqlDriver;
	@Value("${database.jdbc.clickhouse}")
	String clickhouseJdbc;
	@Value("${database.driver.clickhouse}")
	String clickhouseDriver;
	@Value("${database.jdbc.sqlite}")
	String sqliteJdbc;
	@Value("${database.driver.sqlite}")
	String sqliteDriver;

	@Override
	public DataSource createDbPool(DbBaseInfo dbBaseInfo){
		String dbKind = dbBaseInfo.getDbKind();
		String dbName = dbBaseInfo.getDbName();
		String ip = dbBaseInfo.getIp();
		String port = dbBaseInfo.getPort();
		String userName = dbBaseInfo.getUserName();
		String password = dbBaseInfo.getPassword();
		String sqlConnectionStr = null;
		String driverClassName = null;
		DataBaseType dataBaseType = DataBaseType.matchType(dbKind);
		if(dataBaseType.equals(DataBaseType.MYSQL)){
			sqlConnectionStr = mysqlJdbc;
			driverClassName = mysqlDriver;
		}else if(dataBaseType.equals(DataBaseType.ORACLE)){
			sqlConnectionStr = oracleJdbc;
			driverClassName = oracleDriver;
		}else if(dataBaseType.equals(DataBaseType.SQLSERVER)){
			sqlConnectionStr = sqlServerJdbc;
			driverClassName = sqlServerDriver;
		}else if(dataBaseType.equals(DataBaseType.POSTGRESQL)){
			sqlConnectionStr = postgresqlJdbc;
			driverClassName = postgresqlDriver;
		}else if(dataBaseType.equals(DataBaseType.CLICKHOUSE)){
			sqlConnectionStr = clickhouseJdbc;
			driverClassName = clickhouseDriver;
		} else if(dataBaseType.equals(DataBaseType.SQLITE)){
			sqlConnectionStr = sqliteJdbc;
			driverClassName = sqliteDriver;
		}
		String jdbcUrl = String.format(sqlConnectionStr, ip, port, dbName);
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(jdbcUrl);
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);
		dataSource.setInitialSize(10);
		dataSource.setMinIdle(10);
		dataSource.setMaxWait(60*1000);
		dataSource.setMaxActive(20);
		dataSource.setTestWhileIdle(true);
		//dataSource.setValidationQuery("SELECT 'x'");
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(10);
		return dataSource;
	}

	@Override
	public void closeDbPool(DataSource dataSource){
		DruidDataSource druidDataSource = (DruidDataSource)dataSource;
		if(druidDataSource!=null && !druidDataSource.isClosed()){
			druidDataSource.close();
		}
	}

}
