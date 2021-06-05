package com.pomzwj.utils;

import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * 类说明:JDBC连接
 *
 * @author zhaowenjie<1 5 1 3 0 4 1 8 2 0 @ qq.com>
 * @date 2018/10/30/0030.
 */
public class DbConnnecttion {
    static final Logger log = LoggerFactory.getLogger(DbConnnecttion.class);
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

    public static void closeRs(ResultSet rs) {
        if (rs != null) {
            try {
                Statement statement = rs.getStatement();
                rs.close();
                if(statement!=null){
                    Connection connection = statement.getConnection();
                    try {
                        statement.close();
                    } catch (Exception e1) {
                        log.info("关闭数据库Statement连接失败,e={}",e1);
                    }
                    if(connection != null){
                        try {
                            connection.close();
                        } catch (Exception e2) {
                            log.info("关闭数据库Connection连接失败,e={}",e2);
                        }
                    }

                }
            } catch (Exception e3) {
                log.error("关闭数据库ResultSet连接失败,e={}",e3);
            }
        }

    }

    public static void closeStat(Statement statement){
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                log.info("关闭数据库Statement连接失败,e={}",e);
            }
        }


    }

    public static void closeConn(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                log.error("关闭数据库Connection连接失败,e={}",e);
            }
        }


    }
}
