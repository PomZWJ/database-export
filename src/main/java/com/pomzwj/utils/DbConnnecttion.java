package com.pomzwj.utils;

import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 类说明:JDBC连接
 *
 * @author zhaowenjie<1 5 1 3 0 4 1 8 2 0 @ qq.com>
 * @date 2018/10/30/0030.
 */
@Slf4j
public class DbConnnecttion {
    public static Connection getConn(String jdbcUrl, String userName, String password, String driverClassName) throws Exception {
        Connection connection = null;
        try{
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            if (connection == null) {
                throw new DatabaseExportException(MessageCode.DATABASE_LINK_IS_NULL_ERROR.getCode(), MessageCode.DATABASE_LINK_IS_NULL_ERROR.getMsg());
            }
        }catch (Exception e){
            log.error("连接数据库错误={}",e);
            if(e instanceof ClassNotFoundException){
                throw new DatabaseExportException(MessageCode.DATABASE_DRIVE_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_DRIVE_IS_NULL_ERROR.getMsg());
            }else if(e instanceof DatabaseExportException){
                throw new DatabaseExportException(MessageCode.DATABASE_LINK_IS_NULL_ERROR.getCode(),MessageCode.DATABASE_LINK_IS_NULL_ERROR.getMsg());
            }else{
                throw new RuntimeException(e);
            }


        }

        return connection;
    }

    public static void closeRs(ResultSet rs) throws Exception {
        if (rs != null) {

            rs.close();

        }
        log.info("已关闭数据库ResultSet连接");
    }

    public static void closeStat(Statement statement) throws Exception {
        if (statement != null) {

            statement.close();


        }
        log.info("已关闭数据库Statement连接");

    }

    public static void closeConn(Connection connection) throws Exception {

        if (connection != null) {

            connection.close();
        }
        log.info("已关闭数据库Connection连接");

    }
}
