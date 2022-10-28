package com.pomzwj.dbpool.hikaricp;

import com.pomzwj.constant.DataBaseType;
import com.pomzwj.dbpool.DbPoolService;
import com.pomzwj.domain.DbBaseInfo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * HikariCpPool配置
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2021-12-16
 */
@Component
public class HikariCpPoolUtils implements DbPoolService {
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

	@Override
	public DataSource createDbPool(DbBaseInfo dbBaseInfo) {
		String dbKind = dbBaseInfo.getDbKind();
		String dbName = dbBaseInfo.getDbName();
		String ip = dbBaseInfo.getIp();
		String port = dbBaseInfo.getPort();
		String userName = dbBaseInfo.getUserName();
		String password = dbBaseInfo.getPassword();
		String sqlConnectionStr = null;
		String driverClassName = null;
		DataBaseType dataBaseType = DataBaseType.matchType(dbKind);
		if (dataBaseType.equals(DataBaseType.MYSQL)) {
			sqlConnectionStr = mysqlJdbc;
			driverClassName = mysqlDriver;
		} else if (dataBaseType.equals(DataBaseType.ORACLE)) {
			sqlConnectionStr = oracleJdbc;
			driverClassName = oracleDriver;
		} else if (dataBaseType.equals(DataBaseType.SQLSERVER)) {
			sqlConnectionStr = sqlServerJdbc;
			driverClassName = sqlServerDriver;
		} else if (dataBaseType.equals(DataBaseType.POSTGRESQL)) {
			sqlConnectionStr = postgresqlJdbc;
			driverClassName = postgresqlDriver;
		} else if(dataBaseType.equals(DataBaseType.CLICKHOUSE)){
			sqlConnectionStr = clickhouseJdbc;
			driverClassName = clickhouseDriver;
		}
		String jdbcUrl = String.format(sqlConnectionStr, ip, port, dbName);
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(jdbcUrl);
		hikariConfig.setDriverClassName(driverClassName);
		hikariConfig.setUsername(userName);
		hikariConfig.setPassword(password);
		hikariConfig.setMaximumPoolSize(20);
		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		return dataSource;
	}

	@Override
	public void closeDbPool(DataSource dataSource) {
		HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
		if (hikariDataSource != null && !hikariDataSource.isClosed()) {
			hikariDataSource.close();
		}
	}
}
