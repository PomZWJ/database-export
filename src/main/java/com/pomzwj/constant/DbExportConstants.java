package com.pomzwj.constant;

import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 类说明:常用字段
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */
@Component
public class DbExportConstants {
    @Value("${database.jdbc.mysql}")
    String mysqlJdbc;
    @Value("${database.jdbc.oracle}")
    String oracleJdbc;
    @Value("${database.jdbc.sqlServer}")
    String sqlServerJdbc;

    @Value("${database.driver.mysql}")
    String mysqlDriver;
    @Value("${database.driver.oracle}")
    String oracleDriver;
    @Value("${database.driver.sqlServer}")
    String sqlServerDriver;

    @Value("${database.getTableNameSql.mysql}")
    String mysqlGetTableNameSql;
    @Value("${database.getTableNameSql.oracle}")
    String oracleGetTableNameSql;
    @Value("${database.getTableNameSql.sqlServer}")
    String sqlServerGetTableNameSql;

    @Value("${database.getColumnNameInfoSql.mysql}")
    String mysqlGetColumnNameInfoSql;
    @Value("${database.getColumnNameInfoSql.oracle}")
    String oracleGetColumnNameInfoSql;
    @Value("${database.getColumnNameInfoSql.sqlServer}")
    String sqlServerGetColumnNameInfoSql;


    /**
     * 得到连接地址
     * @param dbKind
     * @param ip
     * @param port
     * @param dbName
     * @return
     */
    public String getJdbcUrl(String dbKind, String ip, String port, String dbName) {
        String url = null;
        dbKind = dbKind.toUpperCase();
        DataBaseType dataBaseType = DataBaseType.matchType(dbKind);
        switch (dataBaseType){
            case MYSQL:
                url = String.format(mysqlJdbc,ip,port,dbName);
                break;
            case ORACLE:
                url = String.format(oracleJdbc,ip,port,dbName);
                break;
            case SQLSERVER:
                url = String.format(sqlServerJdbc,ip,port,dbName);
                break;
        }
        return url;
    }

    /**
     * 得到驱动名称
     * @param dbKind
     * @return
     */
    public String getDriverClassName(String dbKind) {
        String driverClassName = null;
        dbKind = dbKind.toUpperCase();
        DataBaseType dataBaseType = DataBaseType.matchType(dbKind);
        switch (dataBaseType){
            case MYSQL:
                driverClassName = mysqlDriver;
                break;
            case ORACLE:
                driverClassName = oracleDriver;
                break;
            case SQLSERVER:
                driverClassName = sqlServerDriver;
                break;
        }
        return driverClassName;
    }

    /**
     * 获取得到所有表和表注释的SQL语句
     * @param dbKind
     * @param dbName--mysql需要数据库名称
     * @return
     */
    public String getTableNameSql(String dbKind ,String dbName){
        String sql = null;
        dbKind = dbKind.toUpperCase();
        DataBaseType dataBaseType = DataBaseType.matchType(dbKind);
        switch (dataBaseType) {
            case MYSQL:
                sql = String.format(mysqlGetTableNameSql, dbName);
                break;
            case ORACLE:
                sql = oracleGetTableNameSql;
                break;
            case SQLSERVER:
                sql = sqlServerGetTableNameSql;
                break;
        }
        return sql;
    }

    /**
     * 得到SQL
     * @param dbKind
     * @param tableName
     * @return
     */
    public String getColumnNameInfoSQL(String dbKind, String tableName) {
        String sql = null;
        dbKind = dbKind.toUpperCase();
        DataBaseType dataBaseType = DataBaseType.matchType(dbKind);
        switch (dataBaseType) {
            case MYSQL:
                sql = String.format(mysqlGetColumnNameInfoSql, tableName);
                break;
            case ORACLE:
                sql = String.format(oracleGetColumnNameInfoSql, tableName);;
                break;
            case SQLSERVER:
                sql = String.format(sqlServerGetColumnNameInfoSql, tableName);;
                break;
        }
        return sql;
    }
}
