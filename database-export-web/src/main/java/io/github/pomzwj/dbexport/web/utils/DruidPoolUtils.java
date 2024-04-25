package io.github.pomzwj.dbexport.web.utils;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.pomzwj.dbexport.core.domain.DbBaseInfo;
import io.github.pomzwj.dbexport.core.type.DataBaseType;
import io.github.pomzwj.dbexport.web.params.DbBaseParams;
import io.github.pomzwj.dbexport.web.vo.DbBaseInfoVo;
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
public class DruidPoolUtils {

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
    @Value("${database.jdbc.dm}")
    String dmJdbc;
    @Value("${database.driver.dm}")
    String dmDriver;
    @Value("${database.jdbc.db2}")
    String db2Jdbc;
    @Value("${database.driver.db2}")
    String db2Driver;

    public DataSource createDbPool(DbBaseInfoVo dbBaseParams){
        String dbKind = dbBaseParams.getDbKind();
        String dbName = dbBaseParams.getDbName();
        String ip = dbBaseParams.getIp();
        String port = dbBaseParams.getPort();
        String userName = dbBaseParams.getUserName();
        String password = dbBaseParams.getPassword();
        String dbSchema = dbBaseParams.getSchemas();
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
        } else if(dataBaseType.equals(DataBaseType.DM)){
            sqlConnectionStr = dmJdbc;
            driverClassName = dmDriver;
        } else if(dataBaseType.equals(DataBaseType.DB2)){
            sqlConnectionStr = db2Jdbc;
            driverClassName = db2Driver;
        }
        String jdbcUrl = "";
        if(dataBaseType.equals(DataBaseType.SQLITE)){
            jdbcUrl = String.format(sqlConnectionStr, dbName);
        }else if(dataBaseType.equals(DataBaseType.POSTGRESQL)){
            jdbcUrl = String.format(sqlConnectionStr, ip, port, dbName,dbSchema);
        }else if(dataBaseType.equals(DataBaseType.DB2)){
            jdbcUrl = String.format(sqlConnectionStr, ip, port, dbName,dbSchema);
        }else{
            jdbcUrl = String.format(sqlConnectionStr, ip, port, dbName);
        }

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
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(10);
        return dataSource;
    }

    public void closeDbPool(DataSource dataSource){
        if(dataSource == null){
            return;
        }
        DruidDataSource druidDataSource = (DruidDataSource)dataSource;
        if(druidDataSource!=null && !druidDataSource.isClosed()){
            druidDataSource.close();
        }
    }

}
